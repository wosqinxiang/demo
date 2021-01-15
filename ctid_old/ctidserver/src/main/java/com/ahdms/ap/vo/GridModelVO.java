package com.ahdms.ap.vo;

import com.ahdms.ap.common.GridModel;

public class GridModelVO<T> extends GridModel<T> {
	
	private int isRegistCount; //已登记人数
	
	private int noRegistCount;  //未登记人数

	public int getIsRegistCount() {
		return isRegistCount;
	}

	public void setIsRegistCount(int isRegistCount) {
		this.isRegistCount = isRegistCount;
	}

	public int getNoRegistCount() {
		return noRegistCount;
	}

	public void setNoRegistCount(int noRegistCount) {
		this.noRegistCount = noRegistCount;
	}
	
}
