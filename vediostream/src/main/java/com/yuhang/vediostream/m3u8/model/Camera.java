package com.yuhang.vediostream.m3u8.model;

import com.yuhang.vediostream.base.BaseModel;

public class Camera extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name; //摄像头名称
	private String rtspUrl ;//摄像头rtsp地址
	private String tagertUrl; //m3u8文件存放地址
	private Long greenhouseId; //大棚id
	private Integer status; //播放状态
	private String imageUrl;//封面id
	private String aliasEn; //别名
	private Integer isScreencap;//是否开启录屏
	
	
	public Integer getIsScreencap() {
		return isScreencap;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setIsScreencap(Integer isScreencap) {
		this.isScreencap = isScreencap;
	}
	public String getAliasEn() {
		return aliasEn;
	}
	public void setAliasEn(String aliasEn) {
		this.aliasEn = aliasEn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRtspUrl() {
		return rtspUrl;
	}
	public void setRtspUrl(String rtspUrl) {
		this.rtspUrl = rtspUrl;
	}
	public String getTagertUrl() {
		return tagertUrl;
	}
	public void setTagertUrl(String tagertUrl) {
		this.tagertUrl = tagertUrl;
	}
	public Long getGreenhouseId() {
		return greenhouseId;
	}
	public void setGreenhouseId(Long greenhouseId) {
		this.greenhouseId = greenhouseId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	

}
