package com.yuhang.vediostream.base;

import java.io.Serializable;
import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created by liujia 2018/1/17 17:23
 */
public class BaseModel implements Serializable {

    /**
     * 主键ID : ID
     */
	@Field
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人 : CREATE_BY
     */
    private Long createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 修改人 : UPDATE_BY
     */
    private Long updateBy;
    
    /**
     * 有效性
     */
    private Integer valid;
    
    /**
     * 版本号
     */
    private Integer version;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
    
}


