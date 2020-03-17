package com.yuhang.commandManager.util;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.yuhang.commandManager.data.CommandTasker;
import com.yuhang.commandManager.data.TaskDao;
import com.yuhang.commandManager.handler.DefaultOutHandlerMethod;
import com.yuhang.commandManager.handler.OutHandler;
import com.yuhang.commandManager.handler.OutHandlerMethod;

/**
 * 命令行操作工具类
 *
 */
@Service
public class ExecUtil {

	/**
	 * 执行命令行并获取进程
	 * 
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public static Process exec(String cmd) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(cmd);// 执行命令获取主进程
		return process;
	}

	/**
	 * 销毁进程
	 * 
	 * @param process
	 * @return
	 */
	public static boolean stop(String pid) {
		if (pid != null) {
			ProcessUtil.killProcessByPid(pid);
			return true;
		}
		return false;
	}

	/**
	 * 销毁输出线程
	 * 
	 * @param outHandler
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean stop(Thread outHandler) {
		boolean alive = outHandler.isAlive();
		System.out.println(alive);
		if (outHandler != null && alive) {
			outHandler.stop();
			outHandler.destroy();
			return true;
		}
		return false;
	}

	/**
	 * 销毁
	 * 
	 * @param process
	 * @param outHandler
	 */
	public static void stop(Thread thread,String pid) {
		if(thread != null && pid != null){		
			stop(thread);
			stop(pid);
		}

	}

	/**
	 * 创建命令行任务
	 * 
	 * @param id
	 * @param command
	 * @return
	 * @throws IOException
	 */
	public static CommandTasker createTasker(String id, String command, OutHandlerMethod ohm) throws IOException {
		// 执行本地命令获取任务主进程
		Process process = exec(command);
		// 创建输出线程
		OutHandler outHandler = OutHandler.create(process.getErrorStream(), id, ohm);

		CommandTasker tasker = new CommandTasker(id, command, process, outHandler);

		return tasker;
	}

	/**
	 * 中断故障缘故重启
	 * 
	 * @param tasker
	 * @param taskDao 
	 * @return
	 * @throws IOException
	 */
	public static CommandTasker restart(String tasker, TaskDao taskDao) throws IOException {
		if (tasker != null) {
			String[] valus = tasker.split(",");
			String id = valus[0], command = valus[3];
			
			
			OutHandlerMethod ohm = new DefaultOutHandlerMethod();

			
			// 安全销毁命令行进程和输出子线程
			stop(valus[1]);
			stop(ProcessUtil.findThread(Long.parseLong(valus[2])));
			taskDao.remove(id);
			// 执行本地命令获取任务主进程
			Process process = exec(command);
	
			// 创建输出线程
			OutHandler outHandler = OutHandler.create(process.getErrorStream(), id, ohm);
		
			CommandTasker commandTasker = new CommandTasker(id,command,process,outHandler);
			
			taskDao.add(commandTasker);
			
			return commandTasker;
		}
		return null;
	}
}
