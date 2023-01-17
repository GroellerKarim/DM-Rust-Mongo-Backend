package eu.groeller.repository;

import eu.groeller.domain.v2.MoeglichePruefungenV2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoeglichePruefungenV2Repository extends MongoRepository<MoeglichePruefungenV2, String> {
}
