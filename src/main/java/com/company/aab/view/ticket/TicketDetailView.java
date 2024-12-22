package com.company.aab.view.ticket;

import com.company.aab.entity.Ticket;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.session.SessionData;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.company.aab.app.FlutterServiceBean.sendToBpium;

@Route(value = "tickets/:id", layout = MainView.class)
@ViewController("Ticket.detail")
@ViewDescriptor("ticket-detail-view.xml")
@EditedEntityContainer("ticketDc")
public class TicketDetailView extends StandardDetailView<Ticket> {
    @ViewComponent
    private InstanceContainer<Ticket> ticketDc;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Notifications notifications;
    @Autowired
    private SessionData sessionData;

    @Subscribe(id = "send", subject = "singleClickListener")
    public void onSendClick(final ClickEvent<JmixButton> event) {
        Ticket t = ticketDc.getItem();

        if (t.getUsername() == null) {

            Object n =  sessionData.getAttribute("username");
            if (n != null) {
                t.setUsername(n.toString());

            }

        }

        t = dataManager.save(t);
        if ("avtokonnekt".equals(t.getTenantAttribute())) {
            boolean sent = sendToBpium("https://autoconnect.bpium.ru/api/webrequest/tiket?async=true",new TR(t));
            if(sent) {
                notifications.create("Отправлено").withType(Notifications.Type.WARNING)
                        .withPosition(Notification.Position.TOP_CENTER)
                        .withDuration(3000)
                        .show();;
            }else{
                notifications.create("Не удалось отправить").withType(Notifications.Type.WARNING)
                        .withPosition(Notification.Position.TOP_CENTER)
                        .withDuration(3000)
                        .show();;
            }
        }
    }
    @Autowired
    private Downloader downloader;
    @Subscribe(id = "Download", subject = "clickListener")
    public void onDownloadClick(final ClickEvent<JmixButton> event) {
        FileRef fileRef =  ticketDc.getItem().getFile();
        downloader.download(fileRef);
    }

   
}

class TR{
    public TR(Ticket t){
      this.file = t.getFile().toString();
      this.kontakt = t.getKontakt();
      this.opisanie = t.getOpisanie();
      this.klient = t.getKlient();
      this.username = t.getUsername();
    }
    private String klient;
    private String kontakt;
    private String opisanie;
    private String file;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKlient() {
        return klient;
    }

    public String getKontakt() {
        return kontakt;
    }

    public String getOpisanie() {
        return opisanie;
    }

    public String getFile() {
        return file;
    }

    public void setKlient(String klient) {
        this.klient = klient;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public void setOpisanie(String opisanie) {
        this.opisanie = opisanie;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
