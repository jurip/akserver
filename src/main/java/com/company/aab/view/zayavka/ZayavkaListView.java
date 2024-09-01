package com.company.aab.view.zayavka;

import com.company.aab.entity.Zayavka;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "zayavkas", layout = MainView.class)
@ViewController("Zayavka.list")
@ViewDescriptor("zayavka-list-view.xml")
@LookupComponent("zayavkasDataGrid")
@DialogMode(width = "64em")
public class ZayavkaListView extends StandardListView<Zayavka> {

}