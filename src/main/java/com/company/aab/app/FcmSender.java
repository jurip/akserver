package com.company.aab.app;

import com.company.aab.entity.Zayavka;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

public class FcmSender {

  public static void sendMessageToApp(String token, Zayavka zayavka) throws Exception {
    String registrationToken = token;
    Message message =
        Message.builder()
            .putData("id", zayavka.getId().toString())
            .putData("message", zayavka.getMessage())
                .putData("adres", zayavka.getAdres())
                .putData("nomer", zayavka.getNomer())
            .setNotification(
                Notification.builder()
                    .setTitle("Новая заявка")
                    .setBody("Заявка на адрес "+zayavka.getAdres())
                    .build())
            .setToken(registrationToken)
            .build();

    FirebaseMessaging.getInstance().send(message);
    
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