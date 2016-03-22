package com.bplow.search.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 
 * 
 * @author wangxiaolei
 * @version $Id: Search.java, v 0.1 2016年3月18日 上午10:10:14 wangxiaolei Exp $
 */
@Service
public class Search {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public void search() throws Exception {
        //Analyzer analyzer = new StandardAnalyzer();
        SmartChineseAnalyzer cnanalyzer = new SmartChineseAnalyzer();

        // Store the index in memory:
        //Directory directory = new RAMDirectory();
        // To store an index on disk, use this instead:
        //File file = new File("D:/www/index");
        Path path = FileSystems.getDefault().getPath("D:/www/index");
        Directory directory = FSDirectory.open(path);
        IndexWriterConfig config = new IndexWriterConfig(cnanalyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        Document doc = new Document();
        String text = "This is the text to be indexed.我爱周星星，喜欢打篮球";
        doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
        doc.add(new Field("fieldname", "打篮球撒范德萨发生大佛山大沙发的飞洒地方多撒啊啊啊啊啊是范德萨范德萨发大厦大佛山大法师打发斯蒂芬", TextField.TYPE_STORED));
        doc.add(new Field("fieldname","abc dfsfsad撒的发反反复复反复反复反复发顺丰的顶顶顶顶顶的说法都是的发生发的啥地方",TextField.TYPE_STORED));
        iwriter.addDocument(doc);
        iwriter.close();

        // Now search the index:
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser("fieldname", cnanalyzer);
        Query query = parser.parse("篮球");
        ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
        //Assert.assertEquals(1, hits.length);
        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            log.info(hitDoc.get("fieldname"));
            //Assert.assertEquals("This is the text to be indexed.", hitDoc.get("fieldname"));
            
        }
        ireader.close();
        directory.close();
        
    }
    
    
    public void addDocToIndex(Document doc) throws IOException{
        SmartChineseAnalyzer cnanalyzer = new SmartChineseAnalyzer();
        Path path = FileSystems.getDefault().getPath("D:/www/index");
        Directory directory = FSDirectory.open(path);
        IndexWriterConfig config = new IndexWriterConfig(cnanalyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        
        iwriter.addDocument(doc);
        iwriter.close();
    }
    
    public ScoreDoc[] search(String querystr) throws IOException, ParseException{
        SmartChineseAnalyzer cnanalyzer = new SmartChineseAnalyzer();
        Path path = FileSystems.getDefault().getPath("D:/www/index");
        Directory directory = FSDirectory.open(path);
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        
        QueryParser parser = new QueryParser("fieldname", cnanalyzer);
        Query query = parser.parse("篮球");
        ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
        return hits;
    }

}
