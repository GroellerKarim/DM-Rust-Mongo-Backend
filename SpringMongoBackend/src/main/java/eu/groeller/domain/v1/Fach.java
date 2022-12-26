package eu.groeller.domain.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "gro_faecher")
public class Fach {

    @Id
    private String id;

    private String name;

    private int version;

    private List<MoeglichePruefungen> moeglichePruefungen;

    public Fach(String name, List<MoeglichePruefungen> moeglichePruefungen) {
        this.name = name;
        this.version = 1;
        this.moeglichePruefungen = moeglichePruefungen;
    }
}
