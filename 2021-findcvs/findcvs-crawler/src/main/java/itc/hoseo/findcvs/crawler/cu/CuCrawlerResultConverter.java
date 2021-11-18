package itc.hoseo.findcvs.crawler.cu;

import itc.hoseo.findcvs.crawler.ResultConverter;
import itc.hoseo.findcvs.crawler.common.AddressConverter;
import itc.hoseo.findcvs.crawler.dto.CvsCrawlResult;
import itc.hoseo.findcvs.crawler.dto.Location;
import itc.hoseo.findcvs.domain.type.Brand;
import itc.hoseo.findcvs.domain.type.ServicesProvided;
import lombok.RequiredArgsConstructor;
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
public class CuCrawlerResultConverter implements ResultConverter {
    @Autowired
    private AddressConverter addressConverter;

    private final String CU_RESULT_SELECTOR = "#result_search > div.result_store > div.detail_store > table > tbody > tr";
    private Map<String, ServicesProvided> servicesProvidedMap = Stream.of(
            new AbstractMap.SimpleEntry<>("sevice01", ALL_DAY),
            new AbstractMap.SimpleEntry<>("sevice02", DELIVERY),
            //new AbstractMap.SimpleEntry<>("sevice03", ), 베이커리
            new AbstractMap.SimpleEntry<>("sevice04", FRIED),
            new AbstractMap.SimpleEntry<>("sevice05", CAFE),
            new AbstractMap.SimpleEntry<>("sevice06", LOTTERY),
            new AbstractMap.SimpleEntry<>("sevice07", LOTTERY),
            new AbstractMap.SimpleEntry<>("sevice08", ATM)
            //new AbstractMap.SimpleEntry<>("sevice09", ), 무인복합기(복사기 같은거)
            //new AbstractMap.SimpleEntry<>("sevice10", ), POS 현금인출
            //new AbstractMap.SimpleEntry<>("sevice11", ) 공유 보조배터리
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public List<CvsCrawlResult> toCvsCrawlResults(String crawlingResult) {
        return Jsoup.parse(crawlingResult)
                .select(CU_RESULT_SELECTOR)
                .stream()
                .map(d -> {
                    AddressConverter.LatLng latLng = addressConverter.getLatLng(d.select("address").text());

                    return CvsCrawlResult.builder()
                            .id(String.format("cu_%s_%f_%f", d.select(".name").text(), latLng.getLat(), latLng.getLng()))
                            .brand(Brand.CU)
                            .name(d.select(".name").text())
                            .telno(d.select(".tel").text())
                            .address(d.select("address").text())
                            .servicesProvideds(getServicesProvideds(d.select("ul").html()))
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
                .select(".on")
                .stream()
                .map(e -> {
                    return servicesProvidedMap.get(e.classNames().stream().filter(s -> s.startsWith("sevice")).findFirst().orElse(null));
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}

