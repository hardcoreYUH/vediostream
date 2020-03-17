package com.yuhang.vediostream.m3u8.mapper.writer;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.yuhang.vediostream.m3u8.model.Camera;

@Mapper
public interface CameraWrMapper {

	/**
	 * 修改设备状态
	 * @param id
	 * @param status 
	 */
	@Update("<script> " +
			" update t_base_camera " +
			" set UPDATE_TIME=now() ,version=version + 1 , status = #{status} " +
			" where id = #{id}" +
			" </script> ")
	void updateStatusById(@Param("id") Long id, @Param("status") Integer status);

	/**
	 * 新增设备
	 * @param camera
	 */
	@Insert("insert into t_base_camera(id,name,rtsp_url,tagert_url,greenhouse_id,status,create_time,valid,image_url,alias_en,version) "
			+ "values(#{camera.id},#{camera.name},#{camera.rtspUrl},#{camera.tagertUrl},#{camera.greenhouseId},1,now(),1,#{camera.imageUrl},#{camera.aliasEn},1)")
	void insertHIkDevice(@Param("camera") Camera camera);

	/**
	 * 修改设备
	 * @param camera
	 */
	@Update("<script> " +
			" update t_base_camera " +
			" set UPDATE_TIME = now() ,version = version + 1" +
			" <if test=\"dto.name!=null and dto.name!=''\">,name=#{dto.name}</if> " +
			" <if test=\"dto.rtspUrl!=null and dto.rtspUrl!=''\">,rtsp_url=#{dto.rtspUrl}</if> " +
			" <if test=\"dto.greenhouseId!=null and dto.greenhouseId!=''\">,greenhouse_id=#{dto.greenhouseId}</if> " +
			" <if test=\"dto.imageUrl!=null and dto.imageUrl!=''\">,image_url=#{dto.imageUrl}</if> " +
			" <if test=\"dto.aliasEn!=null and dto.aliasEn!=''\">,alias_en=#{dto.aliasEn}</if> " +
			" where id = #{dto.id}"+
			"</script> ")
	void updateHIkDevice(@Param("dto")Camera camera);

	/**
	 * 删除设备
	 * @param id
	 */
	@Update("<script> " +
			" update t_base_camera " +
			" set UPDATE_TIME=now() ,version=version + 1 , valid = 2 " +
			" where id = #{id}" +
			" </script> ")
	void daleteHIkDevice(@Param("id")  Long id);
	
	/**
	 * 全部停止
	 */
	@Update("<script> " +
			" update t_base_camera " +
			" set UPDATE_TIME=now() ,version=version + 1 , status = 2  where status = 1 " +
			" </script> ")
	void updateStatus();

}
