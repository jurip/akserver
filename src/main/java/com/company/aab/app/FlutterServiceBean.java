package com.company.aab.app;

import com.company.aab.entity.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jmix.core.*;
import io.jmix.core.security.UserRepository;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service("flutterService")
public class FlutterServiceBean {

    private static final Logger log = LoggerFactory.getLogger(FlutterServiceBean.class);
    public static final String AVTOKONNEKT = "avtokonnekt";
    public static final String INFOGRAF = "infograf";
    private final DataManager dataManager;
    @Value("${bipium}")
    private String bipiumPath;
    @Value("${bipiumtest}")
    private String bipiumtestPath;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public FlutterServiceBean(DataManager dataManager, EntityImportExport entityImportExport) {
        this.dataManager = dataManager;
        this.entityImportExport = entityImportExport;
    }

    private final EntityImportExport entityImportExport;

    public String createUser(String username,String firstName, String lastName, String password){

        User u = dataManager.create(User.class);
        u.setUsername(INFOGRAF+"|"+username);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setPassword(passwordEncoder.encode(password) );
        u.setTenant(INFOGRAF);
        u.setActive(true);
        dataManager.save(u);
        log.info("User created: {}", u);
        return u.getUsername();
    }
    public String changePassword(String username, String password){
        User u = dataManager.load(User.class).query("select u from User u where u.username = :username")
                .parameter("username", username).one();
        u.setPassword(passwordEncoder.encode(password));
        dataManager.save(u);
        return "ok";
    }

    public String login(String username, String password){
        UserDetails userDetails;
        try{
         userDetails= userRepository.loadUserByUsername(username);
        }catch(Exception e){
            return "no";
        }

            if(passwordEncoder.matches(password, userDetails.getPassword()))
                 return "ok";

        return "no";
    }
    public String updateUser(String username, String token){
        User u = dataManager.load(User.class).query("select u from User u where u.username = :username")
                        .parameter("username", username).one();
        u.setFcmRegistrationToken(token);
        dataManager.save(u);
        log.info("User updated:{}", u);
        return "ok";
    }
    public Zayavka sendZayavkaUpdate(Zayavka zayavka){
        Zayavka z = null;
        if(zayavka.getId()!=null){
            z = dataManager.load(Zayavka.class).id(zayavka.getId()).one();
            if(zayavka.getStatus()!=null) z.setStatus(zayavka.getStatus());
           /* if(zayavka.getMessage()!=null) z.setMessage(zayavka.getMessage());
            if(zayavka.getService()!=null) z.setService(zayavka.getService());
            if(zayavka.getManager_number()!=null) z.setManager_number(zayavka.getManager_number());
            if(zayavka.getManager_name()!=null)z.setManager_name(zayavka.getManager_name());
            if(zayavka.getAdres()!=null)z.setAdres(zayavka.getAdres());
            if(zayavka.getComment_address()!=null)z.setComment_address(zayavka.getComment_address());
            if(zayavka.getNachalo()!=null)z.setNachalo(zayavka.getNachalo());
            if(zayavka.getAdres()!=null)z.setAdres(zayavka.getAdres());*/


            z = dataManager.save(z);

            String st = z.getTenantAttribute();
            if(Objects.equals(st, AVTOKONNEKT)||isTestUser(z.getUsername())){
                RZ v =new RZ(new ZayavkaUpdate(z.getId().toString(), new Date(), z.getStatus()));
                String url = getBipiumPathByUser(z.getUsername()) +"/api/webrequest/finish_request";
                sendToBpium(url, v);
                log.info("Zayavka update to bipium:{} {}",v, url );
            }


        }

        return z;
    }
    //from bipium
    public Zayavka updateZayavka(ZayavkaDTO zayavka) throws Exception {
            Zayavka z = dataManager.load(Zayavka.class).id(UUID.fromString(zayavka.getId())).one();
        if (zayavka.getUsername() != null&& zayavka.getUsername()!="") {
            z.setUsername(zayavka.getUsername());

            z.setUser(getUser(zayavka.getUsername()));

        }
            if (zayavka.getStatus() != null) z.setStatus(zayavka.getStatus());
            if (zayavka.getMessage() != null) z.setMessage(zayavka.getMessage());
            if (zayavka.getService() != null) z.setService(zayavka.getService());
            if (zayavka.getManager_number() != null) z.setManager_number(zayavka.getManager_number());
            if (zayavka.getAdres() != null) z.setAdres(zayavka.getAdres());
            if (zayavka.getComment_address() != null) z.setComment_address(zayavka.getComment_address());
            Zayavka r = dataManager.save(z);
            log.info("Saved zayavka update from bipium {}", r);
            ZayavkaDTO result = ZayavkaDTO.getFrom(r);
            sendZayavkaToUserApp(result);


            return r;

    }
//avtokonnekt from bipium


    public Zayavka saveZayavka(ZayavkaDTO zayavka) throws Exception {

        List already = dataManager.load(Zayavka.class)
                .query("select z from Zayavka z where z.nomer = :nomer and z.tenantAttribute = :tenant")
                .parameter("nomer", zayavka.getNomer())
                .parameter("tenant",getTenantFromUsername(zayavka.getUsername()))
                .list();
        if(!already.isEmpty())
            throw new IllegalStateException("dubl");
        Zayavka r = dataManager.create(Zayavka.class);
        r.setTenantAttribute(getTenantFromUsername(zayavka.getUsername()));

        r.setNomer(zayavka.getNomer());
        r.setNachalo(zayavka.getNachalo());
        r.setEnd_date_time(zayavka.getEnd_date_time());
        r.setAdres(zayavka.getAdres());
        r.setComment_address(zayavka.getComment_address());
        r.setService(zayavka.getService());
        r.setMessage(zayavka.getMessage());
        r.setClient(zayavka.getClient());
        r.setContact_name(zayavka.getContact_name());
        r.setContact_number(zayavka.getContact_number());
        r.setManager_name(zayavka.getManager_name());
        r.setManager_number(zayavka.getManager_number());
        r.setUsername(zayavka.getUsername());
        r.setUser(getUser(zayavka.getUsername()));
        r.setLat(zayavka.getLat());
        r.setLng(zayavka.getLng());

        r.setStatus("NOVAYA");
        SaveContext saveContext = new SaveContext();
        if (zayavka.getAvtomobili()!=null)
        for( Avto a: zayavka.getAvtomobili()){


            Avtomobil avto = dataManager.create(Avtomobil.class);
            a.setId(avto.getId().toString());
            avto.setStatus("NOVAYA");

            avto.setZayavka(r);
            avto.setNomer(a.getNomer_avto());
            avto.setMarka(a.getMarka_avto());
            avto.setNomerAG(a.getNomerAG());
            avto.setTenantAttribute(AVTOKONNEKT);
            avto.setUsername(zayavka.getUsername());
            saveContext.saving(avto);
        }
        saveContext.saving(r);
        EntitySet result = dataManager.save(saveContext);
        zayavka.setId(result.get(r).getId().toString());

        sendZayavkaToUserApp(zayavka);

        return result.get(r);
    }
    public Duty saveDuty(DutyDTO duty) throws Exception {

        String user = AVTOKONNEKT+"|"+duty.getUsername();
        Duty r = dataManager.create(Duty.class);
        r.setTenantAttribute(AVTOKONNEKT);
        r.setDate_from(duty.getDate_from());
        r.setDate_until(duty.getDate_until());
        r.setUsername(user);
        r.setStatus(duty.getStatus());


        SaveContext saveContext = new SaveContext();

        saveContext.saving(r);
        EntitySet result = dataManager.save(saveContext);


        return result.get(r);
    }
    private void sendZayavkaToUserApp(ZayavkaDTO zayavka) throws Exception {
        if(zayavka.getUsername()==null)
            return;
        User user = getUser(zayavka.getUsername());

        if(user.getFcmRegistrationToken()!=null){

                FcmSender.sendMessageToApp(user.getFcmRegistrationToken(),zayavka);

        }
    }
    private User getUser(String username){
        User user = dataManager.load(User.class)
                .query("select u from User u where u.username = :username")
                .parameter("username", username).one();
        return user;

    }


    public List<Usluga> getAllUslugas(String company) {

        List<Usluga> l = dataManager.load(Usluga.class).query("select c from Usluga c where c.tenantAttribute = :company order by c.prioritet ")
                .parameter("company", company)
                .list();
        return l;
    }
    public List<User> getAllUsers(String company) {

        List<User> l = dataManager.load(User.class).query("select c from User c where c.tenant = :company order by c.lastName ")
                .parameter("company", company)
                .list();
        return l;
    }
    public List<Chek> getAllCheks(String username) {

        List<Chek> l = dataManager.load(Chek.class).query("select c from Chek c where c.username =:username and c.status='NOVAYA'")
                .list();
        return l;
    }
    public List<Chek> getAllPeremeshenies(String username) {

        List<Chek> l = dataManager.load(Chek.class).query("select c from Peremeshenie c where c.username =:username and c.status='NOVAYA'")
                .list();
        return l;
    }
    @Autowired
    private FetchPlans fetchPlans;

    public List<Zayavka> getAllActiveZayavkas(String username) {
        return getAllZayavkas( username, "NOVAYA");

    }
    public List<Zayavka> getAllReadyZayavkas(String username) {
        return getAllZayavkas( username, null);

    }

    public List<Zayavka> getAllZayavkas(String username, String status) {
        FetchPlan fetchPlan = fetchPlans.builder(Zayavka.class)
                .addFetchPlan(FetchPlan.BASE)
                .add("avtomobili", fetchPlans.builder(Avtomobil.class).addFetchPlan(FetchPlan.BASE))
               .build();

        FluentLoader.ByQuery<Zayavka> l = dataManager.load(Zayavka.class).query("select c from Zayavka c where c.username = :username and c.status = :status")
                .parameter("username", username).parameter("status", "NOVAYA");
        List<Zayavka> r = l.fetchPlan(fetchPlan).list();
        return r;
    }
    public List<User> loadUser(String username){
        return dataManager.load(User.class)
                .query("select u from User u where u.username = :username")
                .parameter("username", username).list();


    }

    public List<Duty>  loadDuties(String username){

            return dataManager.load(Duty.class).query(
                    "select c from Duty c where c.username = :username and :now between c.date_from and c.date_until")
                    .parameter("username", username)
                    .parameter("now", new Date())

                    .list();

    }
    public boolean saveChek(Chek chek){
        Chek c = dataManager.create(Chek.class);
        c.setDate(chek.getDate());
        c.setComment(chek.getComment());
        c.setUsername(chek.getUsername());
        c.setQr(chek.getQr());
        c.setTenantAttribute(chek.getTenantAttribute());

        SaveContext saveContext = new SaveContext();

        List<ChekFoto> fs = new ArrayList<ChekFoto>();
        for (ChekFoto entity : chek.getFotos()) {
            ChekFoto nf = dataManager.create(ChekFoto.class);
            nf.setChek(c);
            nf.setFile(entity.getFile());
            nf.setTenantAttribute(c.getTenantAttribute());
            fs.add(nf);
            saveContext.saving(nf);
        }
        saveContext.saving(c);
        EntitySet d = dataManager.save(saveContext);
        Chek re = d.get(c);
        if(AVTOKONNEKT.equals(c.getTenantAttribute())||isTestUser(re.getUsername())) {
            ChekReport result = getChekReport(re);
            boolean sent = sendToBpium(getBipiumPathByUser(re.getUsername())+"/api/webrequest/check?async=true", result);
            if (!sent) {
                c.setComment("Ошибка бипиума, не отправлено");
                dataManager.save(c);
            }
            return sent;
        }
        return true;

    }
    private String getBipiumPathByUser(String username){
        if(username == null || username == "")
            return bipiumPath;
        User u = getUser(username);
        String path = isTestUser(username)?bipiumtestPath:bipiumPath;
        return path;
    }

    private static ChekReport getChekReport(Chek re) {
        List<CF> fos = new ArrayList<CF>();
        for (ChekFoto f : re.getFotos()) {
            fos.add(new CF(f.getFile().toString()));
        }
        ChekReport result = new ChekReport(new C(re.getUsername(), re.getComment(), fos, re.getQr()));
        return result;
    }
    private static final String API_URL = "http://46.8.225.182:8001/process_photo/";
    private static final String API_KEY = "ftxph-ecfgh-abzyt-vargh";

    private static String sendPostRequest(String fileUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //fileUrl = "fs://2025/02/16/7ad3db9e-01fc-f523-dd40-fded71b7d376.jpg?name=scaled_21dd4921-435f-4080-a23b-aa41eeb1c1ef3344823467500867828.jpg";
        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = String.format("{\"file_url\": \"%s\"}", fileUrl);
        RequestBody body = RequestBody.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-API-KEY", API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            ;
            log.info("Otwet ii:code {}\n telo {}",response.code(), response.body().toString());
            if(response.code() == 422)
                return null;
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    private static JsonObject parseCarInfo(String jsonResponse) {
        if(jsonResponse == null)
            return null;
        // Пример парсинга JSON-ответа
        // Предполагаем, что ответ выглядит так: {"message":"Фото обработано","gpt_response":"Лада Ларгус  \nК279ТХ147"}
        // Используем простой парсинг для демонстраци
        JsonObject o = JsonParser.parseString(jsonResponse).getAsJsonObject();

       return o;
    }
    private static JsonObject sendMessageToBot(String fileUrl, String reportId) {

         //fileUrl = "fs://2025/02/16/bd2ee99e-3b06-710b-ca63-d1d275e4a595.jpg?name=scaled_109bc280-5b6e-41bd-9541-8fc0295bdd775478485042372812491.jpg";
        //String reportId = "00534d83-38cb-d552-fd12-0e881322b7f0";

        JsonObject carInfo = null;
        try {
            log.info("Posylaem w bot {}");
            log.info("Posylaem v ii : {} {}", fileUrl, reportId);
            String response = sendPostRequest(fileUrl);
            if (response == null) return null;

            log.info("Iz ii bota polucheno  {}: ", response);
            carInfo = parseCarInfo(response);
            // Парсинг ответа для получения марки и номера



        } catch (IOException e) {
            log.error("Ощибка при отправке {} ", fileUrl, e);
        }


        return carInfo;
    }
    private String  getTenantFromUsername(String username){
       return username.split("\\|")[0];
    }

    public boolean savePeremeshenie(Peremeshenie peremeshenie){
        Peremeshenie c = dataManager.create(Peremeshenie.class);
        c.setDate(peremeshenie.getDate());
        c.setComment(peremeshenie.getComment());
        c.setUsername(peremeshenie.getUsername());
        c.setTenantAttribute(peremeshenie.getTenantAttribute());

        SaveContext saveContext = new SaveContext();

        List<PFoto> fs = new ArrayList<PFoto>();
        for (PFoto entity : peremeshenie.getFotos()) {
            PFoto nf = dataManager.create(PFoto.class);
            nf.setPeremeshenie(c);
            nf.setFile(entity.getFile());
            nf.setTenantAttribute(c.getTenantAttribute());
            fs.add(nf);
            saveContext.saving(nf);
        }

        List<POborudovanie> os = new ArrayList<POborudovanie>();
        for (POborudovanie o : peremeshenie.getBarcode()){
            POborudovanie of = dataManager.create(POborudovanie.class);
            of.setPeremeshenie(c);
            of.setCode(o.getCode());
            of.setTenantAttribute(c.getTenantAttribute());
            os.add(of);
            saveContext.saving(of);
        }



        saveContext.saving(c);
        EntitySet d = dataManager.save(saveContext);
        Peremeshenie re = d.get(c);
        if(AVTOKONNEKT.equals(c.getTenantAttribute())||isTestUser(re.getUsername())) {
            List<PerF> fos = new ArrayList<PerF>();
            for (PFoto f : re.getFotos()) {
                fos.add(new PerF(f.getFile().toString()));
            }
            List<O> pob = new ArrayList<O>();
            for (POborudovanie f : re.getBarcode()) {
                pob.add(new O(f.getCode()));
            }
            PerReport result = new PerReport(new Per(re.getUsername(), re.getComment(), fos, pob));
            boolean sent = sendToBpium(getBipiumPathByUser(re.getUsername())+"/api/webrequest/peremeshenie?async=true", result);
            if (!sent) {
                c.setComment("Ошибка бипиума, не отправлено");
                dataManager.save(c);
            }
            return sent;
        }
        return true;
    }
    //from bipium
    public boolean updateAvto(Avtomobil avto, boolean status){

        log.info("Avto {} sohraneno w bipiume {}",avto.getId(), status);
        if(!status)
            return false;
        Avtomobil r = dataManager.load(Avtomobil.class).id(avto.getId()).one();
        r.setStatus("VYPOLNENA");
        dataManager.save(r);
        User user = getUser(r.getUsername());

        if(user.getFcmRegistrationToken()!=null){

            FcmSender.sendAvtoUpdateMessageToApp(user.getFcmRegistrationToken(),avto.getId().toString());

        }
        return true;

    }

    //iz mp
    public String resendAvto(String id){
        Optional<Avtomobil> ra = dataManager.load(Avtomobil.class).id(UUID.fromString(id)).optional();
        if(prepareAndSendToBipium(ra.get())){

            ra.get().setStatus(Avtomobil.POSLANA_V_BIPIUM);
            return "ok";
        }else{
            ra.get().setStatus("NE_POSLANA_V_BIPIUM_ERROR");
            return "no";
        }

    }
    //iz mp
    public String saveAvto(Avtomobil avto) {
        Avtomobil newOrLoaded;
        Optional<Avtomobil> ra = dataManager.load(Avtomobil.class).id(avto.getId()).optional();

        if(ra.isEmpty()) {
             newOrLoaded = dataManager.create(Avtomobil.class);
            newOrLoaded.setId(avto.getId());
            newOrLoaded.setZayavka(avto.getZayavka());
            newOrLoaded.setMarka(avto.getMarka());
            newOrLoaded.setNomer(avto.getNomer());
            newOrLoaded.setNomerAG(avto.getNomerAG());
            newOrLoaded.setUsername(avto.getUsername());
            newOrLoaded.setTenantAttribute(avto.getTenantAttribute());
            newOrLoaded.setLat(avto.getLat());
            newOrLoaded.setLng(avto.getLng());
            newOrLoaded.setNachaloRabot(avto.getNachaloRabot());


        }else {
            newOrLoaded = ra.get();
        }
        newOrLoaded.setLat(avto.getLat());
        newOrLoaded.setLng(avto.getLng());
        newOrLoaded.setNachaloRabot(avto.getNachaloRabot());
        newOrLoaded.setComment(avto.getComment());
        newOrLoaded.setDate(avto.getDate());
        newOrLoaded.setStatus("POLUCHENA_IZ_MP");
        List<AvtoFoto> afs = new ArrayList<AvtoFoto>();
        for (AvtoFoto f : avto.getAvtoFotos()){
            AvtoFoto nf = dataManager.create(AvtoFoto.class);
            nf.setAvtomobil(newOrLoaded);
            nf.setFile(f.getFile());
            nf.setTenantAttribute(newOrLoaded.getTenantAttribute());

            afs.add(nf);



        }
        if(!afs.isEmpty()) {
            JsonObject r = sendMessageToBot(afs.get(afs.size() - 1).getFile().toString(), avto.getId().toString());
            if (r != null) {

                if (r.get("Марка") != null && !r.get("Марка").isJsonNull()&&r.get("Марка").getAsString()!="null") {

                    newOrLoaded.setMarka(r.get("Марка").getAsString());
                }
                if (r.get("Госномер") != null && !r.get("Госномер").isJsonNull()&&r.get("Госномер").getAsString()!="null")
                    newOrLoaded.setNomer(r.get("Госномер").getAsString());
            }
        }


        List<Foto> fs = new ArrayList<Foto>();
        for (Foto f : avto.getFotos()){
            Foto nf = dataManager.create(Foto.class);
            nf.setAvtomobil(newOrLoaded);
            nf.setFile(f.getFile());
            nf.setTenantAttribute(newOrLoaded.getTenantAttribute());

            fs.add(nf);

        }
        List<OborudovanieFoto> ofs = new ArrayList<OborudovanieFoto>();
        for (OborudovanieFoto of : avto.getOborudovanieFotos()){
            OborudovanieFoto nf = dataManager.create(OborudovanieFoto.class);
            nf.setAvtomobil(newOrLoaded);
            nf.setFile(of.getFile());
            nf.setTenantAttribute(avto.getTenantAttribute());
            ofs.add(nf);

        }
        newOrLoaded.setOborudovanieFotos(ofs);

        List<Oborudovanie> os = new ArrayList<Oborudovanie>();
        for (Oborudovanie o : avto.getBarcode()){
            Oborudovanie of = dataManager.create(Oborudovanie.class);
            of.setAvtomobil(newOrLoaded);
            of.setCode(o.getCode());
            of.setTenantAttribute(avto.getTenantAttribute());
            os.add(of);

        }
        newOrLoaded.setBarcode(os);

        List<AvtoUsluga> us = new ArrayList<AvtoUsluga>();
        for (AvtoUsluga u : avto.getPerformance_service()){
            AvtoUsluga of = dataManager.create(AvtoUsluga.class);
            of.setAvtomobil(newOrLoaded);
            of.setTitle(u.getTitle());
            Usluga usl = dataManager.load(Usluga.class)
                    .query("select u from Usluga u where u.code = :code and u.tenantAttribute = :tenant")
                    .parameter("code", u.getTitle())
                    .parameter("tenant",newOrLoaded.getTenantAttribute())
                    .one();
            of.setUsluga(usl);
            of.setKolichestvo(u.getKolichestvo());
            of.setSverh(u.getSverh());
            of.setTenantAttribute(avto.getTenantAttribute());
            us.add(of);

        }
        newOrLoaded.setPerformance_service(us);


        List<Soispolnitel> ss = new ArrayList<Soispolnitel>();
        for (Soispolnitel si : avto.getSoispolniteli()){
            Soispolnitel sf = dataManager.create(Soispolnitel.class);
            sf.setAvtomobil(newOrLoaded);
            sf.setUsername(si.getUsername());

            sf.setTenantAttribute(avto.getTenantAttribute());
            ss.add(sf);

        }
        newOrLoaded.setSoispolniteli(ss);


        SaveContext saveContext = new SaveContext();
        for (AvtoFoto entity : afs) {
            saveContext.saving( entity);
        }
        for (Foto entity : fs) {
            saveContext.saving( entity);
        }
        for (OborudovanieFoto entity : ofs) {
            saveContext.saving( entity);
        }
        for (Oborudovanie entity : os) {
            saveContext.saving( entity);
        }
        for (AvtoUsluga entity : us) {
            saveContext.saving( entity);
        }
        for (Soispolnitel entity : ss) {
            saveContext.saving( entity);
        }
        saveContext.saving(newOrLoaded);
        EntitySet s = dataManager.save(saveContext);
        newOrLoaded = s.get(newOrLoaded);

        log.info("Mp sohranjaet otchet: {}", newOrLoaded);


        if(newOrLoaded.getTenantAttribute().equals(AVTOKONNEKT)||isTestUser( newOrLoaded.getUsername())) {
            return prepareAndSendToBipium(newOrLoaded)?Avtomobil.POSLANA_V_BIPIUM:"OSHIBKA_OTPRAVKI_V_BIPIUM";
        }
        newOrLoaded.setStatus(Avtomobil.VYPOLNENA);
        dataManager.save(newOrLoaded);
        return Avtomobil.VYPOLNENA;


    }
    boolean isTestUser(String username){
        Boolean t = getUser(username).getTest();
        return t!=null&&t.booleanValue();
    }

    private boolean prepareAndSendToBipium( Avtomobil savedWithAllData) {
        Avto av = new Avto();
        av.setId(savedWithAllData.getId().toString());
        av.setZayavka_id(savedWithAllData.getZayavka().getId().toString());
        av.setUsername(savedWithAllData.getUsername());

        av.setMarka_avto(savedWithAllData.getMarka());
        av.setNomer_avto(savedWithAllData.getNomer());
        av.setNomerAG(savedWithAllData.getNomerAG());
        av.setDate(savedWithAllData.getDate());
        av.setNachaloRabot(savedWithAllData.getNachaloRabot());
        av.setComment(savedWithAllData.getComment());
        av.setLat(savedWithAllData.getLat());
        av.setLng(savedWithAllData.getLng());
        List<O> b = new ArrayList<O>();
        for (Oborudovanie o : savedWithAllData.getBarcode()){
            b.add(new O(o.getCode()));
        }
        av.setBarcode(b);
        List<U> usl = new ArrayList<U>();
        for (AvtoUsluga o : savedWithAllData.getPerformance_service()){
            U u = new U();
            u.setTitle(o.getTitle());
            u.setKolichestvo(o.getKolichestvo());
            u.setSverh(o.getSverh());
            usl.add(u);
        }
        av.setPerformance_service(usl);
        List<Soisp> soispolniteli = new ArrayList<Soisp>();
        for (Soispolnitel o : savedWithAllData.getSoispolniteli()){
            Soisp u = new Soisp();
            u.setUsername(o.getUsername());

            soispolniteli.add(u);
        }
        av.setSoispolniteli(soispolniteli);


        List<F> fo = new ArrayList<F>();
        for (Foto o : savedWithAllData.getFotos()){
            F u = new F();
            u.setFile(o.getFile().toString());
            fo.add(u);
        }
        av.setFotos(fo);


        List<OF> ofo = new ArrayList<OF>();
        for (OborudovanieFoto o : savedWithAllData.getOborudovanieFotos()){
            OF u = new OF();
            u.setFile(o.getFile().toString());
            ofo.add(u);
        }
        av.setOborudovanieFotos(ofo);

        List<AF> afo = new ArrayList<AF>();
        for (AvtoFoto o : savedWithAllData.getAvtoFotos()){
            AF u = new AF();
            u.setFile(o.getFile().toString());
            afo.add(u);
        }
        av.setAvtofotos(afo);

        R re = new R();
        re.setReport(av);
        savedWithAllData.setStatus("POSYLAEM_V_BIPIUM");
        boolean sent =  sendToBpium( getBipiumPathByUser(savedWithAllData.getUsername()) +"/api/webrequest/mobilapp?async=true", re);
        if(!sent){
            savedWithAllData.setStatus("NE_POSLALI_V_BIPIUM");

            log.info("Ne otpravil v bipium: {}", savedWithAllData);
        }
        dataManager.save(savedWithAllData);
        return sent;
    }

    public static boolean sendToBpium(String url, Object re) {
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();

        String json = g.toJson(re);
        AtomicBoolean ok = new AtomicBoolean(false);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .timeout(Duration.of(10, SECONDS))
                    .headers("Content-Type", "Content-Type: application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();


            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(stringHttpResponse -> {
                        ok.set(stringHttpResponse.statusCode() == 200);
                        log.info("При отправке v bipium: {}",json);}).join();

        } catch (Exception e) {
           log.error("Oshibka При отправке: {}",json, e );
        }
        return  ok.get();
    }
}
class DutyDTO{
    private Date date_from;
    private Date date_until;
    private String status;
    private String fio;
    private String username;


    public void setStatus(String status) {
        this.status = status;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getStatus() {
        return status;
    }

    public Date getDate_from() {
        return date_from;
    }

    public Date getDate_until() {
        return date_until;
    }

    public void setDate_from(Date date_from) {
        this.date_from = date_from;
    }

    public void setDate_until(Date date_until) {
        this.date_until = date_until;
    }

    public String getFio() {
        return fio;
    }

    public String getUsername() {
        return username;
    }
}
class ZayavkaDTO{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String lat;

    private String lng;

    private String service;

    private String comment_address;

    private String message;

    private String client;

    private String contact_name;

    private String contact_number;

    private String manager_name;

    private String manager_number;


    private Date end_date_time;


    private String username;

    private List<Avto> avtomobili;

    private String nomer;

    private Date nachalo;



    private String adres;


    private String status;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getComment_address() {
        return comment_address;
    }

    public void setComment_address(String comment_address) {
        this.comment_address = comment_address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getManager_number() {
        return manager_number;
    }

    public void setManager_number(String manager_number) {
        this.manager_number = manager_number;
    }

    public Date getEnd_date_time() {
        return end_date_time;
    }

    public void setEnd_date_time(Date end_date_time) {
        this.end_date_time = end_date_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Avto> getAvtomobili() {
        return avtomobili;
    }

    public void setAvtomobili(List<Avto> avtomobili) {
        this.avtomobili = avtomobili;
    }

    public String getNomer() {
        return nomer;
    }

    public void setNomer(String nomer) {
        this.nomer = nomer;
    }

    public Date getNachalo() {
        return nachalo;
    }

    public void setNachalo(Date nachalo) {
        this.nachalo = nachalo;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    static ZayavkaDTO getFrom(Zayavka z){
        ZayavkaDTO r = new ZayavkaDTO();
        r.setMessage(z.getMessage());
        r.setService(z.getService());
        r.setUsername(z.getUsername());
        if(z.getUser()!=null){
            r.setUsername(z.getUser().getUsername());
        }
        r.setClient(z.getClient());
        if(z.getKontragent()!=null){
            r.setClient(z.getKontragent().getNazvanie());
        }
        r.setAdres(z.getAdres());
        r.setComment_address(z.getComment_address());
        r.setContact_name(z.getContact_name());
        r.setContact_number(z.getContact_number());
        r.setManager_name(z.getManager_name());
        r.setManager_number(z.getManager_number());
        r.setEnd_date_time(z.getEnd_date_time());
        r.setId(z.getId().toString());
        r.setLat(z.getLat());
        r.setLng(z.getLng());
        r.setNachalo(z.getNachalo());
        r.setNomer(z.getNomer());
        r.setStatus(z.getStatus());

        return  r;
    }
}
class Avto{
    static Avto fromAvtomobil(Avtomobil a){
        Avto r  = new Avto();
        r.setId(a.getId().toString());
        r.setUsername(a.getUsername());
        r.setComment(a.getComment());
        r.setNomerAG(a.getNomerAG());
        r.setMarka_avto(a.getMarka());
        r.setNomer_avto(a.getNomer());

        return r;
    }
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String zayavka_id;
    private String marka_avto;
    private String nomer_avto;
    private String nomerAG;
    private Date date;
    private Date nachaloRabot;
    private String comment;
    private List<O> barcode;
    private List<U> performance_service;
    private List<F> fotos;
    private List<AF> avtofotos;
    private List<OF> oborudovanieFotos;
    private List<Soisp> soispolniteli;
    private String status;
    private String lat;
    private String lng;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getNachaloRabot() {
        return nachaloRabot;
    }

    public void setNachaloRabot(Date nachaloRabot) {
        this.nachaloRabot = nachaloRabot;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public List<AF> getAvtofotos() {
        return avtofotos;
    }

    public void setAvtofotos(List<AF> avtofotos) {
        this.avtofotos = avtofotos;
    }

    public List<Soisp> getSoispolniteli() {
        return soispolniteli;
    }

    public void setSoispolniteli(List<Soisp> soispolniteli) {
        this.soispolniteli = soispolniteli;
    }

    public List<OF> getOborudovanieFotos() {
        return oborudovanieFotos;
    }

    public void setOborudovanieFotos(List<OF> oborudovanieFotos) {
        this.oborudovanieFotos = oborudovanieFotos;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getZayavka_id() {
        return zayavka_id;
    }

    public void setZayavka_id(String zayavka_id) {
        this.zayavka_id = zayavka_id;
    }

    public String getMarka_avto() {
        return marka_avto;
    }

    public void setMarka_avto(String marka_avto) {
        this.marka_avto = marka_avto;
    }

    public String getNomer_avto() {
        return nomer_avto;
    }

    public void setNomer_avto(String nomer_avto) {
        this.nomer_avto = nomer_avto;
    }

    public String getNomerAG() {
        return nomerAG;
    }

    public void setNomerAG(String nomerAG) {
        this.nomerAG = nomerAG;
    }

    public List<O> getBarcode() {
        return barcode;
    }

    public void setBarcode(List<O> barcode) {
        this.barcode = barcode;
    }

    public List<U> getPerformance_service() {
        return performance_service;
    }

    public void setPerformance_service(List<U> performance_service) {
        this.performance_service = performance_service;
    }

    public List<F> getFotos() {
        return fotos;
    }

    public void setFotos(List<F> fotos) {
        this.fotos = fotos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
class O{
    O(String code){
        this.code = code;
    }
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
class ChekReport{
    private C chek;

    public ChekReport(C chek) {
        this.chek = chek;
    }

    public C getChek() {
        return chek;
    }
}
class PerReport{
    private Per per;

    public PerReport(Per per) {
        this.per = per;
    }

    public Per getPer() {
        return per;
    }
}
class C{
    C(String username, String comment, List<CF> fotos, String qr){
        this.comment = comment;
        this.username = username;
        this.fotos = fotos;
        this.qr = qr;
    }
    private String username;
    private String comment;
    private List<CF> fotos;
    private String qr;

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public List<CF> getFotos() {
        return fotos;
    }

    public String getQr() {
        return qr;
    }
}
class Per{
    Per(String username, String comment, List<PerF> fotos,List<O> oborud){
        this.comment = comment;
        this.username = username;
        this.fotos = fotos;
        this.oborud = oborud;
    }
    private String username;
    private String comment;
    private List<PerF> fotos;
    private List<O> oborud;

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public List<PerF> getFotos() {
        return fotos;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setFotos(List<PerF> fotos) {
        this.fotos = fotos;
    }

    public List<O> getOborud() {
        return oborud;
    }

    public void setOborud(List<O> oborud) {
        this.oborud = oborud;
    }
}
class CF{
    CF(String file){
        this.file = file;
    }
    private String file;

    public String getFile() {
        return file;
    }
}
class PerF{
    PerF(String file){
        this.file = file;
    }
    private String file;

    public String getFile() {
        return file;
    }
}
class U{
   private String title;

   private int kolichestvo;

   private int sverh;

    public int getKolichestvo() {
        return kolichestvo;
    }

    public void setKolichestvo(int kolichestvo) {
        this.kolichestvo = kolichestvo;
    }

    public int getSverh() {
        return sverh;
    }

    public void setSverh(int sverh) {
        this.sverh = sverh;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
class Soisp{
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
class F{
    private  String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
class OF{
    private  String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
class AF{
    private  String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
class R{
    private Avto report;

    public Avto getReport() {
        return report;
    }

    public void setReport(Avto report) {
        this.report = report;
    }
}
class RZ{
    private ZayavkaUpdate report;
    public RZ(ZayavkaUpdate zayavkaUpdate) {
        report = zayavkaUpdate;
    }

    public ZayavkaUpdate getReport() {
        return report;
    }

    public void setReport(ZayavkaUpdate report) {
        this.report = report;
    }
}
class ZayavkaUpdate {
    public ZayavkaUpdate(String zayavka_id, Date date, String status) {
        this.zayavka_id = zayavka_id;
        this.date = date;
        this.status = status;
    }

    private  String zayavka_id;
    private Date date;
    private String status;

    public String getZayavka_id() {
        return zayavka_id;
    }

    public void setZayavka_id(String zayavka_id) {
        this.zayavka_id = zayavka_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}