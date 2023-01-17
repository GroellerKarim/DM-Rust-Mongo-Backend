package eu.groeller.domain.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.BsonTimestamp;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class Dauer {

    private int stunden;
    private int minuten;
    private LocalDateTime timestamp;
}
