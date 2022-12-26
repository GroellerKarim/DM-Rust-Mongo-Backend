package eu.groeller.domain.v2;

import eu.groeller.domain.v1.Dauer;
import eu.groeller.domain.v1.PruefArt;
import eu.groeller.domain.v1.Pruefer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "gro_moeglichePruefungen")
public class MoeglichePruefungenV2 {

    @Id
    private String id;

    private List<Pruefer> pruefer;

    private List<Dauer> dauer;

    private PruefArt pruefArt;

    public MoeglichePruefungenV2(List<Pruefer> pruefer, List<Dauer> dauer, PruefArt pruefArt) {
        this.pruefer = pruefer;
        this.dauer = dauer;
        this.pruefArt = pruefArt;
    }
}
