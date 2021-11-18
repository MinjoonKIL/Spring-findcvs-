package itc.hoseo.findcvs.api.repository;

import itc.hoseo.findcvs.domain.model.Cvs;
import itc.hoseo.findcvs.domain.type.ServicesProvided;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CvsRepository {
    public List<Cvs> findByServicesProvided(ServicesProvided... servicesProvideds);

//    public Cvs save(Cvs cvs);
//    public List<Cvs> findAll();
//    public List<Cvs> findById(String id);
//    public List<Cvs> findArea(double lat,double lon);
}
