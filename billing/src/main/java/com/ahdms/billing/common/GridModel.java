package com.ahdms.billing.common;

import java.io.Serializable;
import java.util.List;

public class GridModel<T> implements Serializable{

	private static final long serialVersionUID = -5076963707157716131L;

	private Integer page;  //当前页

	private Integer records; //总记录数

	private Integer total;  //总页数

	private List<T> rows;

	public GridModel() {

	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRecords() {
		return records;
	}

	public void setRecords(Integer records) {
		this.records = records;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}

