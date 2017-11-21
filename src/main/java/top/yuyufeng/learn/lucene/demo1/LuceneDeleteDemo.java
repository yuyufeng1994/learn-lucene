package top.yuyufeng.learn.lucene.demo1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * @author yuyufeng
 * @date 2017/11/21
 */
public class LuceneDeleteDemo {
    public static void main(String[] args) {
        // Lucene Document的域名
        String fieldName = "blog";
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
        Directory directory = null;
        IndexReader ireader = null;
        IndexSearcher isearcher;
        IndexWriter iwriter = null;
        try {
            //索引目录
            directory = new SimpleFSDirectory(new File("D://test/lucene_index"));
            // 配置IndexWriterConfig
            IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            ireader = DirectoryReader.open(directory);
            iwriter = new IndexWriter(directory, iwConfig);
            iwriter.deleteDocuments(new Term("ID","1511235710648"));
            //使用IndexWriter进行Document删除操作时，文档并不会立即被删除，而是把这个删除动作缓存起来，当IndexWriter.Commit()或IndexWriter.Close()时，删除操作才会被真正执行。
            iwriter.commit();
            iwriter.close();
            ireader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


/**
 DeleteDocuments(Query query):根据Query条件来删除单个或多个Document
 DeleteDocuments(Query[] queries):根据Query条件来删除单个或多个Document
 DeleteDocuments(Term term):根据Term来删除单个或多个Document
 DeleteDocuments(Term[] terms):根据Term来删除单个或多个Document
 DeleteAll():删除所有的Document
 */