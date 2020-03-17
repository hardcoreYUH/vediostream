package com.yuhang.vediostream.m3u8.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.yuhang.commandManager.CommandManager;
import com.yuhang.commandManager.commandbuidler.CommandBuidlerFactory;
import com.yuhang.commandManager.util.PinYInUtil;
import com.yuhang.vediostream.base.util.BaseException;
import com.yuhang.vediostream.base.util.IDUtils;
import com.yuhang.vediostream.base.util.Page;
import com.yuhang.vediostream.m3u8.mapper.reader.CameraRdMapper;
import com.yuhang.vediostream.m3u8.mapper.writer.CameraWrMapper;
import com.yuhang.vediostream.m3u8.model.Camera;
import com.yuhang.vediostream.m3u8.service.CameraService;

@Service
public class CameraServiceImpl implements CameraService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private CameraRdMapper crm;

	@Resource
	private CameraWrMapper cwm;

	@Resource
	private CommandManager manager;

	private String IPURL = "http://39.97.244.139/hls/";

	/**
	 * 设备列表
	 */
	@Override
	public List<Camera> queryCameraList(Page page, Long greenHousrId) {

		// 获取总数量
		Map<String, Object> count = crm.queryCameraCount(greenHousrId);

		page.setRowNum(Integer.parseInt(count.get("count").toString()));

		return crm.queryCameraList(page, greenHousrId);
	}

	/**
	 * 开始推流
	 */
	@Override
	public Camera startPlugFlowForHIK(Long id) {

		Camera camera = this.queryCameraMessageById(id);
		if (camera == null) {
			throw new BaseException("-2");
		}
		if (camera.getStatus() == 2) {
			throw new BaseException("-1");
		}
		// 拼接视频存放路径
		String tagertUrl = CommandManager.config.getTagertPath() + "/" + id + "/" + id + ".m3u8";

		logger.info("存放视频路径" + tagertUrl);

		// 如果不存在就创建路径
		File fileUrl = new File(CommandManager.config.getTagertPath() + "/" + id);
		if (!fileUrl.exists()) {
			fileUrl.mkdirs();
		}
		// 组装命令					//设备名称
		String start = manager.start(camera.getName(),					//使用tcp协议拉流
				CommandBuidlerFactory.createBuidler().add("ffmpeg").add("-rtsp_transport", "tcp")
				.add("-i", camera.getRtspUrl()).add("-c", "copy").add("-an").add("-f", "hls")
					//每个切片 5秒                            //切片列表数量为10				//10个视频之后进行覆盖
				.add("-hls_time", "5").add("-hls_list_size", "10").add("-hls_wrap" , "10").add(tagertUrl));

		if (start == null) { 
			throw new BaseException("-3");
		}
		cwm.updateStatusById(id, 2);

		return camera;
	}

	/**
	 * 停止推流
	 */
	@Override
	public String stopPlugFlowForHIK(Long id) {

		Camera camera = this.queryCameraMessageById(id);
		if (camera == null) {
			throw new BaseException("-2");
		}
		if (camera.getStatus() == 1) {
			throw new BaseException("-1");
		}

		String i = "停止失败";

		boolean stop = manager.stop(camera.getName());
		if (stop) {
			// 更改状态
			cwm.updateStatusById(id, 1);
			i = "停止成功";
		}
		return i;
	}

	/**
	 * 停止所有任务
	 */
	@Override
	public String stopAllPlugFlowForHIK() {
		int stopAll = manager.stopAll();
		cwm.updateStatus();
		return stopAll + "";
	}

	/**
	 * 获取设备详情
	 */
	@Override
	public Camera queryCameraMessageById(Long id) {
		return crm.queryCameraMessageById(id);
	}

	/**
	 * 新增设备连接
	 */
	@Override
	public String insertHIkDevice(Camera camera) {
		Long genItemId = IDUtils.genItemId();
		camera.setId(genItemId);
		// 播放地址
		String tagertUrl = IPURL + "/" + genItemId + "/" + genItemId + ".m3u8";
		camera.setTagertUrl(tagertUrl);
		camera.setAliasEn(PinYInUtil.toHanyuPinyin(camera.getName()).toUpperCase());
		cwm.insertHIkDevice(camera);
		return genItemId.toString();

	}

	/**
	 * 修改设备信息
	 */
	@Override
	public String updateHIkDevice(Camera camera) {
		camera.setAliasEn(PinYInUtil.toHanyuPinyin(camera.getName()).toUpperCase());
		cwm.updateHIkDevice(camera);
		return null;
	}

	/**
	 * 删除设备信息
	 */
	@Override
	public String daleteHIkDevice(Long id) {
		cwm.daleteHIkDevice(id);
		return null;
	}

	//ffmpeg -i  -y -f image2 -r 1/1 img%03d.jpg
	@Override
	public Camera cutOutHIKPicture(Long id) {

		Camera camera = this.queryCameraMessageById(id);
		if (camera == null) {
			throw new BaseException("-2");
		}
		if (camera.getStatus() == 3) {
			throw new BaseException("-1");
		}

		// 拼接图片存放路径
		String tagertUrl = CommandManager.config.getTagertPath() + "/" + id + "/" + id + "/image/";

		logger.info("存放视频路径" + tagertUrl);

		// 如果不存在就创建路径
		File fileUrl = new File(CommandManager.config.getTagertPath() + "/" + id + "/image");
		if (!fileUrl.exists()) {
			fileUrl.mkdirs();
		}
		// 组装命令
		String start = manager.start(id.toString(),
				CommandBuidlerFactory.createBuidler().add("ffmpeg -re").add("-rtsp_transport", "tcp")
				.add("-i", camera.getRtspUrl()).add("-y ", "-f").add(" image2 -r 1/1").add(tagertUrl+ id + ".mp4"));

		if (start == null) { 
			throw new BaseException("-3");
		}
		cwm.updateStatusById(id, 3);

		return camera;

	}

}
