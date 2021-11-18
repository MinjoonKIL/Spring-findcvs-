package itc.hoseo.findcvs.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String userId;
    private String userName;
}
