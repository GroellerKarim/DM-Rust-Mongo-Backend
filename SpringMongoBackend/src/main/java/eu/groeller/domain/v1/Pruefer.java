package eu.groeller.domain.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.atmosphere.config.service.Get;
import org.bson.BsonTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class Pruefer {
    private String name;
    private Boolean bietedPruefungAn;

    private List<LocalDateTime> datum;

    private LocalDateTime timestamp;
}
