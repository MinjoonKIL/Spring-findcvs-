package itc.hoseo.findcvs.api.repository;

import itc.hoseo.findcvs.api.dto.CommentDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository {
    public CommentDTO update(String commentId);
    public CommentDTO delete(String CommentId);
    public CommentDTO save(CommentDTO comment);
    public List<CommentDTO> findByIdCvs(String id);
    public List<CommentDTO> findByIdUser(String id);
}
