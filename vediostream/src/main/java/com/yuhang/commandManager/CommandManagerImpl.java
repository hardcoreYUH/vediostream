package com.yuhang.commandManager;

import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.yuhang.commandManager.commandbuidler.CommandAssembly;
import com.yuhang.commandManager.commandbuidler.CommandAssemblyImpl;
import com.yuhang.commandManager.commandbuidler.CommandBuidler;
import com.yuhang.commandManager.data.CommandTasker;
import com.yuhang.commandManager.data.TaskDao;
import com.yuhang.commandManager.handler.DefaultOutHandlerMethod;
import com.yuhang.commandManager.handler.KeepAliveHandler;
import com.yuhang.commandManager.handler.OutHandlerMethod;
import com.yuhang.commandManager.handler.TaskHandler;
import com.yuhang.commandManager.handler.TaskHandlerImpl;
import com.yuhang.commandManager.util.CommonUtil;
import com.yuhang.commandManager.util.ProcessUtil;

/**
 * FFmpeg命令操作管理器
 * 
 */
@Service
public class CommandManagerImpl implements CommandManager {

	Logger logger = LoggerFactory.getLogger(CommandManagerImpl.class);
	/**
	 * 任务持久化器
	 */
	@Resource
	private TaskDao taskDao ;
	/**
	 * 任务执行处理器
	 */
	private TaskHandler taskHandler = null;
	/**
	 * 命令组装器
	 */
	private CommandAssembly commandAssembly = null;
	/**
	 * 任务消息处理器
	 */
	private OutHandlerMethod ohm = null;

	/**
	 * 保活处理器
	 */
	@Resource
	private KeepAliveHandler keepAliveHandler = null;

	/**
	 * 全部默认初始化，自动查找配置文件
	 */
	public CommandManagerImpl() {
		this(null);
	}

	/**
	 * 指定任务池大小的初始化，其他使用默认
	 * 
	 * @param size
	 */
	public CommandManagerImpl(Integer size) {
		init(size);
	}

	/**
	 * 初始化
	 * 
	 * @param taskDao
	 * @param taskHandler
	 * @param commandAssembly
	 * @param ohm
	 * @param size
	 */
	public CommandManagerImpl(TaskDao taskDao, TaskHandler taskHandler, CommandAssembly commandAssembly,
			OutHandlerMethod ohm, Integer size) {
		super();
		this.taskDao = taskDao;
		this.taskHandler = taskHandler;
		this.commandAssembly = commandAssembly;
		this.ohm = ohm;
		init(size);
	}

	/**
	 * 初始化，如果几个处理器未注入，则使用默认处理器
	 * 
	 * @param size
	 */
	public void init(Integer size) {
		if (config == null) {
			CommonUtil.logger(this.getClass()).error("配置文件加载失败！配置文件不存在或配置错误");
			return;
		}

		if (this.ohm == null) {
			this.ohm = new DefaultOutHandlerMethod();
		}


		if (this.taskHandler == null) {
			this.taskHandler = new TaskHandlerImpl(this.ohm);
		}

		if (this.commandAssembly == null) {
			this.commandAssembly = new CommandAssemblyImpl();
		}

	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setTaskHandler(TaskHandler taskHandler) {
		this.taskHandler = taskHandler;
	}

	public void setCommandAssembly(CommandAssembly commandAssembly) {
		this.commandAssembly = commandAssembly;
	}

	public void setOhm(OutHandlerMethod ohm) {
		this.ohm = ohm;
	}

	/**
	 * 是否已经初始化
	 * 
	 * @param 如果未初始化时是否初始化
	 * @return
	 */
	public boolean isInit(boolean b) {
		boolean ret = this.ohm == null || this.taskDao == null || this.taskHandler == null
				|| this.commandAssembly == null;
		if (ret && b) {
			init(null);
		}
		return ret;
	}

	@Override
	public String start(String id, String command) {
		return start(id, command, false);
	}

	@Override
	public String start(String id, String command, boolean hasPath) {
		if (isInit(true)) {
			CommonUtil.logger(this.getClass()).error("执行失败，未进行初始化或初始化失败！");
			return null;
		}
		if (id != null && command != null) {
			CommandTasker tasker = taskHandler.process(id, hasPath ? command : config.getPath() + command);
			if (tasker != null) {
				int ret = taskDao.add(tasker);
				if (ret > 0) {
					return tasker.getId();
				} else {
					// 持久化信息失败，停止处理
					taskHandler.stop(ProcessUtil.getProcessId(tasker.getProcess()), tasker.getThread());
					if (config.isDebug())
						logger.error("持久化失败，停止任务！");
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public String start(Map<String, String> assembly) {
		// ffmpeg环境是否配置正确
		if (checkConfig()) {
			logger.error("配置未正确加载，无法执行");
			return null;
		}
		// 参数是否符合要求
		if (assembly == null || assembly.isEmpty() || !assembly.containsKey("appName")) {
			logger.error("参数不正确，无法执行");
			return null;
		}
		String appName = (String) assembly.get("appName");
		if (appName != null && "".equals(appName.trim())) {
			logger.error("appName不能为空");
			return null;
		}
		assembly.put("ffmpegPath", config.getPath() + "ffmpeg");
		String command = commandAssembly.assembly(assembly);
		if (command != null) {
			return start(appName, command, true);
		}

		return null;
	}

	@Override
	public String start(String id, CommandBuidler commandBuidler) {
		// ffmpeg环境是否配置正确
		if (checkConfig()) {
			CommonUtil.logger(this.getClass()).error("配置未正确加载，无法执行");
			return null;
		}
		String command = commandBuidler.get();
		if (command != null) {
			return start(id, command, true);
		}
		return null;
	}

	private boolean checkConfig() {
		return config == null;
	}

	@Override
	public boolean stop(String id) {
		if (id != null && taskDao.isHave(id)) {
			if (config.isDebug())
				CommonUtil.logger(this.getClass()).info("正在停止任务：" + id);
			String string = taskDao.get(id);
			CommonUtil.logger(this.getClass()).info("正在停止任务：" + string);
			String[] value = string.split(",");
			CommonUtil.logger(this.getClass()).info("正在停止任务：" + value);
			if (taskHandler.stop(value[1], ProcessUtil.findThread(Long.parseLong(value[2])))) {
				taskDao.remove(id);
				return true;
			}
		}
		CommonUtil.logger(this.getClass()).info("正在停止任务：" + id);
		return false;
	}

	@Override
	public int stopAll() {
		Map<Object, Object> map = taskDao.getAll();
		int index = 0;
		for (Entry<Object, Object> entry : map.entrySet()) {

			String[] values = entry.getValue().toString().split(",");
			if (taskHandler.stop(values[1],ProcessUtil.findThread(Long.parseLong(values[2])))) {
				taskDao.remove(values[0]);
				index++;
			} 
		}




		if (config.isDebug())
			logger.info("停止了" + index + "个任务！");
		return index;
	}

	@Override
	public String query(String id) {
		return taskDao.get(id);
	}



	@Override
	public void destory() {
		if (keepAliveHandler != null) {
			// 安全停止保活线程
			keepAliveHandler.interrupt();
		}
	}


}
