package com.yuhang.commandManager.util;

import com.sun.jna.Platform;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;

public class ProcessUtil {

	static Logger logger = LoggerFactory.getLogger(ProcessUtil.class);

	public static String getProcessId(Process process) {
		long pid = -1;
		Field field = null;
		if (Platform.isWindows()) {
			try {
				field = process.getClass().getDeclaredField("handle");
				field.setAccessible(true);
				pid = Kernel32.INSTANCE.GetProcessId((Long) field.get(process));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (Platform.isLinux() || Platform.isAIX()) {
			try {
				Class<?> clazz = Class.forName("java.lang.UNIXProcess");
				field = clazz.getDeclaredField("pid");
				field.setAccessible(true);
				pid = (Integer) field.get(process);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return String.valueOf(pid);
	}

	/**
	 * 关闭Linux进程
	 * 
	 * @param pid
	 *            进程的PID
	 */
	public static boolean killProcessByPid(String pid) {
		if (StringUtils.isEmpty(pid) || "-1".equals(pid)) {
			throw new RuntimeException("Pid ==" + pid);
		}
		logger.info("kill PID  -----> " + pid);
		Process process = null;
		BufferedReader reader = null;
		String command = "";
		boolean result = false;
		if (Platform.isWindows()) {
			command = "cmd.exe /c taskkill /PID " + pid + " /F /T ";
		} else if (Platform.isLinux() || Platform.isAIX()) {
			command = "kill -9 " + pid;
		}
		try {
			// 杀掉进程
			process = Runtime.getRuntime().exec(command);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
			String line = null;
			logger.info(reader.readLine());
			while ((line = reader.readLine()) != null) {
				logger.info("kill PID return info -----> " + line);
			}
			result = true;
		} catch (Exception e) {
			logger.info("杀进程出错：", e);
			result = false;
		} finally {
			if (process != null) {
				process.destroy();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {

				}
			}
		}
		return result;
	}

	public static Thread findThread(long threadId) {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		while (group != null) {
			Thread[] threads = new Thread[(int) (group.activeCount() * 1.2)];
			int count = group.enumerate(threads, true);
			for (int i = 0; i < count; i++) {
				if (threadId == threads[i].getId()) {
					return threads[i];
				}
			}
			group = group.getParent();
		}
		return null;
	}

	public static String getPid() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName(); // format: "pid@hostname"
		try {
			return name.substring(0, name.indexOf('@'));
		} catch (Exception e) {
			return "-1";
		}
	}
}
