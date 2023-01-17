package eu.groeller.presentation;


import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import eu.groeller.domain.v1.Dauer;
import eu.groeller.domain.v1.Fach;
import eu.groeller.domain.v1.PruefArt;
import eu.groeller.domain.v1.Pruefer;
import eu.groeller.domain.v2.Fachv2;
import eu.groeller.domain.v2.MoeglichePruefungenV2;
import eu.groeller.repository.FachRepository;
import eu.groeller.repository.Fachv2Repository;
import eu.groeller.repository.MoeglichePruefungenV2Repository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Route("")
public class ListView extends VerticalLayout {


    private final Fachv2Repository fachv2Repository;
    private final FachRepository fachRepository;
    private final MoeglichePruefungenV2Repository moeglichePruefungenV2Repository;

    GridPro<Fachv2> grid;

    List<Fachv2> subjectList;

    public ListView(Fachv2Repository fachv2Repository, FachRepository fachRepository, MoeglichePruefungenV2Repository moeglichePruefungenV2Repository) {
        this.fachv2Repository = fachv2Repository;
        this.fachRepository = fachRepository;
        this.moeglichePruefungenV2Repository = moeglichePruefungenV2Repository;

        setSizeFull();

        configureGrid();

        Button addSubjectButton = new Button("Add Subject");
        addSubjectButton.addClickListener(click -> {
            this.subjectList.add(new Fachv2());
            grid.getDataProvider().refreshAll();
            grid.getDataProvider().refreshAll();
        });

        Button refreshButton = new Button("Fetch Data");
        refreshButton.addClickListener(buttonClickEvent -> {
            grid.getDataProvider().refreshAll();
        });

        HorizontalLayout layout = new HorizontalLayout();
        layout.add(addSubjectButton, refreshButton);
        add(layout, grid);
    }

    private void configureGrid() {
        grid = new GridPro<>();

        grid.addColumn(Fachv2::getName).setHeader("Subject Name");

        grid.addColumn(new ComponentRenderer<>(Button::new, (button, fach) -> {
            button.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_SUCCESS,
                    ButtonVariant.LUMO_TERTIARY);
            button.addClickListener(e -> {
                openDialog(fach);
            });
            button.setIcon(new Icon(VaadinIcon.EDIT));
        })).setHeader("Edit");

        grid.addColumn(new ComponentRenderer<>(Button::new, (button, fach) -> {
            button.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_ERROR,
                    ButtonVariant.LUMO_TERTIARY);
            button.addClickListener(e -> {
                subjectList.remove(fach);
                moeglichePruefungenV2Repository.deleteAll(fach.getMoeglichePruefungen());
                fachv2Repository.delete(fach);
                grid.getDataProvider().refreshAll();
                grid.getDataProvider().refreshAll();
            });
            button.setIcon(new Icon(VaadinIcon.TRASH));
        })).setHeader("Delete");

        this.subjectList = fachv2Repository.findAll();
        grid.setItems(subjectList);
    }

    public void openDialog(Fachv2 fach) {

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edit Subject");

        var defensiveCopyOfExaminers = new ArrayList<>( List.copyOf(fach.getMoeglichePruefungen()));

        TextField name = new TextField("Name of Subject");
        name.setValue(fach.getName() == null ? "": fach.getName());
        dialog.setWidth(35, Unit.PERCENTAGE);

        GridPro<MoeglichePruefungenV2> pruefungenGrid = new GridPro<MoeglichePruefungenV2>();

        pruefungenGrid.addEditColumn(MoeglichePruefungenV2::getExaminer).text(MoeglichePruefungenV2::setExaminer)
                .setHeader("Examiner")
                .setSortable(true);

        pruefungenGrid.addEditColumn(MoeglichePruefungenV2::getDauer).text(MoeglichePruefungenV2::setDauer)
                .setHeader("Duration");

        pruefungenGrid.addEditColumn(MoeglichePruefungenV2::getPruefArt).select(MoeglichePruefungenV2::setPruefArt, PruefArt.class)
                .setHeader("Exam Type");

        //pruefungenGrid.setItems(List.of(new MoeglichePruefungenV2(List.of(new Pruefer("kek2", true, List.of(LocalDateTime.now()),LocalDateTime.now())),
          //String       List.of(new Dauer(5, 5,LocalDateTime.now())),PruefArt.SCHRIFTLICH)));
        pruefungenGrid.setItems(defensiveCopyOfExaminers);

        pruefungenGrid.addColumn(new ComponentRenderer<>(Button::new, (button, pruefungenV2) -> {
            button.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_ERROR,
                    ButtonVariant.LUMO_TERTIARY);
            button.addClickListener(e -> {
                defensiveCopyOfExaminers.remove(pruefungenV2);

                pruefungenGrid.getDataProvider().refreshAll();
                pruefungenGrid.getDataProvider().refreshAll();
            });
            button.setIcon(new Icon(VaadinIcon.TRASH));
        })).setHeader("Delete");

        Button addPruefung = new Button("Add Examiner");
        addPruefung.addClickListener(click -> {
            var newPruef = new MoeglichePruefungenV2(List.of(), List.of(), null);
            defensiveCopyOfExaminers.add(newPruef);
            pruefungenGrid.getDataProvider().refreshAll();
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button saveButton = new Button("Save");
        saveButton.addClickListener(click -> {
            fach.setName(name.getValue());
            var savedExams = moeglichePruefungenV2Repository.saveAll(defensiveCopyOfExaminers);
            fach.setMoeglichePruefungen(savedExams);
            fachv2Repository.save(fach);
            dialog.close();

            grid.getDataProvider().refreshAll();
        });

        checkNullValue(name, saveButton);

        name.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
            checkNullValue(name,saveButton);
        });

        Button discardButton = new Button("Discard");
        discardButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        discardButton.addClickListener(click -> dialog.close());

        horizontalLayout.add(discardButton, saveButton);

        dialog.add(name, addPruefung, pruefungenGrid, horizontalLayout);

        dialog.setCloseOnOutsideClick(false);
        dialog.open();
    }

    private void checkNullValue(TextField name, Button saveButton) {
        if(name.getValue().isBlank()) {
            saveButton.setEnabled(false);
        }
        else
            saveButton.setEnabled(true);
    }
}
