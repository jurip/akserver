package com.company.aab.view.avtomobil;

import com.company.aab.entity.Avtomobil;
import com.company.aab.entity.Zayavka;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.data.renderer.TextRenderer;
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

    @Subscribe
    public void onInit(InitEvent event) {
      /*  avtomobilsDataGrid.addColumn(new TextRenderer<>
                        (Avtomobil::getText))
                .setHeader("статус").setPartNameGenerator(this::gradePartNameGenerator4);*/
    }
   /* @Install(to = "avtomobilsDataGrid.username", subject = "partNameGenerator")
    protected String gradePartNameGenerator5(final Avtomobil customer) {
        return getStyle(customer);
    }*/
   // @Install(to = "avtomobilsDataGrid.zayavka.nomer", subject = "partNameGenerator")
    //protected String gradePartNameGenerator2(final Avtomobil customer) {
  //      return getStyle(customer);
   // }
    /*@Install(to = "avtomobilsDataGrid.nomer", subject = "partNameGenerator")
    protected String gradePartNameGenerator3(final Avtomobil customer) {
        return getStyle(customer);
    }
    @Install(to = "avtomobilsDataGrid.marka", subject = "partNameGenerator")
    protected String gradePartNameGenerator4(final Avtomobil customer) {
        return getStyle(customer);
    }
    @Install(to = "avtomobilsDataGrid.date", subject = "partNameGenerator")
    protected String gradePartNameGenerator6(final Avtomobil customer) {
        return getStyle(customer);
    }
*/
    private static String getStyle(Avtomobil customer) {
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