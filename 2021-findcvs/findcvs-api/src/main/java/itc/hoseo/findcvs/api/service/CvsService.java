package itc.hoseo.findcvs.api.service;

import itc.hoseo.findcvs.domain.type.Brand;
import itc.hoseo.findcvs.domain.type.ServicesProvided;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CvsService {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchCond {
        double lat;
        double lng;
        List<ServicesProvided> servicesProvideds;
        int distance;
        String keyword;
        List<Brand> brands;
    }

    @Autowired
    private RestHighLevelClient client;


    public String search(SearchCond cond){
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        //기본 검색 쿼리(중심좌표 기준 500미터)
        queryBuilder.must(QueryBuilders.geoDistanceQuery("location").point(cond.lat, cond.lng).distance(cond.distance + "m"));
        if(cond.servicesProvideds.size() > 0){
            cond.servicesProvideds.stream()
                    .forEach(sp -> {
                        queryBuilder.must(QueryBuilders.termQuery("servicesProvideds",sp.name()));
                    });
        }

        if(cond.brands.size() > 0){
            BoolQueryBuilder orCond = new BoolQueryBuilder();
            cond.brands.stream()
                    .forEach(b -> {
                        orCond.should(QueryBuilders.termQuery("brand", b.name()));
                    });
            queryBuilder.must(orCond);
        }

        return buildQuery(queryBuilder);
    }
//
//
//    public String search(double lat, double lng, List<ServicesProvided> servicesProvidedList, int distance) {
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        searchLocation(boolQueryBuilder,lat,lng,distance);
//
//        return buildQuery(boolQueryBuilder);
//
//    }
//
//    public String search(double lat,double lng,int distance, String name) {
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        searchLocation(boolQueryBuilder,lat,lng,distance);
//        searchName(boolQueryBuilder,name);
//        return buildQuery(boolQueryBuilder);
//    }
//
//    public String search(double lat,double lng,int distance,Set<ServicesProvided> service){
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        searchLocation(boolQueryBuilder,lat,lng,distance);
//        searchService(boolQueryBuilder,service);
//        return buildQuery(boolQueryBuilder);
//    }
//
//    public String search(double lat,double lng,int distance,String name,Set<ServicesProvided> service) {
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        searchLocation(boolQueryBuilder,lat,lng,distance);
//        searchName(boolQueryBuilder,name);
//        searchService(boolQueryBuilder,service);
//        return buildQuery(boolQueryBuilder);
//    }
//
//
//
//    //Search Service
//    public BoolQueryBuilder searchLocation(BoolQueryBuilder boolQueryBuilder ,double lat,double lng,int distance){
//        return boolQueryBuilder.must(QueryBuilders.geoDistanceQuery("location").point(lat, lng).distance(distance + "m"));
//    }
//
//    public BoolQueryBuilder searchName(BoolQueryBuilder boolQueryBuilder, String name){
//        boolQueryBuilder.should(QueryBuilders.matchQuery("address",name));
//        boolQueryBuilder.should(QueryBuilders.matchQuery("name",name));
//        boolQueryBuilder.minimumShouldMatch(1);
//        return boolQueryBuilder;
//    }
//
//    public BoolQueryBuilder searchService(BoolQueryBuilder boolQueryBuilder,Set<ServicesProvided> service) {
//        Iterator<ServicesProvided> iter = service.iterator();
//        int loop =0;
//        while (iter.hasNext()) {
//            boolQueryBuilder.must(QueryBuilders.termQuery("servicesProvideds",iter.next()));
//            loop += 1;
//            if(loop >= 5)
//                break;
//        }
//        return boolQueryBuilder;
//    }

    public String buildQuery(BoolQueryBuilder boolQueryBuilder) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("cvs");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);

        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("ES 조회중 오류 발생", e);
        }

        String body = Arrays.asList(response.getHits().getHits())
                .stream()
                .map(SearchHit::getSourceAsString)
                .collect(Collectors.joining(","));
        body = "[" + body + "]";

        return body;
    }
}
