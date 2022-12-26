package eu.groeller.springmongobackend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.groeller.SpringMongoBackendApplication;
import eu.groeller.domain.v1.*;
import eu.groeller.domain.v2.Fachv2;
import eu.groeller.domain.v2.MoeglichePruefungenV2;
import eu.groeller.repository.FachRepository;
import eu.groeller.repository.Fachv2Repository;
import eu.groeller.repository.MoeglichePruefungenV2Repository;
import org.bson.BsonTimestamp;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(classes = SpringMongoBackendApplication.class)
class SpringMongoBackendApplicationTests {

	@Autowired
	FachRepository fachRepository;

	@Autowired
	Fachv2Repository fachv2Repository;

	@Autowired
	MoeglichePruefungenV2Repository moeglichePruefungenV2Repository;
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	@Test
	@Order(1)
	void insertV1() {

		Pruefer pruefer = new Pruefer("Schruti", true, List.of(LocalDateTime.now()), LocalDateTime.now());
		Pruefer pruefer2 = new Pruefer("Person2", false, List.of(LocalDateTime.now()), LocalDateTime.now());

		Dauer dauer = new Dauer(2, 30, LocalDateTime.now());
		MoeglichePruefungen pruefungen = new MoeglichePruefungen(List.of(pruefer, pruefer2), List.of(dauer), PruefArt.SCHRIFTLICH);

		Fach fach = new Fach("DBI", List.of(pruefungen));

		fach = fachRepository.save(fach);
		System.out.println(gson.toJson(fach));
	}

	@Test
	@Order(2)
	void insertV2() {
		Pruefer pruefer = new Pruefer("PersonX", true, List.of(LocalDateTime.now()), LocalDateTime.now());
		Dauer dauer = new Dauer(0, 20, LocalDateTime.now());
		MoeglichePruefungenV2 pruefungen = new MoeglichePruefungenV2(List.of(pruefer), List.of(dauer), PruefArt.MUENDLICH);

		pruefungen = moeglichePruefungenV2Repository.save(pruefungen);


		Fachv2 fach = new Fachv2("Mathe", List.of(pruefungen));

		fach = fachv2Repository.save(fach);
		System.out.println(gson.toJson(fach));
	}

	@Test
	@Order(3)
	void findV1()  {
		Fach f = fachRepository.findFachByName("DBI");

		System.out.println(f.getId());
		String json = gson.toJson(f);
		System.out.println(json);
	}

	@Test
	@Order(4)
	void findV2() {
		Fachv2 f = fachv2Repository.findFachv2ByName("Mathe");
		System.out.println(f.getId());
		String json = gson.toJson(f);
		System.out.println(json);
	}

	@Test
	@Order(5)
	void updateV1() {
		Fach f = fachRepository.findFachByName("DBI");
		Pruefer pruefer = new Pruefer("Prof. Niklas", true, List.of(LocalDateTime.now()), LocalDateTime.now());
		Dauer dauer = new Dauer(2, 30, LocalDateTime.now());
		MoeglichePruefungen pruefungen = new MoeglichePruefungen(List.of(pruefer), List.of(dauer), PruefArt.SCHRIFTLICH);

		f.getMoeglichePruefungen().add(pruefungen);

		f = fachRepository.save(f);

		System.out.println(gson.toJson(f));
	}
	
	@Test
	@Order(6)
	void updateV2() {
		Fachv2 f = fachv2Repository.findFachv2ByName("Mathe");
		Pruefer pruefer = new Pruefer("Prof. Niklas", true, List.of(LocalDateTime.now()), LocalDateTime.now());
		Dauer dauer = new Dauer(2, 30, LocalDateTime.now());
		MoeglichePruefungenV2 pruefungen = new MoeglichePruefungenV2(List.of(pruefer), List.of(dauer), PruefArt.SCHRIFTLICH);

		pruefungen = moeglichePruefungenV2Repository.save(pruefungen);

		f.getMoeglichePruefungen().add(pruefungen);

		f = fachv2Repository.save(f);

		System.out.println(gson.toJson(f));
	}

	@Test
	@Order(7)
	void deleteV1() {
		Fach f = fachRepository.findFachByName("DBI");

		fachRepository.delete(f);
	}

	@Test
	@Order(8)
	void deleteV2() {
		Fachv2 f = fachv2Repository.findFachv2ByName("Mathe");

		f.getMoeglichePruefungen().stream()
				.forEach(moeglichePruefungenV2 -> moeglichePruefungenV2Repository.delete(moeglichePruefungenV2));

		fachv2Repository.delete(f);
	}
}
