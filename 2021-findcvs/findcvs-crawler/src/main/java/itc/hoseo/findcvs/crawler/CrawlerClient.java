package itc.hoseo.findcvs.crawler;

@FunctionalInterface
public interface CrawlerClient {
    public String fetch(int page);
}
