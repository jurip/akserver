package com.company.aab.view.oborudovanie;

import com.company.aab.entity.Oborudovanie;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "oborudovanies/:id", layout = MainView.class)
@ViewController("Oborudovanie.detail")
@ViewDescriptor("oborudovanie-detail-view.xml")
@EditedEntityContainer("oborudovanieDc")
public class OborudovanieDetailView extends StandardDetailView<Oborudovanie> {
}