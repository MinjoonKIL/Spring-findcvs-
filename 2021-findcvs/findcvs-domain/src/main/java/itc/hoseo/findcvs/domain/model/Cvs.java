package itc.hoseo.findcvs.domain.model;

import itc.hoseo.findcvs.domain.type.Brand;
import itc.hoseo.findcvs.domain.type.ServicesProvided;
import lombok.Data;

import java.util.Set;

/**
 * 편의점 정보를 나타내는 도메인 클래스
 */
@Data
public class Cvs {
    private String id; //브랜드명_편의점이름_위도_경도
    private Brand brand;
    private Set<ServicesProvided> servicesProvideds;
    private String name;
    //FIXME 주소를 세분화해서 저장할껀지?
    private String address;
    private String telno;
    private double latitude; //위도
    private double longitude; //경도
}
