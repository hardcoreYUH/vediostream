package com.yuhang.vediostream.m3u8.mapper.reader;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yuhang.vediostream.base.util.Page;
import com.yuhang.vediostream.m3u8.model.Camera;

@Mapper
public interface CameraRdMapper {

	/**
	 * 获取设备数量
	 * @param greenHousrId
	 * @return
	 */
	@Select("<script> select count(id) as count from t_base_camera t where  t.valid = 1 "
			+ "<if test=\"greenHousrId!=null and greenHousrId!=''\"> and t.greenhouse_id=#{greenHousrId}</if>  </script> ")
	Map<String, Object> queryCameraCount(@Param("greenHousrId")Long greenHousrId);

	/**
	 * 分页获取设备列表
	 * @param page
	 * @param greenHousrId
	 * @return
	 */
	@Select("<script>SELECT t.id as id , t.`name` as name ,t.rtsp_url as rtspUrl,"
			+ " t.tagert_url as tagertUrl,t.status ,t.image_url as imageUrl "
			+ " FROM t_base_camera t WHERE t.valid = 1 "
			+ "<if test=\"greenHousrId!=null and greenHousrId!=''\"> and t.greenhouse_id=#{greenHousrId}</if> "
			+ " order by t.create_time desc"
			+ " limit #{page.info}, #{page.pageSize}  </script> ")
	List<Camera> queryCameraList(@Param("page")Page page, @Param("greenHousrId")Long greenHousrId);

	/**
	 * 获取设备详情
	 * @param id
	 * @return
	 */
	@Select("SELECT t.id as id , t.`name` as name ,t.rtsp_url as rtspUrl,"
			+ " t.tagert_url as tagertUrl,t.status ,t.image_url as imageUrl,"
			+ " alias_en as aliasEn , t.create_time as createTime,t.is_screencap as isScreencap  "
			+ " FROM t_base_camera t WHERE t.valid = 1 AND t.id = #{id}")
	Camera queryCameraMessageById(@Param("id")Long id);

}
