package com.company.aab.app;

import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.reports.runner.ReportRunner;
import io.jmix.reports.yarg.reporting.ReportOutputDocument;
import okhttp3.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class SampleJob2 implements Job {

    private static final Logger log = LoggerFactory.getLogger(SampleJob2.class);
    private static final String CHAT_ID = "-4740599753" ;
    private static final String BOT_TOKEN = "7332996600:AAEysrKvQKQDMyu6zkOccJHvYOuEvv37ofo";
    @Autowired
    private ReportRunner reportRunner;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private SystemAuthenticator systemAuthenticator;
    @ManagedOperation
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Пример работы");
        ReportOutputDocument doc =
        systemAuthenticator.withUser("admin", () -> {
            UserDetails user = currentAuthentication.getUser();
            System.out.println("User: " + user.getUsername()); // admin
            // ...
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startYesterday = now.minusDays(1).with(LocalTime.MIN); // 2020-03-23T00:00
            LocalDateTime endToday       = now.with(LocalTime.MAX); // 2020-03-24T23:59:59.999999999
            ReportOutputDocument document = reportRunner.byReportCode("test2")
                    .addParam("start", startYesterday )
                    .addParam("end", endToday )
                    .run();

            sendFileToTelegram(document.getContent());



            return document;
        });

        /*doc.getContent();

        OkHttpClient client = new OkHttpClient();
        //fileUrl = "fs://2025/02/16/7ad3db9e-01fc-f523-dd40-fded71b7d376.jpg?name=scaled_21dd4921-435f-4080-a23b-aa41eeb1c1ef3344823467500867828.jpg";
        MediaType mediaType = MediaType.parse("application/json");

        Request request = new Request.Builder()
                .url("https://api.telegram.org/bot7332996600:AAEysrKvQKQDMyu6zkOccJHvYOuEvv37ofo/sendMessage?chat_id=-4715545526&text=Hello+World!")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            ;
            log.info("Otwet ii:code {}\n telo {}",response.code(), response.body().toString());

        } catch (IOException e) {
            log.error("Error:{}",e);
        }
*/
    }

    public static void sendFileToTelegram(byte[] file) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("chat_id", CHAT_ID)
                .addFormDataPart("document", "отчет.xlsx",
                        RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .build();

        Request request = new Request.Builder()
                .url("https://api.telegram.org/bot" + BOT_TOKEN + "/sendDocument")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println("File sent successfully: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
