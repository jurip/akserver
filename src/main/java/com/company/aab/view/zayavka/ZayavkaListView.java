package com.company.aab.view.zayavka;

import com.company.aab.entity.Foto;
import com.company.aab.entity.Zayavka;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.Metadata;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "zayavkas", layout = MainView.class)
@ViewController("Zayavka.list")
@ViewDescriptor("zayavka-list-view.xml")
@LookupComponent("zayavkasDataGrid")
@DialogMode(width = "64em")
public class ZayavkaListView extends StandardListView<Zayavka> {


    @Autowired
    protected Metadata metadata;
    @ViewComponent
    private DataGrid<Zayavka> zayavkasDataGrid;

    @Install(to = "zayavkasDataGrid.user", subject = "partNameGenerator")
    protected String gradePartNameGenerator(final Zayavka customer) {
        return getStyle(customer);
    }
    @Install(to = "zayavkasDataGrid.client", subject = "partNameGenerator")
    protected String gradePartNameGenerator2(final Zayavka customer) {
        return getStyle(customer);
    }
    @Install(to = "zayavkasDataGrid.nomer", subject = "partNameGenerator")
    protected String gradePartNameGenerator3(final Zayavka customer) {
        return getStyle(customer);
    }
    @Install(to = "zayavkasDataGrid.nachalo", subject = "partNameGenerator")
    protected String gradePartNameGenerator4(final Zayavka customer) {
        return getStyle(customer);
    }

   @Subscribe
    public void onInit(InitEvent event) {
        zayavkasDataGrid.addColumn(new TextRenderer<>
                        (Zayavka::getText))
                .setHeader("статус").setPartNameGenerator(this::gradePartNameGenerator4);
    }
    private static String getStyle(Zayavka customer) {
        if (customer.getStatus() == null) {
            return null;
        }
        return switch (customer.getStatus()) {
            case "NOVAYA" -> "high-grade";
            case "VYPOLNENA" -> "standard-grade";
            case "OTMENA" -> "premium-grade";
            default -> "error-grade";
        };
    }
}