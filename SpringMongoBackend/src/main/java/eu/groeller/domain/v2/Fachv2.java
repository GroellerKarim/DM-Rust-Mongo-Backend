package eu.groeller.domain.v2;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "gro_faecher")
public class Fachv2 {

    @Id
    private String id;

    private String name;

    private int version;

    @DocumentReference
    private List<MoeglichePruefungenV2> moeglichePruefungen;

    public Fachv2(String name, List<MoeglichePruefungenV2> moeglichePruefungen) {
        this.name = name;
        this.version = 2;
        this.moeglichePruefungen = moeglichePruefungen;
    }
}
