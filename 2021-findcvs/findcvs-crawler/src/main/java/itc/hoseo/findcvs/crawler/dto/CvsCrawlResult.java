package itc.hoseo.findcvs.crawler.dto;

import itc.hoseo.findcvs.domain.type.Brand;
import itc.hoseo.findcvs.domain.type.ServicesProvided;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CvsCrawlResult {
    private String id; //브랜드명_편의점이름_위도_경도
    private Brand brand;
    private Set<ServicesProvided> servicesProvideds;
    private String name;
    //FIXME 주소를 세분화해서 저장할껀지?
    private String address;
    private String telno;

    private Location location;
}
