/**
 * www.bplow.com
 */
package com.bplow.search.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.bplow.search.domain.SrContent;

/**
 * @desc 
 * @author wangxiaolei
 * @date 2016年10月29日 下午5:06:00
 */
@ContextConfiguration(locations = { "/applicationContext.xml","/applicationContext-import.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class ContentServiceTest {
	
	private Logger log = LoggerFactory.getLogger(ContentServiceTest.class);
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ContentAnalyzerService contentAnalyzerService;
	
	@Test
	public void testQuery() throws Exception{
		SrContent content = new SrContent();
		
		List<SrContent> list = contentService.querySrContent(content);
		
		for(SrContent cnt :list ){
			String html = IOUtils.toString(cnt.getContent());
			String cntinfo = contentAnalyzerService.getHtmlContent(cnt.getContent());
			log.info("html内容:{}",cntinfo);
		}
		
		log.info("列表大小:{}",list.size());
	}

}
