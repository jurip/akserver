package com.company.aab.view.usluga;

import com.company.aab.entity.Usluga;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "uslugas/:id", layout = MainView.class)
@ViewController("Usluga.detail")
@ViewDescriptor("usluga-detail-view.xml")
@EditedEntityContainer("uslugaDc")
public class UslugaDetailView extends StandardDetailView<Usluga> {
}