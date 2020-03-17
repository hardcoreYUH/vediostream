package com.yuhang.vediostream.m3u8.service;

import java.util.List;

import com.yuhang.vediostream.base.util.Page;
import com.yuhang.vediostream.m3u8.model.Camera;

public interface CameraService {

	List<Camera> queryCameraList(Page page, Long greenHousrId);

	Camera startPlugFlowForHIK(Long id);
	
	Camera queryCameraMessageById(Long id);

	String stopPlugFlowForHIK(Long id);

	String insertHIkDevice(Camera camera);

	String updateHIkDevice(Camera camera);

	String daleteHIkDevice(Long id);

	String stopAllPlugFlowForHIK();

	Camera cutOutHIKPicture(Long id);


}
