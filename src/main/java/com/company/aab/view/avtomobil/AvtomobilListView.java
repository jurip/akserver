package com.company.aab.view.avtomobil;

import com.company.aab.entity.Avtomobil;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.Metadata;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "avtomobils", layout = MainView.class)
@ViewController("Avtomobil.list")
@ViewDescriptor("avtomobil-list-view.xml")
@LookupComponent("avtomobilsDataGrid")
@DialogMode(width = "64em")
public class AvtomobilListView extends StandardListView<Avtomobil> {
    @ViewComponent
    private DataGrid<Avtomobil> avtomobilsDataGrid;
    @Autowired
    protected Metadata metadata;
    @Install(to = "avtomobilsDataGrid.status", subject = "partNameGenerator")
    protected String gradePartNameGenerator(final Avtomobil customer) {
        if (customer.getStatus() == null) {
            return null;
        }
        return switch (customer.getStatus()) {
            case "NOVAYA" -> "high-grade";
            case "HIGH" -> "high-grade";
            case "PREMIUM" -> "premium-grade";
            default -> "standard-grade";
        };
    }
}