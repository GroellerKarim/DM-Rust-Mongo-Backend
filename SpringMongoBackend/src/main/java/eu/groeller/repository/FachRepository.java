package eu.groeller.repository;

import eu.groeller.domain.v1.Fach;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FachRepository extends MongoRepository<Fach, String> {

    Fach findFachByName(String name);
    Fach findSubjectById(String id);

}
