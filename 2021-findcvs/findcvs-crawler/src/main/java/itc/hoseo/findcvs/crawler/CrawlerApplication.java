package itc.hoseo.findcvs.crawler;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableBatchProcessing
public class CrawlerApplication {

	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CrawlerApplication.class, args);
        SpringApplication.exit(context);
    }

}
