package eu.groeller.domain.v1;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.BsonTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Pruefer {
    private String name;
    private Boolean bietedPruefungAn;

    private List<LocalDateTime> datum;

    private LocalDateTime timestamp;
}
