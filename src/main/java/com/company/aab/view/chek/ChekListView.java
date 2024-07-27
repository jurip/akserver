package com.company.aab.view.chek;

import com.company.aab.entity.Chek;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "cheks", layout = MainView.class)
@ViewController("Chek.list")
@ViewDescriptor("chek-list-view.xml")
@LookupComponent("cheksDataGrid")
@DialogMode(width = "64em")
public class ChekListView extends StandardListView<Chek> {
}