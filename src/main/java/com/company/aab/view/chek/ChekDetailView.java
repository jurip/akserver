package com.company.aab.view.chek;

import com.company.aab.entity.Chek;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "cheks/:id", layout = MainView.class)
@ViewController("Chek.detail")
@ViewDescriptor("chek-detail-view.xml")
@EditedEntityContainer("chekDc")
public class ChekDetailView extends StandardDetailView<Chek> {
}