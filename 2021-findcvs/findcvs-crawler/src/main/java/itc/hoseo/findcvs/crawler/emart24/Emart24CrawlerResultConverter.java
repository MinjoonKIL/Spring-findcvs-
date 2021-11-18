package itc.hoseo.findcvs.crawler.emart24;

import itc.hoseo.findcvs.crawler.ResultConverter;
import itc.hoseo.findcvs.crawler.common.AddressConverter;
import itc.hoseo.findcvs.crawler.dto.CvsCrawlResult;
import itc.hoseo.findcvs.crawler.dto.Location;
import itc.hoseo.findcvs.domain.type.Brand;
import itc.hoseo.findcvs.domain.type.ServicesProvided;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static itc.hoseo.findcvs.domain.type.ServicesProvided.*;

@Slf4j
public class Emart24CrawlerResultConverter implements ResultConverter {
    @Autowired
    private AddressConverter addressConverter;

    private final String EMART24_RESULT_SELECTOR = "#skipCont > div.section > div.find_listArea.openList > table > tbody > tr";

    private Map<String, ServicesProvided> servicesProvidedMap = Stream.of(
            new AbstractMap.SimpleEntry<>("checkin1", ALL_DAY),
            new AbstractMap.SimpleEntry<>("checkin2", CAFE),
            new AbstractMap.SimpleEntry<>("checkin3", DELIVERY),
            new AbstractMap.SimpleEntry<>("checkin4", LOTTERY),
            new AbstractMap.SimpleEntry<>("checkin5", ATM)
            //new AbstractMap.SimpleEntry<>("checkin6", ), //스무디킹
            //new AbstractMap.SimpleEntry<>("checkin7", ), //애플액세서리
            //new AbstractMap.SimpleEntry<>("checkin8", ) //앱택배
            //new AbstractMap.SimpleEntry<>("checkin9", ), //와인
            //new AbstractMap.SimpleEntry<>("checkin10", ), //QR출입
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public List<CvsCrawlResult> toCvsCrawlResults(String crawlingResult) {
        return Jsoup.parse(crawlingResult)
                .select(EMART24_RESULT_SELECTOR)
                .stream()
                .map(tr -> {
                    final String address = tr.select(".txtLeft > p:nth-child(1)").text().split("\\|")[0].trim();
                    final String name = tr.select("strong").text();
                    AddressConverter.LatLng latLng = addressConverter.getLatLng(address);

                    return CvsCrawlResult.builder()
                            .id(String.format("emart_%s_%f_%f", name, latLng.getLat(), latLng.getLng()))
                            .brand(Brand.EMART24)
                            .name(name)
                            .telno(tr.select(".pull-left").text().replaceAll("전화번호 : ",""))
                            .address(address)
                            .servicesProvideds(getServicesProvideds(tr.select(".find_listSelect_Img").html()))
                            .location(new Location(latLng.getLat(), latLng.getLng()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public int getTotalPageCount(String crawlingResult) {
        String onClickStr = Jsoup.parse(crawlingResult)
                .select("#paging > ul > a:nth-child(9)")
                .attr("onclick");
        return Integer.parseInt(StringUtils.substringBetween(onClickStr, "(", ")"));
    }

    private Set<ServicesProvided> getServicesProvideds(String services) {
        return Jsoup.parse(services)
                .select("img[src$=_on.png]")
                .stream()
                .map(img -> {
                    String src = img.attr("src");
                    return servicesProvidedMap.get(src.split("\\/")[3].replaceAll("_on.png",""));
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
