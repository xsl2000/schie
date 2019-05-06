package org.schic.modules.sys.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.schic.common.persistence.TreeEntity;

/**
 * 
 * @Description: 系统配置Entity
 * @author Caiwb
 * @date 2019年5月6日 上午10:47:23
 */
public class SysConfigTree extends TreeEntity<SysConfigTree> {

	private static final long serialVersionUID = 1L;
	private String type; // 类型
	private String value; // 数据值
	private String label; // 标签名
	private String description; // 描述
	private SysConfigTree parent; // 父级编号
	private String parentIds; // 所有父级编号
	private String picture; // 图片
	private Date beginCreateDate; // 开始 创建时间
	private Date endCreateDate; // 结束 创建时间
	private Date beginUpdateDate; // 开始 更新时间
	private Date endUpdateDate; // 结束 更新时间

	public SysConfigTree() {
		super();
	}

	public SysConfigTree(String id) {
		super(id);
	}

	@Length(min = 1, max = 100, message = "类型长度必须介于 1 和 100 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min = 1, max = 100, message = "数据值长度必须介于 1 和 100 之间")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Length(min = 1, max = 100, message = "标签名长度必须介于 1 和 100 之间")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Length(min = 1, max = 4000, message = "描述长度必须介于 1 和 4000 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//@JsonBackReference
	@Override
	public SysConfigTree getParent() {
		return parent;
	}

	@Override
	public void setParent(SysConfigTree parent) {
		this.parent = parent;
	}

	@Override
	@Length(min = 0, max = 2000, message = "所有父级编号长度必须介于 0 和 2000 之间")
	public String getParentIds() {
		return parentIds;
	}

	@Override
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min = 0, max = 255, message = "图片长度必须介于 0 和 255 之间")
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}

	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public Date getBeginUpdateDate() {
		return beginUpdateDate;
	}

	public void setBeginUpdateDate(Date beginUpdateDate) {
		this.beginUpdateDate = beginUpdateDate;
	}

	public Date getEndUpdateDate() {
		return endUpdateDate;
	}

	public void setEndUpdateDate(Date endUpdateDate) {
		this.endUpdateDate = endUpdateDate;
	}

	@Override
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}