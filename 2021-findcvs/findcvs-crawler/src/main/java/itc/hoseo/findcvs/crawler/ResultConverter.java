package itc.hoseo.findcvs.crawler;

import itc.hoseo.findcvs.crawler.dto.CvsCrawlResult;

import java.util.List;

@FunctionalInterface
public interface ResultConverter {
    public List<CvsCrawlResult> toCvsCrawlResults(String content);

}
