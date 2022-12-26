package eu.groeller.repository;

import eu.groeller.domain.v1.Fach;
import eu.groeller.domain.v2.Fachv2;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Fachv2Repository extends MongoRepository<Fachv2, String> {

    Fachv2 findFachv2ByName(String name);
    Fachv2 findFachv2ById(String id);
}
