package com.yuhang.vediostream.m3u8;
import com.yuhang.commandManager.CommandManager;
import com.yuhang.commandManager.CommandManagerImpl;
import com.yuhang.commandManager.commandbuidler.CommandBuidlerFactory;


/**
 * 
 * @author yuhang
 * @date 2019/12/11
 *
 */

public class RtspToM3u8 {
	


	/**
	 * ./ffmpeg -rtsp_transport tcp -i rtsp://admin:hw123456@39.155.180.130:1554/h264/ch35/main/av_stream  -an
	 *  -c copy -f hls -hls_time 4  -hls_list_size 10 -hls_wrap 5 /usr/local/nginx/html/hls/test.m3u8
	 *  
	 *  
	 *  ffmpeg  -i rtsp://admin:hw123456@39.155.180.130:1554/h264/ch35/main/av_stream 
	 *      -f hls - /usr/local/nginx/html/hls/test.m3u8
	 *  
	 *  hls_time seconds
	 *  	Set the segment length in seconds. Default value is 2.
	 *  
	 *  hls_list_size size
	 *  	Set the maximum number of playlist entries. If set to 0 the list file will contain all the segments. Default value is 5.
	 *  
	 *  hls_wrap wrap
	 *  	Set the number after which the segment filename number (the number specified in each segment file) wraps.
	 *  	If set to 0 the number will be never wrapped. Default value is 0.
	 *  	This option is useful to avoid to fill the disk with many segment files, and limits the maximum number of segment files written to disk towrap.
	 *  
	 * @param args
	 */
	public static void main(String[] args) {
		CommandManager manager=new CommandManagerImpl(10);
		//组装命令
		String start = manager.start("test1", CommandBuidlerFactory.createBuidler()
				.add("ffmpeg")
				.add("-rtsp_transport","tcp")
				.add("-i","rtsp://admin:hw123456@39.155.180.130:1554/h264/ch33/main/av_stream")
				.add("-c","copy")
				.add("-an")
				.add("-f","hls")
				.add("-hls_time","4")
				.add("-hls_list_size","10")
				.add("-hls_wrap","5")
				.add("http://sy.gengzhiyun.com:15080/123/123.m3u8"));
		//执行任务，id就是appName，如果执行失败返回为null

		//CommandTasker info = manager.query(start);
		//System.out.println(info);
	}



}
