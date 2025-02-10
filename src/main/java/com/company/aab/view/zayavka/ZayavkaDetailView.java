package com.company.aab.view.zayavka;

import com.company.aab.app.*;
import com.company.aab.entity.Kontragent;
import com.company.aab.entity.User;
import com.company.aab.entity.Zayavka;
import com.company.aab.view.main.MainView;
import com.kuliginstepan.dadata.client.domain.Suggestion;
import com.kuliginstepan.dadata.client.domain.address.Address;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.valuepicker.EntityPicker;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Stream;

@Route(value = "zayavkas/:id", layout = MainView.class)
@ViewController("Zayavka.detail")
@ViewDescriptor("zayavka-detail-view.xml")
@EditedEntityContainer("zayavkaDc")
public class ZayavkaDetailView extends StandardDetailView<Zayavka> {
    @Subscribe
    public void onValidation(final ValidationEvent event) {
        Zayavka z =  zayavkaDc.getItem();
    }
  
    @ViewComponent
    private InstanceContainer<Zayavka> zayavkaDc;
    @ViewComponent
    private EntityPicker<User> userField;
    @ViewComponent
    protected ComboBox<String> adresField;



    @Subscribe("nachaloField")
    public void onNachaloFieldComponentValueChange(final AbstractField.ComponentValueChangeEvent<TypedDateTimePicker<Comparable>, Comparable<?>> event) {
        LocalDateTime start = nachaloField.getValue();
        if(start==null)
            return;
        if(end_date_timeField.getValue()==null){
            end_date_timeField.setValue(start);
        }
    }
    @ViewComponent
    private TypedDateTimePicker<Comparable> end_date_timeField;
    @ViewComponent
    private TypedDateTimePicker<Comparable> nachaloField;
    @ViewComponent
    private TypedTextField<String> manager_nameField;
    @ViewComponent
    private TypedTextField<String> contact_numberField;
    @ViewComponent
    private TypedTextField<String> contact_nameField;
    @ViewComponent
    private TypedTextField<String> manager_numberField;


    @Subscribe("kontragentField")
    public void onKontragentFieldComponentValueChange(final AbstractField.ComponentValueChangeEvent<EntityPicker<Kontragent>, Kontragent> event) {
        Kontragent k = kontragentField.getValue();
        if(k==null)
            return;
        if(zayavkaDc.getItem().getAdres()==null){
            adresField.setValue(k.getAdres()!=null?k.getAdres():"");
        }
        contact_nameField.setValue(k.getContact_name()!=null?k.getContact_name():"");
        contact_numberField.setValue(k.getContact_number()!=null?k.getContact_number():"");
        manager_nameField.setValue(k.getManager_name()!=null?k.getManager_name():"");
        manager_numberField.setValue(k.getManager_number()!=null?k.getManager_number():"");

    }
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
        //z = dataManager.save(z);
        zayavkaDc.setItem(z);
        if(z.getUser().getFcmRegistrationToken()!=null){

            FcmSender.sendMessageToApp(z.getUser().getFcmRegistrationToken(),zayavkaDc.getItem());
            viewNavigators.listView(this, Zayavka.class).navigate();

        }

    }

    @Autowired
    private Sequences sequences;


    @Subscribe
    public void onInitEntity(final InitEntityEvent<Zayavka> event) {
        if(event.getEntity().getNomer()==null){
            Long number = sequences.createNextValue(Sequence.withName(event.getEntity().getTenantAttribute()+"zayavka_number")
                    .setStartValue(1)
                    .setIncrement(1));
            event.getEntity().setNomer(String.valueOf(number));
        }
    }

    @Subscribe
    public void onInit(final InitEvent event) {

    }
    @Install(to = "adresField", subject = "itemsFetchCallback")
    private Stream<String> programmaticComboBoxItemsFetchCallback(Query<Suggestion<Address>, String> query) {
        String enteredValue = query.getFilter()
                .orElse("");

        return adresService.getSuggestionsForAddress(enteredValue)
                .toStream().map(addressSuggestion -> addressSuggestion.getValue())

                .skip(query.getOffset())
                .limit(query.getLimit())
                ;


    }


}