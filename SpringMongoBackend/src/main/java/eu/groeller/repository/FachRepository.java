package eu.groeller.repository;

import eu.groeller.domain.v1.Fach;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FachRepository extends MongoRepository<Fach, String> {

    Fach findFachByName(String name);
    Fach findSubjectById(String id);

}
