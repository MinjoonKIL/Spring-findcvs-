package itc.hoseo.findcvs.crawler.cu;

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
public class CuCrawlerClient implements CrawlerClient {
    private final String CU_BASE_URL = "http://cu.bgfretail.com/store/list_Ajax.do";

    @Override
    public String fetch(int page){
        try{
            return Jsoup.connect(CU_BASE_URL)
                    .data(getParameters(page))
                    .method(Connection.Method.POST)
                    .get()
                    .toString();

        }catch (IOException ie){
            throw new RuntimeException("CU 크롤링 실패 : [" + page + "] 페이지", ie);
        }
    }


    public Map<String,String> getParameters(int page) {
        Map<String,String> params = new HashMap<>();
       params.put("pageIndex",Integer.toString(page));
       params.put("listType","");
       params.put("jumpoCode","");
       params.put("jumpoLotto","");
       params.put("jumpoToto","");
       params.put("jumpoCash","");
       params.put("jumpoHour","");
       params.put("jumpoCafe","");
       params.put("jumpoDelivery","");
       params.put("jumpoBakery","");
       params.put("jumpoFry","");
       params.put("jumpoMultiDevice","");
       params.put("jumpoPosCash","");
       params.put("jumpoBattery","");
       params.put("jumpoAdderss","");
       params.put("jumpoSido","");
       params.put("jumpoGugun","");
       params.put("jumpodong","");
       params.put("user_id","");
       params.put("sido","");
       params.put("Gugun","");
       params.put("jumpoName","");
       return params;
    }
}
