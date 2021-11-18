package itc.hoseo.findcvs.crawler.gs25;

import itc.hoseo.findcvs.crawler.CrawlerClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class Gs25CrawlerClient implements CrawlerClient {
    private final String GS_TOKEN_BASE_URL = "http://gs25.gsretail.com/gscvs/ko/store-services/locations";
    private final String GS_BASE_URL = "http://gs25.gsretail.com/gscvs/ko/store-services/locationList";
    public String fetch(int page) {
        try {
            CookieStore cookieStore = new BasicCookieStore();

            HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            String csrfBody = restTemplate.getForEntity(
                    URI.create(GS_TOKEN_BASE_URL), String.class).getBody();

            String csrf = Jsoup.parse(csrfBody)
                    .select("#CSRFForm > input[type=hidden]")
                    .attr("value");

            ResponseEntity<String> response = restTemplate.postForEntity(
                    GS_BASE_URL + "?CSRFToken=" + csrf, getParameters(page), String.class);


            return response.getBody();

        } catch (RuntimeException ie) {
            throw new RuntimeException("GS 크롤링 실패 : [" + page + "] 페이지", ie);
        }
    }



    public HttpEntity getParameters(int page) {
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        headers.add("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.add("Accept", "application/json, text/javascript, */*; q=0.01" );
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("pageNum",Integer.toString(page));
        params.add("pageSize","50");
        params.add("searchShopName","");
        params.add("searchSido","");
        params.add("searchGugun","");
        params.add("searchDong","");
        params.add("searchType","");
        params.add("searchTypeService","0");
        params.add("searchTypeToto","0");
        params.add("searchTypeCafe25","0");
        params.add("searchTypeInstant","0");
        params.add("searchTypeDrug","0");
        params.add("searchTypeSelf25","0");
        params.add("searchTypePost","0");
        params.add("searchTypeATM","0");
        params.add("searchTypeWithdrawal","0");
        params.add("searchTypeTaxrefund","0");
        params.add("searchTypeSmartAtm","0");
        params.add("searchTypeSelfCookingUtensils","0");
        params.add("searchTypeDeliveryService","0");

        return new HttpEntity<>(params, headers);
    }
}
