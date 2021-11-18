package itc.hoseo.findcvs.crawler.gs25;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import itc.hoseo.findcvs.crawler.ResultConverter;
import itc.hoseo.findcvs.crawler.dto.CvsCrawlResult;
import itc.hoseo.findcvs.crawler.dto.Location;
import itc.hoseo.findcvs.domain.type.Brand;
import itc.hoseo.findcvs.domain.type.ServicesProvided;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static itc.hoseo.findcvs.domain.type.ServicesProvided.*;

@Slf4j
@Component
public class Gs25CrawlerResultConverter implements ResultConverter {
    private final String CU_RESULT_SELECTOR = "#result_search > div.result_store > div.detail_store > table > tbody > tr";
    private Map<String, ServicesProvided> servicesProvidedMap = Stream.of(
            //new AbstractMap.SimpleEntry<>("sevice01", ALL_DAY),
            new AbstractMap.SimpleEntry<>("post", DELIVERY),
            //new AbstractMap.SimpleEntry<>("sevice03", ), 베이커리
            new AbstractMap.SimpleEntry<>("instant", FRIED),
            new AbstractMap.SimpleEntry<>("cafe25", CAFE),
            new AbstractMap.SimpleEntry<>("toto", LOTTERY),
            new AbstractMap.SimpleEntry<>("atm", ATM),
            new AbstractMap.SimpleEntry<>("ssmart_atm", ATM)
            //new AbstractMap.SimpleEntry<>("sevice09", ), 무인복합기(복사기 같은거)
            //new AbstractMap.SimpleEntry<>("sevice10", ), POS 현금인출
            //new AbstractMap.SimpleEntry<>("sevice11", ) 공유 보조배터리
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public List<CvsCrawlResult> toCvsCrawlResults(String crawlingResult) {
        ObjectMapper om = new ObjectMapper();
        try {
            crawlingResult = crawlingResult.replaceAll("\\\\", "");
            crawlingResult = crawlingResult.substring(1, crawlingResult.length() - 1);
            JsonNode root = om.readTree(crawlingResult);
            ArrayNode arrayNode = (ArrayNode) root.path("results");
            return StreamSupport.stream(Spliterators
                            .spliteratorUnknownSize(arrayNode.elements(),
                                    Spliterator.ORDERED), false)
                    .map(jn -> {
                        double lat = jn.path("longs").asDouble();
                        double lng = jn.path("lat").asDouble();
                        String name = jn.path("shopName").textValue();
                        return CvsCrawlResult.builder()
                                .id(String.format("gs_%s_%f_%f", name, lat, lng))
                                .brand(Brand.GS25)
                                .name(jn.path("shopName").textValue())
                                .address(jn.path("address").textValue())
                                .location(new Location(lat, lng))
                                .servicesProvideds(getServicesProvideds(jn.path("offeringService").toString()))
                                .build();
                    }).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json 파싱 실패", e);
        }
    }

    public int getTotalPageCount(String crawlingResult) {
        String onClickStr = Jsoup.parse(crawlingResult)
                .select("#paging > ul > a:nth-child(9)")
                .attr("onclick");
        return Integer.parseInt(StringUtils.substringBetween(onClickStr, "(", ")"));
    }

    private Set<ServicesProvided> getServicesProvideds(String services) {
        if (StringUtils.isEmpty(services)) return Collections.emptySet();
        ObjectMapper om = new ObjectMapper();
        try {
            ArrayNode arrayNode = (ArrayNode) om.readTree(services);
            return StreamSupport.stream(Spliterators
                            .spliteratorUnknownSize(arrayNode.elements(),
                                    Spliterator.ORDERED), false)
                    .map((str) -> {
                        return servicesProvidedMap.get(str.textValue());
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("serviceProvied 파싱 실패", e);
        }
    }
}
