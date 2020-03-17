package com.yuhang.commandManager.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yuhang.commandManager.data.TaskDao;
import com.yuhang.commandManager.util.ExecUtil;

/**
 * 任务保活处理器（一个后台保活线程，用于处理异常中断的持久任务）
 *
 */
@Service
public class KeepAliveHandler extends Thread {

	static Logger  logger = LoggerFactory.getLogger(KeepAliveHandler.class);
	/** 待处理队列 */
	private static Queue<String> queue = null;

	public int err_index = 0;// 错误计数

	public volatile int stop_index = 0;// 安全停止线程标记

	/** 任务持久化器 */
	@Resource
	private TaskDao taskDao;

	/** 任务校验 */
	static Map<String, Object> map = new HashMap<String, Object>();

	public KeepAliveHandler() {
		queue = new ConcurrentLinkedQueue<>();
		start();
	}

	public static void add(String id) {
		
		if (map.get(id) == null) {
			if (queue != null) {
				logger.debug("检测到中断，提交重启任务给保活处理器");
				queue.offer(id);
				map.put(id, id);
			}
		}
	}

	public boolean stop(Process process) {
		if (process != null) {
			process.destroy();
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		for (; stop_index == 0;) {
			logger.info("保活器自检" + System.currentTimeMillis());
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				logger.error("保活器阻塞异常" + e1.getMessage());
			}
			if (queue == null) {
				continue;
			}
			String id = null;

			String task = null;

			try {
				while (queue.peek() != null) {
					logger.error("准备重启任务：" + queue);
					id = queue.poll();
					task = taskDao.get(id);
					// 重启任务
					ExecUtil.restart(task,taskDao);

					map.remove(id);
				}
			} catch (IOException e) {
				logger.error(id + " 任务重启失败，详情：" + task.split("/")[0]);
				// 重启任务失败
				err_index++;
			} catch (Exception e) {
				logger.error(e.getMessage());			
				e.getStackTrace();
			}
		}
	}

	@Override
	public void interrupt() {
		stop_index = 1;
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}


}
