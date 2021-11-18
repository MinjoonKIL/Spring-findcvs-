package itc.hoseo.findcvs.api;

import com.google.common.collect.ImmutableSet;
import itc.hoseo.findcvs.api.es.EsQueryBuilder;
import itc.hoseo.findcvs.api.service.CvsService;
import itc.hoseo.findcvs.domain.model.Cvs;
import itc.hoseo.findcvs.domain.type.ServicesProvided;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
class ApiApplicationTests {

    @Autowired
    CvsService cvsService;

    Cvs cvs;
    ServicesProvided service;

	@Test
    void Test() throws Exception{
        //cvsService.search(37.56292,126.83995,3,ImmutableSet.of(service.ALL_DAY,service.CAFE));
//        cvsService.search(37.56292,126.83995,3,"강서구",ImmutableSet.of(service.ALL_DAY,service.CAFE));
        //cvsService.search();
        //cvsService.search(37.56292,126.83995,3,"호서");
    }


}
