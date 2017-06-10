/**
 * www.bplow.com
 */
package com.bplow.search.page;

import java.util.List;

import com.bplow.search.domain.SearchBo;

/**
 * @desc 
 * @author wangxiaolei
 * @date 2016年10月20日 下午9:23:54
 */
public class Page {
	
	private int pageSize = 10;
	
	private int pageNo = 1;
	
	private int totals;
	
	private int allPages;
	
	private List<SearchBo> data;
	
	public List<SearchBo> getData() {
		return data;
	}

	public void setData(List<SearchBo> data) {
		this.data = data;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotals() {
		return totals;
	}

	public void setTotals(int totals) {
		this.totals = totals;
		
		if(totals == 0 || pageSize == 0)
            this.allPages = 0;
        else
        	this.allPages = totals / pageSize + (totals % pageSize <= 0 ? 0 : 1);
		
	}

	public int getAllPages() {
		return allPages;
	}

	public void setAllPages(int allPages) {
		this.allPages = allPages;
	}
	
	public int getShowSize(){
		
		return this.pageNo * this.pageSize;
		
	}

}
