package com.yuhang.commandManager.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 默认任务消息输出处理
 * 
 */
@Service
public class DefaultOutHandlerMethod implements OutHandlerMethod {


	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 任务是否异常中断，如果
	 */
	public boolean isBroken = false;

	@Override
	public void parse(String id, String msg) {
		// 过滤消息
		if (msg.indexOf("fail") != -1) {	
			logger.info(id + "任务可能发生故障：" + msg);
			logger.info("失败，设置中断状态");
			isBroken = true;
		} else if (msg.indexOf("miss") != -1) {
			logger.info(id + "任务可能发生丢包：" + msg);
			logger.info("失败，设置中断状态");
			isBroken = true;
		} else if (msg.indexOf("unknown") != -1) {
			logger.info(id + "任务可能发生故障：" + msg);
			logger.info("失败，设置中断状态");
			isBroken = true;
		} else if (msg.indexOf("Operation not permitted") != -1) {
			logger.info(id + "任务可能发生故障：" + msg);
			logger.info("失败，设置中断状态");
			isBroken = true;
		} else {
			isBroken = false;
			logger.info(id + " 消息：" + msg);

		}

	}

	@Override
	public boolean isbroken() {
		return isBroken;
	}

	public boolean isBroken() {
		return isBroken;
	}

	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}
	
	

}
