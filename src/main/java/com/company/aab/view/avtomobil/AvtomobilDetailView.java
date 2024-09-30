package com.company.aab.view.avtomobil;

import com.company.aab.entity.Avtomobil;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "avtomobils/:id", layout = MainView.class)
@ViewController("Avtomobil.detail")
@ViewDescriptor("avtomobil-detail-view.xml")
@EditedEntityContainer("avtomobilDc")
public class AvtomobilDetailView extends StandardDetailView<Avtomobil> {
}