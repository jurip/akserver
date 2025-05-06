package com.company.aab.view.calendar;


import com.company.aab.entity.Zayavka;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.Messages;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import io.jmix.fullcalendarflowui.component.FullCalendar;
import io.jmix.fullcalendarflowui.component.data.EntityCalendarEvent;
import io.jmix.fullcalendarflowui.component.event.EventClickEvent;
import io.jmix.fullcalendarflowui.kit.component.model.CalendarDisplayModes;
import org.springframework.beans.factory.annotation.Autowired;

import static io.jmix.fullcalendarflowui.kit.component.model.CalendarDisplayModes.DAY_GRID_MONTH;

@Route(value = "calendar-view", layout = MainView.class)
@ViewController(id = "CalendarView")
@ViewDescriptor(path = "calendar-view.xml")
public class CalendarView extends StandardView {
    @ViewComponent
    private JmixComboBox<CalendarDisplayModes> displayModesBox;
    @ViewComponent
    private FullCalendar calendar;

    @Autowired
    private Messages messages;
    @Autowired
    private Notifications notifications;
    @Autowired
    private ViewNavigators viewNavigators;

    @Subscribe
    public void onInit(final InitEvent event) {
        displayModesBox.setItems(CalendarDisplayModes.values());
        displayModesBox.setItemLabelGenerator(messages::getMessage);
        displayModesBox.setValue(DAY_GRID_MONTH);

        displayModesBox.addValueChangeListener(e ->
                calendar.setCalendarDisplayMode(e.getValue() == null ? DAY_GRID_MONTH : e.getValue()));
    }
    @Subscribe("calendar")
    public void onCalendarEventClick(final EventClickEvent event) {

        viewNavigators.detailView(this, Zayavka.class).editEntity(

                (Zayavka)(
                        (EntityCalendarEvent)event.getCalendarEvent()).getEntity()).navigate();
    }
}