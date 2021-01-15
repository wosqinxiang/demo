/**
 * Created on 2019年11月6日 by liuyipin
 */
package com.ahdms.ap.config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */ 
@Component
public class FastDFSPool {

	private static int INIT_POOL_SIZE ;
	
	@Value("${fsdf_init_pool_size}")
	public void set(int size){
		FastDFSPool.INIT_POOL_SIZE = size;
	}
	
	@Value("${fdfs.conf.charset:ISO8859-1}")
	private String charset;
	
	@Value("${fdfs.conf.connectTimeout:30}")
	private int connectTimeout;
	
	@Value("${fdfs.conf.networkTimeout:60}")
	private int networkTimeout;
	
	@Value("${fdfs.conf.trackerHttpPort:80}")
	private int tracker_http_port;
	
	@Value("${fdfs.conf.trackerServers}")
	private String trackerServers;
	
	

	public static final int MAX_POOL_SIZE = 256;
	public static final int MIN_POOL_SIZE = 8; 
	public static final int NETWORK_TIMEOUT = 50000;// ms < 60s
	public static final int NETWORK_CHECK_INTERVAL = 5000;
	private static final Logger logger = LoggerFactory.getLogger(FastDFSPool.class);
	private FastDFSPool instance;

	List<StorageClient> avaliableStorageClients = new ArrayList<>();
	List<StorageClient> inusingStorageClients = new ArrayList<>();
	Map<StorageClient, Long> timemap = new HashMap<>();

	Runnable timeoutChecker = new Runnable() {
		public void run() {
			while (true) {
				try {
					Thread.sleep(NETWORK_CHECK_INTERVAL);
					synchronized (FastDFSPool.class) {
						logger.debug("timeout checker");
						StorageClient keys[] = timemap.keySet().toArray(new StorageClient[timemap.keySet().size()]);
						Long now = System.currentTimeMillis();
						for (int i = 0; i < keys.length; i++) {
							StorageClient sc = keys[i];
							if ((now - timemap.get(sc)) > NETWORK_TIMEOUT) {
//								logger.info("{} timeout, remove", sc);
								avaliableStorageClients.remove(sc);
								inusingStorageClients.remove(sc);
								timemap.remove(sc);
							    
							}
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	};

	@PostConstruct
	public void init(){
		try {
			logger.info("init fdfs client");
			initClientGlobal();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		(new Thread(timeoutChecker)).start();
		increasePool(INIT_POOL_SIZE);
	}
	
	private void initClientGlobal(){
		ClientGlobal.g_charset = charset;
		ClientGlobal.g_connect_timeout = connectTimeout*1000;
		ClientGlobal.g_network_timeout = networkTimeout*1000;
		ClientGlobal.g_tracker_http_port = tracker_http_port;
		
		String[] trackerServersStrs = trackerServers.split(",");
		
		InetSocketAddress[] tracker_servers = new InetSocketAddress[trackerServersStrs.length];
		for(int i=0;i<tracker_servers.length;i++){
			String[] split = trackerServersStrs[i].split("\\:",2);
			tracker_servers[i] = new InetSocketAddress(split[0],Integer.parseInt(split[1]));
		}
		ClientGlobal.g_tracker_group = new TrackerGroup(tracker_servers);
	}

	private void increasePool(int size) {
		for (int i = 0; i < size && avaliableStorageClients.size() < MAX_POOL_SIZE; i++) {
			addOneGroup();
		}
	}

	/**
	 * 添加一组storage,数量等于当前所有storage数量。如何排除已经满了的服务器？
	 *
	 * @创建人 YeQiang
	 * @创建时间 2016年6月21日 @创建目的【】 @修改目的【修改人：，修改时间：】
	 */
	private void addOneGroup() {
		synchronized (FastDFSPool.class) {
			try {
				TrackerClient tc = new TrackerClient();
				TrackerServer ts = tc.getConnection();
				StorageServer[] sss = tc.getStoreStorages(ts, null);
				if (sss != null) {
					for (int i = 0; i < sss.length; i++) {
						StorageClient sc = new StorageClient(ts, sss[i]);
						timemap.put(sc, System.currentTimeMillis());
						avaliableStorageClients.add(sc);
						sss[i].close();
					}
				} else {
					logger.error("storage servers is null");
				}
				ts.close(); 
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public StorageClient getStorageClient() {
		int retry = 0;
		while (avaliableStorageClients.size() < 1) {
			increasePool(INIT_POOL_SIZE);
			if (++retry > 5) {
				break;
			}
		}

		int ascs = avaliableStorageClients.size();
		logger.debug("avaliableStorageClietns size = {}", ascs);
		if (ascs > 0) {
			synchronized (FastDFSPool.class) {
				StorageClient sc = avaliableStorageClients.get(0);
				timemap.put(sc, System.currentTimeMillis());
				avaliableStorageClients.remove(sc);
				inusingStorageClients.add(sc);
				logger.debug("get storageClient:{}", sc);
				return sc;
			}
		} else {
			logger.warn("cannot get storage client");
			return null;
		}
	}

	public void releaseStorageClient(StorageClient sc) {
		if (sc != null) {
			synchronized (FastDFSPool.class) {
				if (inusingStorageClients.remove(sc)) {
					avaliableStorageClients.add(sc);
					logger.debug("release StorageClient:{}, avaliableStorageClietns size = {}", sc,
							avaliableStorageClients.size());
				} else {
					logger.warn("cannot find StorageClient:{} in inusingStorageClients list", sc);
				}
			}
		}
	}
}


