package com.yuhang.commandManager.handler;
/**
 * 输出消息处理
 *
 */
public interface OutHandlerMethod {
	/**
	 * 解析消息
	 * @param id-任务ID
	 * @param msg -消息
	 */
	public void parse(String id, String msg);
	
	/**
	 * 任务是否异常中断
	 * @return
	 */
	public boolean isbroken();
}
