package itc.hoseo.findcvs.crawler.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
public class AddressConverter {

    private RestTemplate restTemplate = new RestTemplate();

    public LatLng getLatLng(String address) {
        if(StringUtils.hasText(address) == false) {
            return new LatLng(0.0, 0.0);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK 7720e2e195b7f09bf3215d501c9ffb09");


        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(
                "https://dapi.kakao.com/v2/local/search/address.json?query=" + address,
                HttpMethod.GET,
                entity,
                String.class);

        ObjectMapper om = new ObjectMapper();
        JsonNode root = null;
        try {
            root = om.readTree(result.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new LatLng(0.0, 0.0);
        }
        if(root.path("meta").path("total_count").asInt() == 0){
            return new LatLng(0.0, 0.0);
        }
        //경도 x, 위도 y
        JsonNode doc = ((ArrayNode)root.path("documents")).get(0).path("address");
        return  new LatLng(doc.path("y").asDouble(), doc.path("x").asDouble());
    }

    @Data
    @AllArgsConstructor
    public static class LatLng {
        private final double lat, lng;
    }
}
