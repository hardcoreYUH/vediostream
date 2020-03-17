package com.yuhang.commandManager.data;

import java.util.Map;

/**
 * 任务信息持久层接口
 * 
 */
public interface TaskDao {
	/**
	 * 通过id查询任务信息
	 * @param id - 任务ID
	 * @return CommandTasker -任务实体
	 */
	public String get(String id);
	/**
	 * 查询全部任务信息
	 * @return Collection<CommandTasker>
	 */
	public  Map<Object,Object> getAll();
	/**
	 * 增加任务信息
	 * @param CommandTasker -任务信息实体
	 * @return 增加数量：<1-增加失败，>=1-增加成功
	 */
	public int add(CommandTasker CommandTasker);
	/**
	 * 删除id对应的任务信息
	 * @param id
	 * @return 数量：<1-操作失败，>=1-操作成功
	 */
	public int remove(String id);
	/**
	 * 删除全部任务信息
	 * @return 数量：<1-操作失败，>=1-操作成功
	 */
	public int removeAll();
	/**
	 * 是否存在某个ID
	 * @param id - 任务ID
	 * @return true:存在，false：不存在
	 */
	public boolean isHave(String id);
}
