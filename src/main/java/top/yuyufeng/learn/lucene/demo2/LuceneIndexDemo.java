package top.yuyufeng.learn.lucene.demo2;

/**
 * @author yuyufeng
 * @date 2017/11/21
 */

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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
public class LuceneIndexDemo {
    public static void main(String[] args) {
        // 建立5条索引
        String content = "10月11日杭州云栖大会上，马云表达了对新建成的阿里巴巴全球研究院—阿里巴巴达摩院的愿景，希望达摩院二十年内成为世界第一大经济体，服务世界二十亿人，创造一亿个工作岗位。";
        Long createTime = System.currentTimeMillis();
        String id = createTime + "";
        int readCount =10;
        float score =9.5f;
        index(content, createTime, id, readCount, score);


        content = "中国互联网界，阿里巴巴被认为是技术实力最弱的公司。我确实不懂技术，承认不懂技术不丢人，不懂装懂才丢人。";
        createTime = System.currentTimeMillis();
        id = createTime + "";
        readCount =3;
        score =9.7f;
        index(content, createTime, id, readCount, score);

        content = "阿里巴巴未来二十年的目标是打造世界第五大经济体，不是我们狂妄，而是世界需要这么一个经济体，也一定会有这么一个经济体。";
        createTime = System.currentTimeMillis();
        id = createTime + "";
        readCount =69;
        score =5.6f;
        index(content, createTime, id, readCount, score);

        content = "达摩院一定也必须要超越英特尔，必须超越微软，必须超越IBM，因为我们生于二十一世纪，我们是有机会后发优势的。";
        createTime = System.currentTimeMillis();
        id = createTime + "";
        readCount =38;
        score =4.7f;
        index(content, createTime, id, readCount, score);

        content = "阿里巴巴有很多争议，似乎无处不在，我还真想不出有什么东西是我们不做的。互联网是一种思想，是一种技术革命，不应该有界限。跨界乐趣无穷。我觉得阿里巴巴的跨界还不错";
        createTime = System.currentTimeMillis();
        id = createTime + "";
        readCount =73;
        score =1.7f;
        index(content, createTime, id, readCount, score);

    }

    private static void index(String content, Long createTime, String id, int readCount, float score) {
        // 实例化IKAnalyzer分词器
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);

        Directory directory = null;
        IndexWriter iwriter;
        try {
            // 索引目录
            directory = new SimpleFSDirectory(new File("D://test/lucene_index_blog"));

            // 配置IndexWriterConfig
            IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory, iwConfig);
            // 写入索引
            Document doc = new Document();

            doc.add(new StringField("ID", id, Field.Store.YES));
            doc.add(new TextField("content", content, Field.Store.YES));
            doc.add(new LongField("createTime", createTime, Field.Store.YES));
            doc.add(new IntField("readCount", readCount, Field.Store.YES));
            doc.add(new FloatField("score", score, Field.Store.YES));
            iwriter.addDocument(doc);
            iwriter.close();
            System.out.println("建立索引成功:" + id);
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