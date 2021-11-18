package itc.hoseo.findcvs.crawler.emart24;

import com.fasterxml.jackson.databind.ObjectMapper;
import itc.hoseo.findcvs.crawler.CrawlerClient;
import itc.hoseo.findcvs.crawler.ResultConverter;
import itc.hoseo.findcvs.crawler.cu.CuCrawlerClient;
import itc.hoseo.findcvs.crawler.cu.CuCrawlerResultConverter;
import itc.hoseo.findcvs.crawler.dto.CvsCrawlResult;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "spring.batch.job.names", havingValue = "emart")
@RequiredArgsConstructor
public class Emart24BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Bean
    public Job createJob() {
        return jobBuilderFactory.get("emart")
                .start(createStep())
                .build();
    }


    @Bean
    public Step createStep() {
        final ObjectMapper om = new ObjectMapper();

        return stepBuilderFactory.get("chunk")
                .<CvsCrawlResult, CvsCrawlResult>chunk(5)
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
        return new CvsCrawResultReader(new Emart24CrawlerClient(), resultConverter());
    }

    @Bean
    public ResultConverter resultConverter() {
        return new Emart24CrawlerResultConverter();
    }

    @RequiredArgsConstructor
    class CvsCrawResultReader extends ItemStreamSupport implements ItemReader<CvsCrawlResult> {
        private final CrawlerClient client;
        private final ResultConverter converter;

        private int curIndex;
        private int curPage;
        private List<CvsCrawlResult> list;

        @Override
        public CvsCrawlResult read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            CvsCrawlResult cvs = null;

            if (curIndex < list.size() && list.size() > 0) {
                cvs = list.get(curIndex);
                curIndex++;
            }

            return cvs;
        }

        @Override
        public void open(ExecutionContext executionContext) {
            curPage = 1;
            list = converter.toCvsCrawlResults(client.fetch(curPage));
            curPage++;
        }

        @Override
        public void update(ExecutionContext executionContext) {
            if (curIndex == list.size()) {
                list = converter.toCvsCrawlResults(client.fetch(curPage));
                curPage++;
                curIndex = 0;
            }
        }
    }
}
