package eu.groeller.domain.v1;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.BsonTimestamp;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

public class MoeglichePruefungen {

    private List<Pruefer> pruefer;

    private List<Dauer> dauer;

    private PruefArt pruefArt;

}
