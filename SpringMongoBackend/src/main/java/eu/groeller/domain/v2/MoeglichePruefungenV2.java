package eu.groeller.domain.v2;

import eu.groeller.domain.v1.Dauer;
import eu.groeller.domain.v1.PruefArt;
import eu.groeller.domain.v1.Pruefer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.atmosphere.config.service.Get;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter


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

    public String getExaminer() {
        var examiner = pruefer.stream().findFirst();
        if(examiner.isPresent()) {
            return examiner.get().getName();
        }
        else
            return "";
    }

    public void setExaminer(String name) {
        pruefer = new ArrayList<>(List.of(new Pruefer(name, true, List.of(LocalDateTime.now()), LocalDateTime.now())));
    }

    public String getDauer() {
        var  zeit = dauer.stream().findAny();

        if(zeit.isPresent()) {
            var minuten = zeit.get().getMinuten() < 10 ?
                    "0" + zeit.get().getMinuten():
                    zeit.get().getMinuten();
            return zeit.get().getStunden() + ":" + minuten;
        }
        else {
            return "";
        }
    }

    public void setDauer(String dauer) {
        var split = dauer.split(":");

        if (split.length != 2) {
            throw new RuntimeException("Invalid Dauer");
        }

        var newDauer = new Dauer(Integer.valueOf(split[0]), Integer.valueOf(split[1]), LocalDateTime.now());

        this.dauer = new ArrayList<>(List.of(newDauer));
    }
}
