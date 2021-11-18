package itc.hoseo.findcvs.crawler.se;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class SECrawlerClient {
    private final String SE_BASE_URL = "https://www.7-eleven.co.kr/util/storeLayerPop.asp";

    public String fetch(String sido, String gu){
        try{
            return Jsoup.connect(SE_BASE_URL)
                    .data(getParameters(sido,gu))
                    .method(Connection.Method.POST)
                    .get()
                    .toString();

        }catch (IOException ie){
            throw new RuntimeException("CU 크롤링 실패 : [" + sido + "] 페이지", ie);
        }
    }


    public Map<String,String> getParameters(String sido, String gu) {
        Map<String,String> params = new HashMap<>();
        params.put("storeLaySido",sido);
        params.put("storeLayGu",gu);
        params.put("hiddentext","none");
        return params;
    }
}
