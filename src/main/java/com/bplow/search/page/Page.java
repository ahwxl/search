/**
 * www.bplow.com
 */
package com.bplow.search.page;

import java.util.List;

import org.apache.lucene.search.ScoreDoc;

import com.bplow.search.domain.SearchBo;

/**
 * @desc 
 * @author wangxiaolei
 * @date 2016年10月20日 下午9:23:54
 */
public class Page {
	
	private int limit;
	
	private int pageNum;
	
	private int position;

	private List<SearchBo> data;
	
	private ScoreDoc scoreDoc;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<SearchBo> getData() {
		return data;
	}

	public void setData(List<SearchBo> data) {
		this.data = data;
	}

	public ScoreDoc getScoreDoc() {
		return scoreDoc;
	}

	public void setScoreDoc(ScoreDoc scoreDoc) {
		this.scoreDoc = scoreDoc;
	}
	
}
