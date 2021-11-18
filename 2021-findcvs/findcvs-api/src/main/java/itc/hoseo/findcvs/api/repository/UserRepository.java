package itc.hoseo.findcvs.api.repository;

import itc.hoseo.findcvs.api.dto.UserDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    public UserDTO save(UserDTO user);
    public UserDTO findById(String id);
}
