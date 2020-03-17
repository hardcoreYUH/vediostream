package com.yuhang.commandManager.data;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuhang.commandManager.util.ProcessUtil;
import com.yuhang.commandManager.util.RedisUtil;

/**
 * 任务存放缓存
 * 
 * 
 */
@Service
public class TaskDaoImpl implements TaskDao {
	// 存放任务信息
	@Autowired
	private RedisUtil map;
	
	private String key = "HIK";
	


	@Override
	public String get(String id) {
		Object hget = map.hget(key,id);
		return hget.toString();
	}



	@Override
	public int add(CommandTasker CommandTasker) {
		String id = CommandTasker.getId();
		if (id != null ) {
			
			String processId = ProcessUtil.getProcessId(CommandTasker.getProcess());
			map.hset(key, id, id + "," + processId + "," + CommandTasker.getThread().getId() + "," + CommandTasker.getCommand());
			if(map.hget(key,id)!=null)
			{
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int remove(String id) {
		if(map.hdel(key, id) != null){
			return 1;
		};
		return 0;
	}

	@Override
	public int removeAll() {
		try {
			map.hdel(key);
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	@Override
	public boolean isHave(String id) {
		return map.hHasKey(key, id);
	}


	//
	@Override
	public Map<Object,Object> getAll() {
		Map<Object, Object> hmGet = map.hmget(key);
		
		return hmGet;
	}

}
