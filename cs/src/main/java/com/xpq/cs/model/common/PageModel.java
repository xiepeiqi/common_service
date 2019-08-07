package com.xpq.cs.model.common;

import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页数据参数模型
 * @author xiepeiqi @date 2019年3月4日
 */
@ApiModel(value="分页数据参数模型")
@Data
public class PageModel implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 默认当前页，1
	 */
	private static final int DEFAULT_PAGE=1;
	/**
	 * 默认分页大小，10
	 */
	private static final int DEFAULT_PAGESIZE=10;

	/**
	 * 当前页，默认1
	 */
	@ApiModelProperty(value="当前页",required=false)
	@Range(min=DEFAULT_PAGE,message="当前页不能小于1")
	private Integer page;
	
	/**
	 * 分页大小，默认10
	 */
	@ApiModelProperty(value="分页大小",required=false)
	@Range(min=DEFAULT_PAGESIZE,message="分页大小不能小于10")
	private Integer pageSize;
	
	public Integer getPage() {
		return page!=null?page:DEFAULT_PAGE;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize!=null?pageSize:DEFAULT_PAGESIZE;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "PageModel [page=" + page + ", pageSize=" + pageSize + "]";
	}
	

}
