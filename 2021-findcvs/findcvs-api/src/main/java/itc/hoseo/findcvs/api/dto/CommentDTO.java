package itc.hoseo.findcvs.api.dto;

import itc.hoseo.findcvs.domain.model.Cvs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
    private String commentId;
    private String commnet;
    private UserDTO userId;
    private Cvs id;
}
