package itc.hoseo.findcvs.crawler.emart24;

import itc.hoseo.findcvs.crawler.CrawlerClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class Emart24CrawlerClient implements CrawlerClient {
    private final String EMART24_BASE_URL = "https://emart24.co.kr/introduce2/findBranch.asp";

    public String fetch(int page){
        try{
            return Jsoup.connect(EMART24_BASE_URL)
                    .data(getParameters(page))
                    .method(Connection.Method.POST)
                    .get()
                    .toString();

        }catch (IOException ie){
            throw new RuntimeException("EMART24 크롤링 실패 : [" + page + "] 페이지", ie);
        }
    }


    public Map<String,String> getParameters(int page) {
       Map<String,String> params = new HashMap<>();
        params.put("wpsido","");
        params.put("spgugun","");
        params.put("cpage",Integer.toString(page));
        params.put("service_cv","");
        params.put("stplacesido","");
        params.put("stplacegugun","");
        params.put("sText","");
        return params;
    }
}
