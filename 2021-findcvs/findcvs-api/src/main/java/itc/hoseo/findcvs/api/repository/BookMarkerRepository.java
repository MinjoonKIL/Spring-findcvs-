package itc.hoseo.findcvs.api.repository;

import itc.hoseo.findcvs.api.dto.BookMarkerDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMarkerRepository {
    public BookMarkerDTO save();
    public BookMarkerDTO delete(String id);
    public List<BookMarkerDTO> findById(String id);
}
