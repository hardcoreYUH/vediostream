package com.yuhang.vediostream.m3u8.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.yuhang.vediostream.base.util.ApiConstant;
import com.yuhang.vediostream.base.util.JsonUtil;
import com.yuhang.vediostream.base.util.Page;
import com.yuhang.vediostream.m3u8.model.Camera;
import com.yuhang.vediostream.m3u8.service.CameraService;

@RestController
@RequestMapping("/camera")
public class CameraController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private CameraService cs;

	/**
	 * 查询摄像头信息
	 * 
	 * @param request
	 * @param paramsJson
	 * @return
	 */
	@RequestMapping("/queryCameraList")
	public String queryCameraList(HttpServletRequest request, Integer pageSize, Integer rows, Long greenHousrId) {
		// 创建结果集
		logger.info(" 查询所有摄像头: 当前为第" + pageSize + "页，每页展示" + rows + "条");
		Map<String, Object> result = null;
		try {
			if (pageSize == null) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "缺少当前页"));
			}
			if (rows == null) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "缺少每页数量"));
			}
			Page page = new Page(pageSize, rows);
			List<Camera> list = cs.queryCameraList(page, greenHousrId);
			result = ApiConstant.resp(ApiConstant.API_SUCC);
			result.put("page", page);
			result.put("data", list);
		} catch (Exception e) {
			logger.error(getClass() + ".queryCameraList", e.getMessage());
			logger.error(e.getMessage());
			if (result == null)
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_ERROR));
		}

		return JsonUtil.toJson(result);
	}

	/**
	 * 开始推流
	 * 
	 * @param request
	 * @param paramsJson
	 * @return
	 */
	@RequestMapping("/startPlugFlowForHIK")
	public String startPlugFlowForHIK(HttpServletRequest request, Long id) {
		// 创建结果集
		logger.info("进入推流接口: id = " + id);
		Map<String, Object> result = null;
		try {
			if (id == null) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "缺少摄像头id"));
			}

			Camera camera = cs.startPlugFlowForHIK(id);
			result = ApiConstant.resp(ApiConstant.API_SUCC);
			result.put("data", camera);
		} catch (Exception e) {
			logger.error(getClass() + ".startPlugFlowForHIK", e.getMessage());
			logger.error(e.getMessage());
			if (e.getMessage() != null && e.getMessage().equals("-1")) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "该设备正在工作，请勿重复 操作 "));
			}
			if (e.getMessage() != null && e.getMessage().equals("-2")) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "没有该设备，请核实后发起"));
			}
			if (e.getMessage() != null && e.getMessage().equals("-3")) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "任务开启失败"));
			}
			if (result == null)
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_ERROR));
		}
		
		return JsonUtil.toJson(result);
	}

	/**
	 * 停止推流
	 * 
	 * @param request
	 * @param paramsJson
	 * @return
	 */
	@RequestMapping("/stopPlugFlowForHIK")
	public String stopPlugFlowForHIK(HttpServletRequest request, Long id) {
		// 创建结果集
		logger.info("进入停止推流接口: id = " + id);
		Map<String, Object> result = null;
		try {
			if (id == null) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "缺少摄像头id"));
			}
			String name = cs.stopPlugFlowForHIK(id);
			result = ApiConstant.resp(ApiConstant.API_SUCC);
			result.put("data", name);
		} catch (Exception e) {
			logger.error(getClass() + ".stopPlugFlowForHIK", e.getMessage());
			logger.error(e.getMessage());
			if (e.getMessage() != null && e.getMessage().equals("-1")) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "该设备已经停止，请勿重复 操作 "));
			}
			if (e.getMessage() != null && e.getMessage().equals("-2")) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "没有该设备，请核实后发起"));
			}
			if (result == null)
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_ERROR));
		}

		return JsonUtil.toJson(result);
	}
	/**
	 * 停止所有任务
	 * 
	 * @param request
	 * @param paramsJson
	 * @return
	 */
	@RequestMapping("/stopAllPlugFlowForHIK")
	public String stopAllPlugFlowForHIK(HttpServletRequest request) {
		// 创建结果集
		logger.info("进入停止所有任务");
		Map<String, Object> result = null;
		try {
	
			String name = cs.stopAllPlugFlowForHIK();
			result = ApiConstant.resp(ApiConstant.API_SUCC);
			result.put("data", name);
		} catch (Exception e) {
			logger.error(getClass() + ".stopAllPlugFlowForHIK", e.getMessage());
			logger.error(e.getMessage());
			if (result == null)
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_ERROR));
		}
		
		return JsonUtil.toJson(result);
	}

	/**
	 * 添加新的设备连接
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/insertHIkDevice")
	public String insertHIkDevice(HttpServletRequest request) {
		// 创建结果集
		Map<String, Object> result = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			JSONObject jsonObject = JSONObject.parseObject(sb.toString());
			logger.info("新增设备连接信息:" + jsonObject);
			Camera auction = jsonObject.getJSONObject("self").toJavaObject(Camera.class);
			String name = cs.insertHIkDevice(auction);
			result = ApiConstant.resp(ApiConstant.API_SUCC);
			result.put("data", name);
		} catch (Exception e) {
			logger.error(getClass() + ".insertHIkDevice", e.getMessage());
			logger.error(e.getMessage());

			if (result == null)
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_ERROR));
		}

		return JsonUtil.toJson(result);
	}

	/**
	 * 修改设备连接
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateHIkDevice")
	public String updateHIkDevice(HttpServletRequest request) {
		// 创建结果集
		Map<String, Object> result = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			JSONObject jsonObject = JSONObject.parseObject(sb.toString());
			logger.info("修改设备信息:" + jsonObject);
			Camera auction = jsonObject.getJSONObject("self").toJavaObject(Camera.class);
			String name = cs.updateHIkDevice(auction);
			result = ApiConstant.resp(ApiConstant.API_SUCC);
			result.put("data", name);
		} catch (Exception e) {
			logger.error(getClass() + ".updateHIkDevice", e.getMessage());
			logger.error(e.getMessage());

			if (result == null)
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_ERROR));
		}

		return JsonUtil.toJson(result);
	}

	/**
	 * 删除设备连接
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/daleteHIkDevice")
	public String daleteHIkDevice(HttpServletRequest request, Long id) {
		// 创建结果集
		Map<String, Object> result = null;
		try {
			logger.info("删除设备信息:" + id);
			String name = cs.daleteHIkDevice(id);
			result = ApiConstant.resp(ApiConstant.API_SUCC);
			result.put("data", name);
		} catch (Exception e) {
			logger.error(getClass() + ".daleteHIkDevice", e.getMessage());
			logger.error(e.getMessage());

			if (result == null)
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_ERROR));
		}

		return JsonUtil.toJson(result);
	}

	
	/**
	 * 查询设备连接详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryHIkDeviceById")
	public String queryHIkDeviceById(HttpServletRequest request, Long id) {
		// 创建结果集
		Map<String, Object> result = null;
		try {
			logger.info("查询设备信息:" + id);
			Camera camera = cs.queryCameraMessageById(id);
			result = ApiConstant.resp(ApiConstant.API_SUCC);
			result.put("data", camera);
		} catch (Exception e) {
			logger.error(getClass() + ".queryHIkDeviceById", e.getMessage());
			logger.error(e.getMessage());

			if (result == null)
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_ERROR));
		}

		return JsonUtil.toJson(result);
	}
	
	/**
	 * 海康摄像头每秒图片截取
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/cutOutHIKPicture")
	public String cutOutHIKPicture(HttpServletRequest request, Long id) {
		// 创建结果集
		Map<String, Object> result = null;
		try {
			logger.info("海康摄像头每秒图片截取:" + id);
			Camera camera = cs.cutOutHIKPicture(id);
			result = ApiConstant.resp(ApiConstant.API_SUCC);
			result.put("data", camera);
		} catch (Exception e) {
			logger.error(getClass() + ".cutOutHIKPicture", e.getMessage());
			logger.error(e.getMessage());
			
			if (e.getMessage() != null && e.getMessage().equals("-1")) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "已经开始截取，请勿重复 操作 "));
			}
			if (e.getMessage() != null && e.getMessage().equals("-2")) {
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_FAILED, "没有该设备，请核实后发起"));
			}
			
			if (result == null)
				return JsonUtil.toJson(ApiConstant.resp(ApiConstant.RET_ERROR));
		}
		
		return JsonUtil.toJson(result);
	}

}
