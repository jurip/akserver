package com.company.aab.view.charts;


import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "charts-view", layout = MainView.class)
@ViewController(id = "ChartsView")
@ViewDescriptor(path = "charts-view.xml")
public class ChartsView extends StandardView {
}