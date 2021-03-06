package itc.hoseo.findcvs.crawler.se;

import com.fasterxml.jackson.databind.ObjectMapper;
import itc.hoseo.findcvs.crawler.CrawlerClient;
import itc.hoseo.findcvs.crawler.ResultConverter;
import itc.hoseo.findcvs.crawler.cu.CuCrawlerClient;
import itc.hoseo.findcvs.crawler.cu.CuCrawlerResultConverter;
import itc.hoseo.findcvs.crawler.dto.CvsCrawlResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@ConditionalOnProperty(name = "spring.batch.job.names", havingValue = "se")
@RequiredArgsConstructor
@Slf4j
public class SEBatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Bean
    public Job createJob() {
        return jobBuilderFactory.get("se")
                .start(createStep())
                .build();
    }


    @Bean
    public Step createStep() {
        final ObjectMapper om = new ObjectMapper();

        return stepBuilderFactory.get("chunk")
                .<CvsCrawlResult, CvsCrawlResult>chunk(50)
                .reader(crawResultReader())
                .writer(items -> {
                    BulkRequest bulkRequest = new BulkRequest();

                    items.forEach(i -> {
                        UpdateRequest updateRequest = new UpdateRequest("cvs", i.getId())
                                .doc(om.convertValue(i, Map.class))
                                .upsert(om.convertValue(i, Map.class));
                        bulkRequest.add(updateRequest);
                    });
                    restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                })
                .build();
    }


    @Bean
    public CvsCrawResultReader crawResultReader() {
        return new CvsCrawResultReader(new SECrawlerClient(), resultConverter());
    }

    @Bean
    public ResultConverter resultConverter() {
        return new SECrawlerResultConverter();
    }

    @RequiredArgsConstructor
    class CvsCrawResultReader extends ItemStreamSupport implements ItemReader<CvsCrawlResult> {
        private final SECrawlerClient client;
        private final ResultConverter converter;

        private int curIndex;
        private List<CvsCrawlResult> list;
        private Queue<String> paramQueue = new LinkedList<>();
        private FetchParm curParam;

        @Override
        public CvsCrawlResult read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            CvsCrawlResult cvs = null;

            if (curIndex < list.size() && list.size() > 0) {
                cvs = list.get(curIndex);
                curIndex++;
            }

            if(curIndex == list.size()) {
                list = getList();
                if(list.size() == 0) {
                    return null;
                }
                curIndex = 0;
                cvs = list.get(curIndex);
                curIndex++;
            }

            return cvs;
        }

        @Override
        public void open(ExecutionContext executionContext) {
            for(FetchParm fp : FetchParm.values()){
                if(fp.city.length == 0){
                    paramQueue.add(fp.name());
                    continue;
                }

                for(String city : fp.city){
                    paramQueue.add(fp.name() + "@" + city);
                }
            }
            list = getList();
        }

        private List<CvsCrawlResult> getList() {
            String pStr =  paramQueue.poll();
            if(pStr == null) return Collections.emptyList();
            if(pStr.contains("@")){
                String[] pArr = pStr.split("@");
                return converter.toCvsCrawlResults(client.fetch(pArr[0], pArr[1]));
            }else {
                return converter.toCvsCrawlResults(client.fetch(pStr, ""));
            }
        }

        @Override
        public void update(ExecutionContext executionContext) {
//            if (curIndex == list.size()) {
//                list = getList();
//                curIndex = 0;
//            }
        }
    }

    enum FetchParm {
        ?????????,
        ????????????,
        ????????????,
        ??????,
        ??????,
        ??????,
        ??????,
        ??????,
        ??????,
        ??????,
        ????????????,
        ????????????,
        ?????????,
        ????????????,
        ????????????,
        ??????("?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "????????????", "?????????", "?????????", "????????????", "?????????", "?????????", "?????????", "?????????", "?????????", "????????????", "?????????", "?????????", "?????????", "??????", "?????????"),
        ?????????("?????????", "????????? ?????????", "????????? ????????????", "????????? ????????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "????????????", "????????????", "????????? ?????????", "????????? ?????????", "????????? ?????????", "????????? ?????????", "????????? ?????????", "????????? ?????????", "????????? ?????????", "????????? ?????????", "????????? ?????????", "????????? ?????????", "?????????", "????????? ?????????", "????????? ?????????", "?????????", "????????? ?????????", "????????? ?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "????????? ?????????", "????????? ?????????", "????????? ?????????", "?????????", "????????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????");
        private String state;
        private String[] city;

        private FetchParm(String... city) {
            if(city.length == 0){
                this.city = new String[0];
            }else{
                this.city = city;
            }
        }
    }
}
