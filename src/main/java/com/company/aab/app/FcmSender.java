package com.company.aab.app;

import com.company.aab.entity.Avtomobil;
import com.company.aab.entity.Zayavka;
import com.github.javaparser.utils.Log;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FcmSender {


  public static void sendMessageToApp(String token,Zayavka zayavka)  {
    sendMessageToApp(token, ZayavkaDTO.getFrom(zayavka));
  }

  public static void sendMessageToApp(String token,ZayavkaDTO zayavka) {

    List<Avto> as = zayavka.getAvtomobili();
    

    String pattern = "yyyy-MM-dd HH:mm:ss";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
    DateFormat df = new SimpleDateFormat(pattern);

    Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();


    Message message =
        Message.builder()

            .putData("id", zayavka.getId().toString())
            .putData("message", zayavka.getMessage()!=null?zayavka.getMessage():"")
                .putData("adres", zayavka.getAdres()!=null?zayavka.getAdres():"")
                .putData("nomer", zayavka.getNomer()!=null?zayavka.getNomer():"")

                .putData("nachalo", df.format(zayavka.getNachalo()!=null? zayavka.getNachalo():new Date()))
                .putData("end_date_time", df.format(zayavka.getEnd_date_time()!=null?zayavka.getEnd_date_time():new Date()))
                .putData("comment_address",zayavka.getComment_address()!=null? zayavka.getComment_address():"")
                .putData("service", zayavka.getService()!=null?zayavka.getService():"")
                .putData("client", zayavka.getClient()!=null?zayavka.getClient():"")
                .putData("contact_name", zayavka.getContact_name()!=null?zayavka.getContact_name():"")
                .putData("contact_number", zayavka.getContact_number()!=null?zayavka.getContact_number():"")
                .putData("manager_name", zayavka.getManager_name()!=null?zayavka.getManager_name():"")
                .putData("manager_number", zayavka.getManager_number()!=null?zayavka.getManager_number():"")
                .putData("lat", zayavka.getLat()!=null?zayavka.getLat():"")
                .putData("lng", zayavka.getLng()!=null?zayavka.getLng():"")
                .putData("avtomobili", g.toJson( zayavka.getAvtomobili()!=null
                        ?zayavka.getAvtomobili():List.of()))




            .setNotification(
                Notification.builder()
                    .setTitle("Новая заявка")
                    .setBody("Заявка на адрес "+zayavka.getAdres())
                    .build())
            .setToken(token)
            .build();
    try {
      String result = FirebaseMessaging.getInstance().send(message);
    }catch (Exception e){
      Log.error(e);
    }

    
  }

  private static void sendMessageToFcmTopic() throws Exception {
    String topicName = "app_promotion";

    Message message =
        Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle("A new app is available")
                    .setBody("Check out our latest app in the app store.")
                    .build())
            .setAndroidConfig(
                AndroidConfig.builder()
                    .setNotification(
                        AndroidNotification.builder()
                            .setTitle("A new Android app is available")
                            .setBody("Our latest app is available on Google Play store")
                            .build())
                    .build())
            .setTopic("app_promotion")
            .build();

    FirebaseMessaging.getInstance().send(message);

    System.out.println("Message to topic sent successfully!!");
  }


}