package com.company.aab.view.zayavka;

import com.company.aab.app.AdresService;
import com.company.aab.app.FcmSender;
import com.company.aab.entity.Kontragent;
import com.company.aab.entity.User;
import com.company.aab.entity.Zayavka;
import com.company.aab.view.main.MainView;
import com.kuliginstepan.dadata.client.domain.Suggestion;
import com.kuliginstepan.dadata.client.domain.address.Address;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.valuepicker.EntityPicker;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.util.Date;

@Route(value = "zayavkas/:id", layout = MainView.class)
@ViewController("Zayavka.detail")
@ViewDescriptor("zayavka-detail-view.xml")
@EditedEntityContainer("zayavkaDc")
public class ZayavkaDetailView extends StandardDetailView<Zayavka> {
    @ViewComponent
    private InstanceContainer<Zayavka> zayavkaDc;
    @ViewComponent
    private EntityPicker<User> userField;
    @ViewComponent
    private FormLayout form;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private TypedTextField<String> nomerField;
    @ViewComponent
    private EntityPicker<Kontragent> kontragentField;
    @Autowired
    private AdresService adresService;
    @ViewComponent
    private TypedTextField<String> adresField;
    @Autowired
    private ViewNavigators viewNavigators;

    @Subscribe(id = "send", subject = "singleClickListener")
    public void onSendClick(final ClickEvent<JmixButton> event) {

        userField.executeValidators();
        nomerField.executeValidators();
        kontragentField.executeValidators();
        if(userField.isInvalid()||nomerField.isInvalid()||kontragentField.isInvalid())return;
        Zayavka z = zayavkaDc.getItem();
        if(z.getNachalo()==null){
            z.setNachalo(new Date());
        }
        if(z.getEnd_date_time()==null){
            z.setEnd_date_time(new Date());
        }
        if(z.getStatus()==null){
            z.setStatus("NOVAYA");
        }

        z.setClient(z.getKontragent().getNazvanie());
        z.setUsername(z.getUser().getUsername());
        dataManager.save(z);
        if(z.getUser().getFcmRegistrationToken()!=null){

            FcmSender.sendMessageToApp(z.getUser().getFcmRegistrationToken(),z);
            viewNavigators.listView(Zayavka.class).navigate();

        }

    }

    @Subscribe("adresField")
    public void onAdresFieldComponentValueChange(final AbstractField.ComponentValueChangeEvent<TypedTextField<String>, String> event) {

        Suggestion<Address> z = adresService.getSuggestionsForAddress(event.getValue()).blockFirst();

        if(z!=null)
         adresField.setValue(z.getValue());
    }
}