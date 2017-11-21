package top.yuyufeng.learn.lucene.demo1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * @author yuyufeng
 * @date 2017/11/21
 */
public class LuceneUpdateDemo {
    public static void main(String[] args) {
        // 实例化IKAnalyzer分词器
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);

        Directory directory = null;
        IndexWriter iwriter;
        try {
            // 索引目录
            directory = new SimpleFSDirectory(new File("D://test/lucene_index"));

            // 配置IndexWriterConfig
            IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory, iwConfig);
            // 写入索引
            Document doc = new Document();
            String id = "1511235711323";
            doc.add(new StringField("ID", id, Field.Store.YES));
            doc.add(new TextField("blog", "更新文档后->达摩院一定也必须要超越英特尔，必须超越微软，必须超越IBM，因为我们生于二十一世纪，我们是有机会后发优势的。", Field.Store.YES));
            //先根据Term ID 删除，在建立新的索引
            iwriter.updateDocument(new Term("ID", id), doc);
            iwriter.close();
            System.out.println("更新索引成功:" + 1511233039462L);
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
 * DeleteDocuments(Query query):根据Query条件来删除单个或多个Document
 * DeleteDocuments(Query[] queries):根据Query条件来删除单个或多个Document
 * DeleteDocuments(Term term):根据Term来删除单个或多个Document
 * DeleteDocuments(Term[] terms):根据Term来删除单个或多个Document
 * DeleteAll():删除所有的Document
 */