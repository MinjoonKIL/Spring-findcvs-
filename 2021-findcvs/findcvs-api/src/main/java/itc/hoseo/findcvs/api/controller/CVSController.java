package itc.hoseo.findcvs.api.controller;

import itc.hoseo.findcvs.api.service.CvsService;
import itc.hoseo.findcvs.domain.type.Brand;
import itc.hoseo.findcvs.domain.type.ServicesProvided;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CVSController {

    private final CvsService cvsService;

    @GetMapping("/")
    public String search(@RequestParam("c") String c,
                         @RequestParam List<ServicesProvided> servicesProvideds,
                         @RequestParam List<Brand> brands) {
        //lat 위도 : -90 ~ 90
        //lng 경도 : -180 ~ 180
        double lat = 37.56290925619275;
        double lng = 126.8399490701588;


        if (StringUtils.hasLength(c) == true || c.contains(",") == true) {
            String latlng[] = c.split(",");
            lat = Double.parseDouble(latlng[0]);
            lng = Double.parseDouble(latlng[1]);
        }
        CvsService.SearchCond searchCond = CvsService.SearchCond.builder()
                .lat(lat)
                .lng(lng)
                .distance(500)
                .servicesProvideds(servicesProvideds)
                .brands(brands)
                .build();

        return cvsService.search(searchCond);
    }
}
