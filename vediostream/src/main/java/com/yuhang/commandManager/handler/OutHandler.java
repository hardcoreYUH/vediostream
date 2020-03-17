package com.yuhang.commandManager.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.yuhang.commandManager.CommandManager;
import com.yuhang.commandManager.util.CommonUtil;

/**
 * 任务消息输出处理器
 *
 */
public class OutHandler extends Thread {
	/** 控制状态 */
	private volatile boolean desstatus = true;

	/** 读取输出流 */
	private BufferedReader br = null;

	/** 任务ID */
	private String id = null;

	/** 消息处理方法 */
	private OutHandlerMethod ohm;
	
	
	




	public OutHandler() {
		super();
	}


	public boolean isDesstatus() {
		return desstatus;
	}


	public void setDesstatus(boolean desstatus) {
		this.desstatus = desstatus;
	}


	public BufferedReader getBr() {
		return br;
	}


	public void setBr(BufferedReader br) {
		this.br = br;
	}


	public void setId(String id) {
		this.id = id;
	}


	public OutHandlerMethod getOhm() {
		return ohm;
	}


	public void setOhm(OutHandlerMethod ohm) {
		this.ohm = ohm;
	}


	/**
	 * 创建输出线程（默认立即开启线程）
	 * 
	 * @param is
	 * @param id
	 * @param ohm
	 * @return
	 */
	public static OutHandler create(InputStream is, String id, OutHandlerMethod ohm) {
		return create(is, id, ohm, true);
	}

	
	/**
	 * 创建输出线程
	 * 
	 * @param is
	 * @param id
	 * @param ohm
	 * @param start-是否立即开启线程
	 * @return
	 */
	public static OutHandler create(InputStream is, String id, OutHandlerMethod ohm, boolean start) {
		OutHandler out = new OutHandler(is, id, ohm);
		if (start)
			out.start();
		return out;
	}

	public OutHandler(InputStream is, String id, OutHandlerMethod ohm) {
		br = new BufferedReader(new InputStreamReader(is));
		this.id = id;
		this.ohm = ohm;
	}

	/**
	 * 重写线程销毁方法，安全的关闭线程
	 */
	@Override
	public void destroy() {
		setDesstatus(false);
	}

	/**
	 * 执行输出线程
	 */
	@Override
	public void run() {
		String msg = null;
		try {
			if (CommandManager.config.isDebug()) {
				CommonUtil.logger(this.getClass()).info(id + "开始推流！");
			}
			while (desstatus) {
				msg = br.readLine();
				// 输出停止 重启服务
				if (msg == null) {
					KeepAliveHandler.add(id);
				} else {
					ohm.parse(id, msg);
					if (ohm.isbroken()) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						KeepAliveHandler.add(id);
					}
				}
			}
		} catch (IOException e) {
			CommonUtil.logger(this.getClass()).error("发生内部异常错误，自动关闭[" + this.getId() + "]线程");
			destroy();
		} finally {
			if (this.isAlive()) {
				destroy();
			}
		}
	}	

}
