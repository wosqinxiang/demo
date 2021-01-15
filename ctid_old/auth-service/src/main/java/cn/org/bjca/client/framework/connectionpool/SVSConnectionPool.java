package cn.org.bjca.client.framework.connectionpool;

import bjca.org.util.LoggerUtil;
import cn.org.bjca.client.config.PropertiesConfigForSVS;
import cn.org.bjca.client.exceptions.ConnectException;
import cn.org.bjca.client.framework.SVSAddress;
import cn.org.bjca.client.framework.SVSConnection;
import cn.org.bjca.client.framework.connection.Connection;
import cn.org.bjca.client.security.DefaultSecurityEngineDeal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class SVSConnectionPool implements ConnectionPool {
	private static SVSConnectionPool poolInstance;
	private static Vector freeConnections;
	private static Vector errorConnections;
	private static Vector usedConnections;
	private static ArrayList addressList = new ArrayList();
	private static int maxConnNum = 20;
	private static int minConnNum = 10;
	private static int timeout = 1000;
	private static long repairInterval = 300000L;
	private static long checkFreeConnections = 600000L;

	public static SVSConnectionPool getInstance() {
		if (poolInstance == null) {
			synchronized (SVSConnectionPool.class) {
				if (poolInstance == null) {
					poolInstance = new SVSConnectionPool();
				}
			}
		}

		return poolInstance;
	}

	static synchronized void heatbeat() {
		DefaultSecurityEngineDeal safeEng = new DefaultSecurityEngineDeal("BJCADevice");

		try {
			safeEng.getActiveThreadNum();
		} catch (Exception var2) {
			LoggerUtil.errorlog("heatbeat error !" + var2);
		}

	}

	static synchronized void checkFreeConnections() {
		while (freeConnections.size() > PropertiesConfigForSVS.getMaxConnNum()) {
			freeConnections.remove(0);
		}

		while (errorConnections.size() > PropertiesConfigForSVS.getMaxConnNum()) {
			errorConnections.remove(0);
		}

	}

	static synchronized void timeTask() {
		Timer timer = new Timer(true);
		TimerTask timeTask = new TimerTask() {
			public void run() {
				repairConnection();
			}
		};
		timer.schedule(timeTask, 0L, repairInterval);

		Timer timerHeatbeatTime = new Timer(true);
		TimerTask timerHeatbeatTimeTask = new TimerTask() {
			public void run() {
				heatbeat();
			}
		};
		timerHeatbeatTime.schedule(timerHeatbeatTimeTask, 0L, PropertiesConfigForSVS.getHeatbeatTime());

		Timer timerCheckFreeConnections = new Timer(true);
		TimerTask timerCheckFreeConnectionsTimeTask = new TimerTask() {
			public void run() {
				checkFreeConnections();
			}
		};
		timerCheckFreeConnections.schedule(timerCheckFreeConnectionsTimeTask, 0L, checkFreeConnections);
	}

	static void repairConnection() {
		int errorConnNum = errorConnections.size();
		LoggerUtil.debuglog("The total number of wrong connection == " + errorConnNum);
		if (errorConnNum > 0) {
			int removeFlag = 0;

			for (int i = 0; i < errorConnNum; ++i) {
				LoggerUtil.debuglog("remove flag == " + removeFlag);
				LoggerUtil.debuglog("rest error connection number == " + errorConnections.size());
				LoggerUtil.debuglog("The wrong connection pool size == " + errorConnections.size());
				LoggerUtil.debuglog("Get the wrong connection == " + (i - removeFlag));
				LoggerUtil.debuglog("The free connection pool size == " + freeConnections.size());
				SVSConnection errorConn = (SVSConnection) errorConnections.get(i - removeFlag);

				try {
					errorConn.getConn().close();
				} catch (Exception var5) {
					LoggerUtil.debuglog("Closed error connection error !", var5);
				}

				try {
					SVSConnection newSvsConn = newConnection(errorConn.getSvsAddress());
					if (newSvsConn.getConn() != null) {
						errorConnections.remove(i - removeFlag);
						++removeFlag;
						freeConnections.add(newSvsConn);
						LoggerUtil.debuglog(
								"Repairing connection success ! The address is " + errorConn.getSvsAddress().getIp());
						LoggerUtil.systemlog(
								"Repairing connection success ! The address is " + errorConn.getSvsAddress().getIp());
					}

					LoggerUtil.systemlog("The free connection number is == " + freeConnections.size());
					LoggerUtil.systemlog("The error connection number is == " + errorConnections.size());
					LoggerUtil.systemlog("The usedConnNum connection number is == " + usedConnections.size());
				} catch (Exception var6) {
					LoggerUtil.debuglog("Exception in repairing the error connection !", var6);
					if (errorConn != null) {
						LoggerUtil.debuglog("The error connection ip is " + errorConn.getSvsAddress().getIp());
					}
				}
			}
		} else {
			LoggerUtil.debuglog("The error connection is zero !");
		}

	}

	private SVSConnectionPool() {
		addressList = PropertiesConfigForSVS.getAddressList();
		maxConnNum = PropertiesConfigForSVS.getMaxConnNum();
		minConnNum = PropertiesConfigForSVS.getMinConnNum();
		timeout = PropertiesConfigForSVS.getTimeout();
		freeConnections = new Vector();
		usedConnections = new Vector();
		errorConnections = new Vector();
		int connNum = minConnNum;
		if (maxConnNum < minConnNum) {
			connNum = maxConnNum;
		}

		if (connNum > 500) {
			connNum = 500;
		}

		for (int i = 0; i < connNum; ++i) {
			int addressLen = addressList.size();
			SVSConnection svsConn = null;

			try {
				SVSAddress address = (SVSAddress) addressList.get(i % addressLen);
				svsConn = newConnection(address);
				if (svsConn.getConn() == null) {
					errorConnections.add(svsConn);
				} else {
					freeConnections.add(svsConn);
				}

				LoggerUtil.systemlog("The free connection number is == " + freeConnections.size());
				LoggerUtil.systemlog("The error connection number is == " + errorConnections.size());
				LoggerUtil.systemlog("The usedConnNum connection number is == " + usedConnections.size());
			} catch (Exception var6) {
				errorConnections.add(svsConn);
				LoggerUtil.debuglog("Create connection error ,when initialization connection pool! ", var6);
				LoggerUtil.errorlog("Create connection error ,when initialization connection pool! ", var6);
			}
		}

		timeTask();
	}

	public synchronized SVSConnection getC() throws ConnectException {
		SVSConnection svsConn = null;
		long startTime = (new Date()).getTime();
		if (freeConnections.size() > 0) {
			svsConn = (SVSConnection) freeConnections.firstElement();
			usedConnections.add(svsConn);
			freeConnections.remove(svsConn);
			LoggerUtil.debuglog("The free connection number is == " + freeConnections.size());
			LoggerUtil.debuglog("The error connection number is == " + errorConnections.size());
			LoggerUtil.debuglog("The usedConnNum connection number is == " + usedConnections.size());
		} else if (maxConnNum != 0 && usedConnections.size() >= maxConnNum) {
			if (errorConnections.size() >= maxConnNum) {
				return null;
			}

			try {
				if (timeout == 0) {
					this.wait();
				} else {
					this.wait((long) timeout);
				}
			} catch (InterruptedException var5) {
				LoggerUtil.debuglog("Connection pool InterruptedException!", var5);
				LoggerUtil.errorlog("Connection pool InterruptedException!", var5);
			}

			if (timeout != 0 && (new Date()).getTime() - startTime > (long) timeout) {
				return null;
			}

			svsConn = this.getC();
		} else {
			SVSAddress svsAddress = (SVSAddress) addressList.get(0);
			addressList.remove(0);
			addressList.add(svsAddress);
			svsConn = newConnection(svsAddress);
			usedConnections.add(svsConn);
			LoggerUtil.systemlog("The free connection number is == " + freeConnections.size());
			LoggerUtil.systemlog("The error connection number is == " + errorConnections.size());
			LoggerUtil.systemlog("The usedConnNum connection number is == " + usedConnections.size());
		}

		return svsConn;
	}

	private static SVSConnection newConnection(SVSAddress svsAddress) throws ConnectException {
		Connection con = null;
		SVSConnection svsConn = new SVSConnection();
		svsConn.setSvsAddress(svsAddress);

		try {
			con = new Connection(svsAddress.getIp(), svsAddress.getPort());
		} catch (Exception var4) {
			LoggerUtil.errorlog(svsAddress.getIp() + " connection error !", var4);
			LoggerUtil.debuglog(svsAddress.getIp() + " connection error !", var4);
			throw new ConnectException("Connect server error !");
		}

		LoggerUtil.debuglog("Create new connection result == " + con);
		LoggerUtil.systemlog("-------------------------------------------------------");
		LoggerUtil.systemlog(
				"Create new connection success ! The address is " + svsAddress.getIp() + ":" + svsAddress.getPort());
		svsConn.setConn(con);
		return svsConn;
	}

	public synchronized void freeConnection(SVSConnection conn) {
		freeConnections.addElement(conn);
		usedConnections.remove(conn);
		this.notifyAll();
	}

	public synchronized void setErrorConnection(SVSConnection conn) {
		errorConnections.add(conn);
		usedConnections.remove(conn);
		this.notifyAll();
	}

	public boolean release() {
		Enumeration allFreeConnections = freeConnections.elements();

		while (allFreeConnections.hasMoreElements()) {
			SVSConnection svsFreeCon = (SVSConnection) allFreeConnections.nextElement();

			try {
				Connection freeCon = svsFreeCon.getConn();
				if (freeCon != null) {
					freeCon.close();
				}
			} catch (IOException var8) {
				LoggerUtil.errorlog("Exception in closed connection!", var8);
				LoggerUtil.debuglog("Exception in closed connection!", var8);
			}
		}

		freeConnections.removeAllElements();
		Enumeration allErrorConnections = errorConnections.elements();

		while (allErrorConnections.hasMoreElements()) {
			SVSConnection svsErrorCon = (SVSConnection) allErrorConnections.nextElement();

			try {
				if (svsErrorCon != null) {
					Connection errorCon = svsErrorCon.getConn();
					if (errorCon != null) {
						errorCon.close();
					}
				}
			} catch (IOException var7) {
				LoggerUtil.errorlog("Exception in closed connection!", var7);
				LoggerUtil.debuglog("Exception in closed connection!", var7);
			}
		}

		errorConnections.removeAllElements();
		Enumeration allusedConnections = usedConnections.elements();

		while (allusedConnections.hasMoreElements()) {
			SVSConnection svsUsedCon = (SVSConnection) allusedConnections.nextElement();

			try {
				Connection usedCon = svsUsedCon.getConn();
				if (usedCon != null) {
					usedCon.close();
				}
			} catch (IOException var6) {
				LoggerUtil.errorlog("Exception in closed connection!", var6);
				LoggerUtil.debuglog("Exception in closed connection!", var6);
			}
		}

		usedConnections.removeAllElements();
		poolInstance = null;
		return true;
	}

	public static void main(String[] args) {
	}

	public static int getFreeConnectionsNum() {
		return freeConnections.size();
	}

	public static int getErrorConnectionsNum() {
		return errorConnections.size();
	}

	public static Vector getErrorConnections() {
		return errorConnections;
	}

	public static void setErrorConnections(Vector errorConnections) {
		errorConnections = errorConnections;
	}
}