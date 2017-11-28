package top.yuyufeng.learn.lucene.demo2;

/**
 * @author yuyufeng
 * @date 2017/11/21
 */
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * created by yuyufeng on 2017/11/13.
 */
public class LuceneSearchDemo {
    public static void main(String[] args) {

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
        Directory directory = null;
        IndexReader ireader = null;
        IndexSearcher isearcher;

        try {
            //索引目录
            directory = new SimpleFSDirectory(new File("D://test/lucene_index_blog"));
            // 配置IndexWriterConfig
            IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

            // 实例化搜索器
            ireader = DirectoryReader.open(directory);
            isearcher = new IndexSearcher(ireader);


            BooleanQuery booleanQuery = new BooleanQuery();
            String keyword = "达摩院";
            // 条件一
            QueryParser qp = new QueryParser(Version.LUCENE_47, "content", analyzer);
            Query query = qp.parse(keyword);
            booleanQuery.add(query,BooleanClause.Occur.MUST);

            //条件二
            query = NumericRangeQuery.newFloatRange("score",1f,5f,true,true);
            booleanQuery.add(query,BooleanClause.Occur.MUST);



            TopDocs topDocs = isearcher.search(booleanQuery,100);
            System.out.println("命中：" + topDocs.totalHits);
            // 遍历输出结果
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i = 0; i < topDocs.totalHits; i++) {
                Document targetDoc = isearcher.doc(scoreDocs[i].doc);
                System.out.println("内容：" + targetDoc.toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (ireader != null) {
                try {
                    ireader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
 * 1．MUST和MUST：取得连个查询子句的交集。
 2．MUST和MUST_NOT：表示查询结果中不能包含MUST_NOT所对应得查询子句的检索结果。
 3．SHOULD与MUST_NOT：连用时，功能同MUST和MUST_NOT。
 4．SHOULD与MUST连用时，结果为MUST子句的检索结果,但是SHOULD可影响排序。
 5．SHOULD与SHOULD：表示“或”关系，最终检索结果为所有检索子句的并集。
 6．MUST_NOT和MUST_NOT：无意义，检索无结果。
 **/