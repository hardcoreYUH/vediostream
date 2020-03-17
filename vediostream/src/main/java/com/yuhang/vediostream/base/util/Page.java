package com.yuhang.vediostream.base.util;

public class Page {
	
	private Integer pageSize;//每页显示多少数据
	private Integer rowNum; //总共多少数据
	private Integer pageNum=1;//当前页
	private Integer lastPage;//最后一页
	
	private Integer info;//起始索引
	
	/**
	 * @param pageNum 当前页
	 * @param pageSize 每页显示多少数据
	 */
	public Page(Integer pageNum,Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize=pageSize;
		this.info=(pageNum-1)*pageSize;
	}
	
	public static void main(String[] args) {
		Page pa = new Page(3, 2);
		pa.setRowNum(10);
		System.out.println("分页："+pa.getInfo()+","+pa.getPageSize());
		System.out.println("最后一页："+pa.getLastPage());
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getRowNum() {
		return rowNum;
	}
	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
		this.lastPage=(int) Math.ceil((double)rowNum/(double)pageSize);
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.info=(pageNum-1)*pageSize;
		this.pageNum = pageNum;
	}
 
	public Integer getInfo() {
		return info;
	}
 
	public void setInfo(Integer info) {
		this.info = info;
	}
 
	public Integer getLastPage() {
		return lastPage;
	}
 
	public void setLastPage(Integer lastPage) {
		this.lastPage = lastPage;
	}
	
}
