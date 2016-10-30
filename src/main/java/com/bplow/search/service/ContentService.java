/**
 * www.bplow.com
 */
package com.bplow.search.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bplow.search.common.DateHelper;
import com.bplow.search.domain.SearchBo;
import com.bplow.search.domain.SrContent;
import com.bplow.search.mapper.SrContentMapper;

/**
 * @desc 
 * @author wangxiaolei
 * @date 2016年10月29日 下午3:28:56
 */
@Service
public class ContentService {
	
	private Logger log = LoggerFactory.getLogger(ContentService.class);
	
	@Autowired
	private SrContentMapper srContentMapper;
	
	@Autowired
	private Search search;
	
	@Autowired
	private ContentAnalyzerService contentAnalyzerService;
	
	
	public void addContent(SrContent content) throws Exception{
		
		content.setId(UUID.randomUUID().toString().replace("-", ""));
		content.setGmtModify(new Date());
		srContentMapper.insert(content);
		
		SearchBo bo = new SearchBo();
		bo.setId(content.getId());
		bo.setName(content.getCntCaption());
		bo.setCnt(contentAnalyzerService.getHtmlContent(content.getContent()));
		
		log.info("添加内容到索引中:{}",bo.getId());
		search.addDocToIndex(bo);
		
	}
	
	public List<SrContent> querySrContent(SrContent content){
		
		List <SrContent> list = srContentMapper.queryForPage(content);
		
		return list;
	}

}
