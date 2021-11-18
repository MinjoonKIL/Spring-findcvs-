package itc.hoseo.findcvs.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}


    /*
    API 설계
    Repository
    SearchRepository
    UserRepository
    CommentRepository
    BookmarkRepository
    -----------------------------------------------
    Controller
    MainController
    -----------------------------------------------
    Service
    SerchService
    UserService
    CommentService
    BookmarkService
    -----------------------------------------------
     */
}
