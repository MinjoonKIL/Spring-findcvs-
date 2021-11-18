package itc.hoseo.findcvs.crawler.se;

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

import static itc.hoseo.findcvs.domain.type.ServicesProvided.*;
import static org.apache.commons.lang3.StringUtils.indexOf;

@Slf4j
@Component
public class SECrawlerResultConverter implements ResultConverter {
    private final String SE_RESULT_SELECTOR = "#storeForm > div:nth-child(4) > div.list_stroe > ul > li";
    private Map<String, ServicesProvided> servicesProvidedMap = Stream.of(
            new AbstractMap.SimpleEntry<>("ico_24h", ALL_DAY),
            new AbstractMap.SimpleEntry<>("ico_cafe", CAFE),
            new AbstractMap.SimpleEntry<>("ico_parcel",DELIVERY),
            //new AbstractMap.SimpleEntry<>("medical", FRIED),
            //new AbstractMap.SimpleEntry<>("reserve", CAFE),
            new AbstractMap.SimpleEntry<>("ico_atm", ATM),
            new AbstractMap.SimpleEntry<>("ico_chicken", FRIED),
            new AbstractMap.SimpleEntry<>("ico_toto", LOTTERY)
            //new AbstractMap.SimpleEntry<>("sevice09", ), 무인복합기(복사기 같은거)
            //new AbstractMap.SimpleEntry<>("sevice10", ), POS 현금인출
            //new AbstractMap.SimpleEntry<>("sevice11", ) 공유 보조배터리
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public List<CvsCrawlResult> toCvsCrawlResults(String crawlingResult) {
        return Jsoup.parse(crawlingResult)
                .select(SE_RESULT_SELECTOR)
                .stream()
                .map(d -> {
                    double lat = Double.parseDouble(d.select("li> a").attr("href").replaceAll("[^0-9.,]", "").split("\\,")[1]);
                    double lng = Double.parseDouble(d.select("li> a").attr("href").replaceAll("[^0-9.,]", "").split("\\,")[2]);
                    String name = d.select("a > span:nth-child(1)").text();
                    return CvsCrawlResult.builder()
                            .id(String.format(String.format("se_%s_%f_%f", name, lat, lng)))
                            .brand(Brand.SEVEN_ELEVEN)
                            .name(d.select("a > span:nth-child(1)").text())
                            .telno(d.select(".tel").text())
                            .address(d.select("a > span:nth-child(2)").text())
                            .servicesProvideds(getServicesProvideds(d.select("a > span").html()))
                            .location(new Location(lat,lng))
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
                .select("img[src$=.png]")
                .stream()
                .map(e -> {
                    String src = e.attr("src");
                    return servicesProvidedMap.get(src.split("\\/")[5].replaceAll(".png",""));
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

    }
}
