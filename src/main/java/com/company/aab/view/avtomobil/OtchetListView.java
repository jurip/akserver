package com.company.aab.view.avtomobil;

import com.company.aab.entity.Avtomobil;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "otchets", layout = MainView.class)
@ViewController(id = "Otchet.list")
@ViewDescriptor(path = "otchet-list-view.xml")
@LookupComponent("avtomobilsDataGrid")
@DialogMode(width = "64em")
public class OtchetListView extends StandardListView<Avtomobil> {
    @Autowired
    private ViewNavigators viewNavigators;
    Avtomobil selected;

    @Subscribe
    public void onInit(final InitEvent event) {
        avtomobilsDataGrid.addSelectionListener(new SelectionListener<Grid<Avtomobil>, Avtomobil>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<Avtomobil>, Avtomobil> selectionEvent) {
                if(selectionEvent.getFirstSelectedItem().isPresent())
              selected =   selectionEvent.getFirstSelectedItem().get();
            }
        });
        avtomobilsDataGrid.addItemDoubleClickListener(new ComponentEventListener<ItemDoubleClickEvent<Avtomobil>>() {
            @Override
            public void onComponentEvent(ItemDoubleClickEvent<Avtomobil> avtomobilItemDoubleClickEvent) {
                open(avtomobilItemDoubleClickEvent.getItem());
            }
        });
    }
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
    @ViewComponent
    private DataGrid<Avtomobil> avtomobilsDataGrid;
    private void  open(Avtomobil a){
        viewNavigators.detailView(this, Avtomobil.class).withViewClass(OtchetDetailView.class).editEntity(a).navigate();
    }

    @Subscribe(id = "editButton", subject = "clickListener")
    public void onSelectButtonClick(final ClickEvent<JmixButton> event) {

        viewNavigators.detailView(this, Avtomobil.class).withViewClass(OtchetDetailView.class).editEntity(selected).navigate();
    }
}