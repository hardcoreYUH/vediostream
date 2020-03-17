package com.yuhang.commandManager.commandbuidler;

/**
 * 默认流式命令构建器工厂类 采用链式模式
 *
 */
public class CommandBuidlerFactory {

	public static CommandBuidler createBuidler() {
		return new DefaultCommandBuidler();
	};
	
	public static  CommandBuidler createBuidler(String rootpath) {
		return new DefaultCommandBuidler(rootpath);
	};
}
