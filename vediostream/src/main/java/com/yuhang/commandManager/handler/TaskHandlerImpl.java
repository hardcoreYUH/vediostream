package com.yuhang.commandManager.handler;

import java.io.IOException;

import com.yuhang.commandManager.CommandManager;
import com.yuhang.commandManager.data.CommandTasker;
import com.yuhang.commandManager.util.CommonUtil;
import com.yuhang.commandManager.util.ExecUtil;
import com.yuhang.commandManager.util.ProcessUtil;

/**
 * 任务处理实现
 * 
 */
public class TaskHandlerImpl implements TaskHandler {

	private OutHandlerMethod ohm = null;

	public TaskHandlerImpl(OutHandlerMethod ohm) {
		this.ohm = ohm;
	}

	public void setOhm(OutHandlerMethod ohm) {
		this.ohm = ohm;
	}

	@Override
	public CommandTasker process(String id, String command) {
		CommandTasker tasker = null;
		try {
			tasker = ExecUtil.createTasker(id, command, ohm);

			if (CommandManager.config.isDebug())
				CommonUtil.logger(this.getClass()).info(id + " 执行命令行：" + command);

			return tasker;
		} catch (IOException e) {
			// 运行失败，停止任务
			ExecUtil.stop(ProcessUtil.getProcessId(tasker.getProcess()));

			if (CommandManager.config.isDebug())
				CommonUtil.logger(this.getClass()).error(id + " 执行命令失败！进程和输出线程已停止");

			// 出现异常说明开启失败，返回null
			return null;
		}
	}

	@Override
	public boolean stop(String process) {
		return ExecUtil.stop(process);
	}

	@Override
	public boolean stop(Thread outHandler) {
		return ExecUtil.stop(outHandler);
	}

	@Override
	public boolean stop(String process, Thread thread) {
		CommonUtil.logger(this.getClass()).error("process = " + process + " -- thread " + thread);
		boolean ret = false;
		if(thread instanceof OutHandler){			
			ret = stop(thread);
		}
		ret = stop(process);
		return ret;
	}
}
