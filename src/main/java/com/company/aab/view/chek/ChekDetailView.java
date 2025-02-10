package com.company.aab.view.chek;

import com.company.aab.entity.Chek;
import com.company.aab.entity.ChekFoto;
import com.company.aab.view.main.MainView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.FileStorage;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Route(value = "cheks/:id", layout = MainView.class)
@ViewController("Chek.detail")
@ViewDescriptor("chek-detail-view.xml")
@EditedEntityContainer("chekDc")
public class ChekDetailView extends StandardDetailView<Chek> {
    @ViewComponent
    private Html html;
    @ViewComponent
    private TypedTextField<Object> company;

    @Subscribe(id = "showChek", subject = "clickListener")
    public void onShowChekClick(final ClickEvent<JmixButton> event) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "token=31088.nYAffWRLIcxex3cjK&qrraw=t=20200924T1837%26s=349.93%26fn=9282440300682838%26i=46534%26fp=1273019065%26n=1");
        Request request = new Request.Builder()
                .url("https://proverkacheka.com/api/v1/check/get")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")

                .addHeader("Cookie", "ENGID=1.1")
                .build();
        String response = client.newCall(request).execute().body().string();
        JsonObject convertedObject = new Gson().fromJson(response, JsonObject.class);
        JsonObject t = convertedObject.getAsJsonObject("data").getAsJsonObject("json");

        company.setValue(t.get("user").toString());
    }
    @Autowired
    private DialogWindows dialogWindows;

    private void openViewWithParams(String username) {
        DialogWindow<MyOnboardingView> window =
                dialogWindows.view(this, MyOnboardingView.class).build();
        window.getView().setHtml(username);
        window.open();
    }
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private FileStorage fileStorage;

    @Supply(to = "fotosDataGrid.file", subject = "renderer")
    private Renderer<ChekFoto> fotosDataGridFileRenderer() {
        return new ComponentRenderer<>(userStep -> {
            Image image = uiComponents.create(Image.class);

            StreamResource streamResource = new StreamResource(
                    userStep.getFile() .getFileName(),
                    () -> fileStorage.openStream(userStep.getFile()));
            image.setSrc(streamResource);
            image.setHeight(150, Unit.PIXELS);

            return image;
        });
    }
}