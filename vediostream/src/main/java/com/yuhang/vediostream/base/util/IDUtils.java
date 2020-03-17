package com.yuhang.vediostream.base.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class IDUtils {

	/**
	 * 主键生成
	 * 
	 */ 
	public static long genItemId1() {
		//取当前时间的长整形值包含毫秒
		long millis = System.currentTimeMillis(); 
		//加上两位随机数
		Random random = new Random(); 
		int end2 = random.nextInt(99);
		//如果不足两位前面补0 
		String str = millis + String.format("%02d", end2); 
		long id = new Long(str); return id; 
	} 
	
	
	private static AtomicLong atomicTimeMills = new AtomicLong(0);

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static long genItemId() {
	    while (true) {
	        long currentMill = atomicTimeMills.get();
	        Long currentTimeMillis = Long.parseLong(LocalDateTime.now().format(formatter) + System.nanoTime()%100);
	        if (currentTimeMillis > currentMill && atomicTimeMills.compareAndSet(currentMill, currentTimeMillis)) {
	            return Long.parseLong(currentTimeMillis.toString());
	        }
	    
	    }
	}

	public static void main(String[] args) {
		System.out.println(IDUtils.genItemId());
	}
}


