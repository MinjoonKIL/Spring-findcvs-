package itc.hoseo.findcvs.domain.type;

import java.util.List;

/**
 * 편의점에서 제공되는 서비스
 */
public enum ServicesProvided {
    ALL_DAY("24시간_영업"),
    DELIVERY("택배"),
    FRIED("튀김류"),
    CAFE("카페"),
    LOTTERY("로또&토토"),
    ATM("현금인출기");

    private final String desc;

    private ServicesProvided(String desc){
        this.desc = desc;
    }

}
