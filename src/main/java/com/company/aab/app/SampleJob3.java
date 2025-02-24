package com.company.aab.app;

import io.jmix.core.DataManager;
import io.jmix.core.entity.KeyValueEntity;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.reports.runner.ReportRunner;
import io.jmix.reports.yarg.reporting.ReportOutputDocument;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class SampleJob3 implements Job {

    private static final Logger log = LoggerFactory.getLogger(SampleJob3.class);
    private static final String CHAT_ID = "-4740599753" ;
    private static final String BOT_TOKEN = "7332996600:AAEysrKvQKQDMyu6zkOccJHvYOuEvv37ofo";
    private final DataManager dataManager;
    @Autowired
    private ReportRunner reportRunner;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private SystemAuthenticator systemAuthenticator;

    public SampleJob3(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @ManagedOperation
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Пример работы");
        String doc =
        systemAuthenticator.withUser("admin", () -> {
            UserDetails user = currentAuthentication.getUser();
            System.out.println("User: " + user.getUsername()); // admin
            // ...
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startYesterday = now.minusDays(1).with(LocalTime.MIN); // 2020-03-23T00:00
            LocalDateTime startToday = now.with(LocalTime.MIN);
            Date start = Date.from(startYesterday.atZone(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(startToday.atZone(ZoneId.systemDefault()).toInstant());

            List<KeyValueEntity> kvEntities = dataManager.loadValues(
                            "select " +
                                    "e.nomer, " +
                                    "e.marka,  " +
                                    "zayavka.nomer, " +
                                    "zayavka_user.firstName, " +
                                    "zayavka_user.lastName, " +
                                    "zayavka_user.username " +
                                    "from Avtomobil e " +
                                    "left join e.zayavka zayavka " +
                                    "left join e.zayavka.user zayavka_user " +
                                    "where e.date > :start and e.date < :end  " +
                                    "order by zayavka_user.username, zayavka.nomer "
                                   )
                    .store("main")
                    .properties("nomer","marka","nomer_zayavki", "firstName","lastName",  "username")
                    .parameter("start", start)
                    .parameter("end", end)
                    .list();

            StringBuilder sb = new StringBuilder();
            sb.append("Сводка за ");

            sb.append(DateFormatUtils.format(start, "yyyy-MM-dd"));
            sb.append("%0A");

            String curUser = "";
            String curZ = "";
            int sum = 1;
            Iterator<KeyValueEntity> i = kvEntities.iterator();
            KeyValueEntity curkvEntity = i.next();
            KeyValueEntity next = i.next();
            sb.append("%0AМонтажник: ");
            String u = curkvEntity.getValue("firstName")+" "+curkvEntity.getValue("lastName");
            sb.append(u) .append("%0A").append("Отправлено в Бипиум :%0A");
            sb.append("Номер заявки: ").append(curkvEntity.getValue("nomer_zayavki").toString());
            sb.append("%0A");
            sb.append(curkvEntity.getValue("marka").toString());
            sb.append(" ").append(curkvEntity.getValue("nomer").toString()).append("%0A");
            do  {
                boolean isSameUser = StringUtils.equals(curkvEntity.getValue("username"), next.getValue("username"));
                boolean isSameZayavka = StringUtils.equals(curkvEntity.getValue("nomer_zayavki"), next.getValue("nomer_zayavki"));
                if(!isSameZayavka) {
                    sb.append("Отчетов: ").append(sum).append("%0A");
                    sum = 0;
                }
                if(!isSameUser){
                    sb.append("%0AМонтажник: ");
                     u = next.getValue("firstName")+" "+next.getValue("lastName");
                    sb.append(u) .append("%0A");
                }
                if(!isSameZayavka){
                       sb.append("Номер заявки: ").append(curkvEntity.getValue("nomer_zayavki").toString());
                       sb.append("%0A");
                   }



                sb.append(curkvEntity.getValue("marka").toString());
                sb.append(" ").append(curkvEntity.getValue("nomer").toString()).append("%0A");
                curkvEntity = next;
                next = i.next();
                sum = sum +1;

            } while (i.hasNext());
            sb.append("Отчетов: ").append(sum).append("%0A");


            sendTextToTelegram(sb.toString());



            return sb.toString();
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
    public static  void sendTextToTelegram(String text){
        OkHttpClient client = new OkHttpClient();
        //fileUrl = "fs://2025/02/16/7ad3db9e-01fc-f523-dd40-fded71b7d376.jpg?name=scaled_21dd4921-435f-4080-a23b-aa41eeb1c1ef3344823467500867828.jpg";
        MediaType mediaType = MediaType.parse("application/json");

        Request request = new Request.Builder()
                .url("https://api.telegram.org/bot"+BOT_TOKEN+"/sendMessage?chat_id="+CHAT_ID+"&text=\""+text+"\"")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            ;
            log.info("Otwet ii:code {}\n telo {}",response.code(), response.body().toString());

        } catch (IOException e) {
            log.error("Error:{}",e);
        }
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
