package com.ahdms.ctidservice.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {
	
	public static 	ThreadPoolExecutor pool = new ThreadPoolExecutor(100, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024));
	
	public static void execute(Runnable r){
		try {
			pool.execute(r);
		} catch (Exception e) {
			
		}
	}
	

}
