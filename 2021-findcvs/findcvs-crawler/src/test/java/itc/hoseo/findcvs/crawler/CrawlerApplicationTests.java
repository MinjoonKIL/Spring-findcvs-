package itc.hoseo.findcvs.crawler;

import itc.hoseo.findcvs.crawler.dto.CvsCrawlResult;
import itc.hoseo.findcvs.crawler.gs25.Gs25CrawlerResultConverter;
import itc.hoseo.findcvs.crawler.se.SECrawlerClient;
import itc.hoseo.findcvs.crawler.se.SECrawlerResultConverter;
import itc.hoseo.findcvs.crawler.cu.CuCrawlerClient;
import itc.hoseo.findcvs.crawler.cu.CuCrawlerResultConverter;
import itc.hoseo.findcvs.crawler.emart24.Emart24CrawlerClient;
import itc.hoseo.findcvs.crawler.emart24.Emart24CrawlerResultConverter;
import itc.hoseo.findcvs.crawler.gs25.Gs25CrawlerClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CrawlerApplicationTests {


    @Autowired
    SECrawlerResultConverter seConverter;


    @Test
    void gsTest() {
        Gs25CrawlerClient gs25CrawlerClient = new Gs25CrawlerClient();
        String fetchResult = gs25CrawlerClient.fetch(1);
        Gs25CrawlerResultConverter converter = new Gs25CrawlerResultConverter();
        for(CvsCrawlResult rs : converter.toCvsCrawlResults(fetchResult)){
            System.out.println(rs);
        }
        //converter.toCvsCrawlResults(fetchResult);
        //assertEquals(3109,converter.getTotalPageCount(fetchResult));
    }


}
