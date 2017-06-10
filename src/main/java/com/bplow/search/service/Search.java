package com.bplow.search.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.bplow.search.domain.SearchBo;
import com.bplow.search.page.Page;

/**
 * 
 * 
 * @author wangxiaolei
 * @version $Id: Search.java, v 0.1 2016年3月18日 上午10:10:14 wangxiaolei Exp $
 */
@Service
public class Search implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	IndexSearcher isearcher = null;

	private IndexWriter iwriter;

	private DirectoryReader ireader;

	SmartChineseAnalyzer cnanalyzer = new SmartChineseAnalyzer();

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${index.path}")
	private String indexPath;
	
	@Autowired
	private ValidatorService validatorService;

	public void afterPropertiesSet() throws Exception {
		Path path = FileSystems.getDefault().getPath(indexPath);
		Directory directory = FSDirectory.open(path);
		IndexWriterConfig config = new IndexWriterConfig(cnanalyzer);
		iwriter = new IndexWriter(directory, config);
		Document doc = new Document();
		doc.add(new Field("id", "1", TextField.TYPE_STORED));
		doc.add(new Field("content", "初始化", TextField.TYPE_STORED));
		iwriter.addDocument(doc);
		iwriter.close();
		ireader = DirectoryReader.open(directory);
		isearcher = new IndexSearcher(ireader);
	}

	public void search(String keyword) throws Exception {
		// Analyzer analyzer = new StandardAnalyzer();
		// Store the index in memory:
		// Directory directory = new RAMDirectory();
		// To store an index on disk, use this instead:
		// File file = new File("D:/www/index");
		/*
		 * Path path = FileSystems.getDefault().getPath(indexPath); Directory
		 * directory = FSDirectory.open(path); IndexWriterConfig config = new
		 * IndexWriterConfig(cnanalyzer); IndexWriter iwriter = new
		 * IndexWriter(directory, config);
		 */
		/*
		 * Document doc = new Document(); String text =
		 * "This is the text to be indexed.我爱周星星，喜欢打篮球"; doc.add(new
		 * Field("fieldname", text, TextField.TYPE_STORED)); doc.add(new
		 * Field("fieldname",
		 * "打篮球撒范德萨发生大佛山大沙发的飞洒地方多撒啊啊啊啊啊是范德萨范德萨发大厦大佛山大法师打发斯蒂芬",
		 * TextField.TYPE_STORED)); doc.add(new
		 * Field("fieldname","abc dfsfsad撒的发反反复复反复反复反复发顺丰的顶顶顶顶顶的说法都是的发生发的啥地方"
		 * ,TextField.TYPE_STORED)); iwriter.addDocument(doc); iwriter.close();
		 */

		// Now search the index:
		// DirectoryReader ireader = DirectoryReader.open(directory);
		// IndexSearcher isearcher = new IndexSearcher(ireader);
		// Parse a simple query that searches for "text":
		QueryParser parser = new QueryParser("content", cnanalyzer);
		Query query = parser.parse(keyword);
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			log.info("{},[{}]", hitDoc.get("name"), hitDoc.get("id"));
		}
		ireader.close();
		// directory.close();
	}

	/**
	 * 加索引
	 * 
	 * @param doc
	 * @throws IOException
	 */
	public void addDocToIndex(Document doc) throws IOException {

		if (!iwriter.isOpen()) {
			Path path = FileSystems.getDefault().getPath(indexPath);
			Directory directory = FSDirectory.open(path);
			IndexWriterConfig config = new IndexWriterConfig(cnanalyzer);
			iwriter = new IndexWriter(directory, config);
		}

		iwriter.addDocument(doc);
		iwriter.close();
	}

	public void addDocToIndex(SearchBo bo) throws IOException {
		
		if(validatorService.validator(bo)){
			return ;
		}

		Document doc = new Document();

		doc.add(new StringField("id", bo.getId(), Store.YES));
		doc.add(new StringField("url", bo.getUrl(), Store.YES));
		doc.add(new Field("name", bo.getName(), TextField.TYPE_STORED));
		doc.add(new Field("content", bo.getCnt(), TextField.TYPE_STORED));
		doc.add(new IntField("gmtCreate",new Long(new Date().getTime()).intValue(),
				IntField.TYPE_STORED));

		addDocToIndex(doc);

	}
	
	public void delDocById(String id) throws IOException{
		
		if(StringUtils.isEmpty(id)){
			return ;
		}
		
		if (!iwriter.isOpen()) {
			Path path = FileSystems.getDefault().getPath(indexPath);
			Directory directory = FSDirectory.open(path);
			IndexWriterConfig config = new IndexWriterConfig(cnanalyzer);
			iwriter = new IndexWriter(directory, config);
		}
		
		iwriter.deleteDocuments(new  Term("id",id));
		iwriter.close();
		
	}
	
	//更新文档
	public void updateDoc(SearchBo bo) throws IOException{
		
		delDocById(bo.getId());
		
		addDocToIndex(bo);
		
	}

	/*
	 * 高亮
	 */
	public ScoreDoc[] search(String fieldName, String querystr)
			throws IOException, ParseException, InvalidTokenOffsetsException {
		Long starttime = System.currentTimeMillis();

		QueryParser parser = new QueryParser(fieldName, cnanalyzer);
		Query query = parser.parse(querystr);

		Formatter formatter = new SimpleHTMLFormatter(
				"<span class=\"highlighter\">", "</span>");
		Scorer fragmentScorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
		Fragmenter fragmenter = new SimpleFragmenter(100);// 高亮范围
		highlighter.setTextFragmenter(fragmenter);

		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		log.info("耗时:{}", System.currentTimeMillis() - starttime);
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);

			// 如果当前属性值中没有出现关键字,则返回null
			String hctemp = highlighter.getBestFragment(cnanalyzer, "name",
					hitDoc.get("name"));
			if (null != hctemp) {
				log.info("高亮:{}", hctemp);
			}

			log.info(hitDoc.get("name"));
			log.info("id:{}", hitDoc.get("id"));
		}
		return hits;
	}

	/**
	 * 获取高亮关键字
	 * 
	 * @param query
	 * @param num
	 * @param scoreDoc
	 * @param fieldName
	 * @return
	 * @throws IOException
	 * @throws InvalidTokenOffsetsException
	 */
	public String getHightLighterTxt(Query query, int num, ScoreDoc[] scoreDoc,
			String fieldName)

	throws IOException, InvalidTokenOffsetsException {
		Formatter formatter = new SimpleHTMLFormatter(
				"<span class=\"highlighter\">", "</span>");
		Scorer fragmentScorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
		Fragmenter fragmenter = new SimpleFragmenter(100);// 高亮范围
		highlighter.setTextFragmenter(fragmenter);

		Document hitDoc = isearcher.doc(scoreDoc[num].doc);
		String hctemp = highlighter.getBestFragment(cnanalyzer, fieldName,
				hitDoc.get(fieldName));

		return hctemp;
	}

	/**
	 * 分页
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public void searchPage(String fieldName, String querystr)
			throws IOException, ParseException {
		QueryParser parser = new QueryParser(fieldName, cnanalyzer);
		Query query = parser.parse(querystr);

		int pageStart = 0;
		ScoreDoc lastBottom = null;// 相当于pageSize
		while (pageStart < 60) {// 这个只有是paged.scoreDocs.length的倍数加一才有可能翻页操作
			TopDocs paged = null;
			paged = isearcher.searchAfter(lastBottom, query, null, 10);// 查询首次的30条
			if (paged.scoreDocs.length == 0) {
				break;// 如果下一页的命中数为0的情况下，循环自动结束
			}
			page(isearcher, paged);// 分页操作，此步是传到方法里对数据做处理的

			pageStart += paged.scoreDocs.length;// 下一次分页总在上一次分页的基础上
			lastBottom = paged.scoreDocs[paged.scoreDocs.length - 1];// 上一次的总量-1，成为下一次的lastBottom
		}
	}

	/**
	 * 
	 * 分页查询
	 *
	 * @param keyword
	 * @param limit
	 * @param pageNum
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws InvalidTokenOffsetsException
	 */
	public Page searchForPage(String keyword, Page pagetmp) throws IOException,
			ParseException, InvalidTokenOffsetsException {
		String[] fields = { "content", "name" };
		Query query = new MultiFieldQueryParser(fields, cnanalyzer)
				.parse(keyword);

		ScoreDoc afterDoc = new ScoreDoc(0, 0f);

		/*
		 * TopDocs topDocs = isearcher.searchAfter(afterDoc, query,
		 * pagetmp.getShowSize(), Sort.RELEVANCE);
		 */
		TopDocs topDocs = isearcher.search(query, pagetmp.getShowSize(),
				Sort.RELEVANCE);

		logger.info("命中记录数:[{}]", topDocs.totalHits);

		pagetmp.setTotals(topDocs.totalHits);

		Page page = this.getPage(isearcher, topDocs, query, pagetmp);

		return page;
	}

	/**
	 * 获取记录总数
	 * 
	 * @param keywords
	 * @return
	 * @throws Exception
	 */
	public int getCounts(String keywords) throws Exception {

		int docCount = 0;
		QueryParser parser = new QueryParser("name", cnanalyzer);
		Query query = parser.parse(keywords);
		TotalHitCountCollector totalHitCountCollector = new TotalHitCountCollector();
		/*
		 * BooleanQuery q = new BooleanQuery(); q.add(new BooleanClause(new
		 * WildcardQuery(new Term("", String
		 * .valueOf(WildcardQuery.WILDCARD_STRING))),
		 * BooleanClause.Occur.MUST)); q.add(query, BooleanClause.Occur.MUST);
		 */
		isearcher.search(query, totalHitCountCollector);
		docCount = totalHitCountCollector.getTotalHits();

		return docCount;
	}

	public Page getPage(IndexSearcher isearcher, TopDocs paged, Query query,
			Page page) throws IOException, ParseException,
			InvalidTokenOffsetsException {

		ScoreDoc[] hits = paged.scoreDocs;

		List<SearchBo> list = new ArrayList<SearchBo>();
		for (int i = page.getPageSize() * Math.max(page.getPageNo() - 1, 0); i < hits.length; i++) {
			SearchBo bo = new SearchBo();
			Document hitDoc = isearcher.doc(hits[i].doc);
			bo.setId(hitDoc.get("id"));
			bo.setName(this.getHightLighterTxt(query, i, hits, "name"));
			bo.setCnt(this.getHightLighterTxt(query, i, hits, "content"));
			bo.setUrl(hitDoc.get("url"));

			log.info("RECORD:[编号:{},id:{},name:{}]", hits[i].doc,
					hitDoc.get("content"), hitDoc.get("name"));
			list.add(bo);
		}
		page.setData(list);

		return page;
	}

	/* 打印 */
	private void page(IndexSearcher isearcher, TopDocs paged)
			throws IOException {
		ScoreDoc[] hits = paged.scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			log.info("记录[{},{}]", hitDoc.get("id"), hitDoc.get("content"));
		}
	}

	/**
	 * 关联查询 多条件查询
	 * 
	 * @throws ParseException
	 */
	public void multiQuery(String keyword) throws ParseException {
		String[] fields = { "phoneType", "name", "category", "price" };
		Query query = new MultiFieldQueryParser(fields, cnanalyzer)
				.parse(keyword);

	}

	/**
	 * 排序
	 * 
	 * @throws ParseException
	 */
	public void searchOrderBy(String fieldName, String keyword)
			throws ParseException {
		String[] fields = { "content", "name", "category", "price" };
		Query query = new MultiFieldQueryParser(fields, cnanalyzer)
				.parse(keyword);

		Sort sort = new Sort(new SortField("date", Type.INT, true));// true为降序排列
		Sort sort2 = new Sort(new SortField[] {
				new SortField("date", Type.INT, true),
				new SortField("ename", Type.STRING, false) });

	}

	/**
	 * 过滤
	 */

	/**
	 * 结果合并
	 * 
	 */

	/**
	 * 加权
	 */

	/**
	 * 检索结果的评分
	 */

}
