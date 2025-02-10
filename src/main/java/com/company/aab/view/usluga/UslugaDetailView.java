package com.company.aab.view.usluga;

import com.company.aab.entity.Usluga;
import com.company.aab.entity.Zayavka;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "uslugas/:id", layout = MainView.class)
@ViewController("Usluga.detail")
@ViewDescriptor("usluga-detail-view.xml")
@EditedEntityContainer("uslugaDc")
public class UslugaDetailView extends StandardDetailView<Usluga> {
    @Autowired
    private Sequences sequences;
    @Subscribe
    public void onInitEntity(final InitEntityEvent<Usluga> event) {
        if(event.getEntity().getCode()==null){
            Long number = sequences.createNextValue(Sequence.withName(event.getEntity().getTenantAttribute()+"usluga_number")
                    .setStartValue(1)
                    .setIncrement(1));
            event.getEntity().setCode(String.valueOf(number));
        }
    }
}