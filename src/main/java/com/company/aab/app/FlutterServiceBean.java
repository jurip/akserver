package com.company.aab.app;

import com.company.aab.entity.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jmix.core.*;
import io.jmix.core.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final DataManager dataManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public FlutterServiceBean(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    public String login(String username, String password){
        UserDetails userDetails = userRepository.loadUserByUsername(username);

            if(passwordEncoder.matches(password, userDetails.getPassword()))
                 return "ok";

        return "no";
    }
    public Zayavka sendZayavkaUpdate(Zayavka zayavka){
        if(zayavka.getId()!=null){
            Zayavka z = dataManager.load(Zayavka.class).id(zayavka.getId()).one();
            if(zayavka.getStatus()!=null) z.setStatus(zayavka.getStatus());
            if(zayavka.getMessage()!=null) z.setMessage(zayavka.getMessage());
            if(zayavka.getService()!=null) z.setService(zayavka.getService());
            if(zayavka.getManager_number()!=null) z.setManager_number(zayavka.getManager_number());
            if(zayavka.getAdres()!=null)z.setAdres(zayavka.getAdres());
            if(zayavka.getComment_address()!=null)z.setComment_address(zayavka.getComment_address());
            Zayavka result = dataManager.save(z);

            String st = result.getStatus();
            if(Objects.equals(st, Zayavka.VYPOLNENA)){
                RZ v =new RZ(new  ZayavkaVypolnena(result.getId().toString(), new Date(), result.getStatus()));

                sendToBpium("https://autoconnect.bpium.ru/api/webrequest/finish_request", v);
            }

            return result;
        }

        Zayavka r  = dataManager.save(zayavka);


        return r;
    }
    public Zayavka updateZayavka(Zayavka zayavka) {
            Zayavka z = dataManager.load(Zayavka.class).id(zayavka.getId()).one();
            if (zayavka.getStatus() != null) z.setStatus(zayavka.getStatus());
            if (zayavka.getMessage() != null) z.setMessage(zayavka.getMessage());
            if (zayavka.getService() != null) z.setService(zayavka.getService());
            if (zayavka.getManager_number() != null) z.setManager_number(zayavka.getManager_number());
            if (zayavka.getAdres() != null) z.setAdres(zayavka.getAdres());
            if (zayavka.getComment_address() != null) z.setComment_address(zayavka.getComment_address());
            Zayavka r = dataManager.save(z);

            return r;

    }
    public Zayavka saveZayavka(Zayavka zayavka){
        Zayavka r = dataManager.create(Zayavka.class);
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

        r.setStatus("NOVAYA");

        Zayavka result = dataManager.save(r);
        return result;
    }


    public List<Usluga> getAllUslugas(String username) {

        List<Usluga> l = dataManager.load(Usluga.class).query("select c from Usluga c")
                .list();
        return l;
    }
    public List<Chek> getAllCheks(String username) {

        List<Chek> l = dataManager.load(Chek.class).query("select c from Chek c where c.username =:username and c.status='NOVAYA'")
                .list();
        return l;
    }
    public List<Zayavka> getAllActiveZayavkas(String username) {

        List<Zayavka> l = dataManager.load(Zayavka.class).query("select c from Zayavka c where c.username = :username and c.status = :status")
                .parameter("username", username).parameter("status", "NOVAYA").list();
        return l;
    }

    public Duty saveDuty(Duty duty){
        Duty d = dataManager.save(duty);
        return d;
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

        SaveContext saveContext = new SaveContext();

        List<ChekFoto> fs = new ArrayList<ChekFoto>();
        for (ChekFoto entity : chek.getFotos()) {
            ChekFoto nf = dataManager.create(ChekFoto.class);
            nf.setChek(c);
            nf.setFile(entity.getFile());
            fs.add(nf);
            saveContext.saving(nf);
        }
        saveContext.saving(c);
        EntitySet d = dataManager.save(saveContext);
        Chek re = d.get(c);
        List<CF> fos = new ArrayList<CF>();
        for (ChekFoto f :re.getFotos()){
            fos.add(new CF(f.getFile().toString()));
        }
        ChekReport result = new ChekReport(new C(re.getUsername(), re.getComment(), fos));
        boolean sent =  sendToBpium("https://autoconnect.bpium.ru/api/webrequest/check", result);
        if(!sent){
            c.setComment("Ошибка бипиума, не отправлено");
            dataManager.save(c);
        }
        return sent;
    }
    public boolean saveAvto(Avtomobil avto) {
        Avtomobil a = dataManager.create(Avtomobil.class);
        a.setZayavka(avto.getZayavka());
        a.setMarka(avto.getMarka());
        a.setNomer(avto.getNomer());
        a.setDate(avto.getDate());
        a.setUsername(a.getUsername());
        a.setStatus("VYPOLNENA");
        List<Foto> fs = new ArrayList<Foto>();
        for (Foto f : avto.getFotos()){
            Foto nf = dataManager.create(Foto.class);
            nf.setAvtomobil(a);
            nf.setFile(f.getFile());

            fs.add(nf);

        }
        List<Oborudovanie> os = new ArrayList<Oborudovanie>();
        for (Oborudovanie o : avto.getBarcode()){
            Oborudovanie of = dataManager.create(Oborudovanie.class);
            of.setAvtomobil(a);
            of.setCode(o.getCode());
            os.add(of);

        }
        a.setBarcode(os);

        List<AvtoUsluga> us = new ArrayList<AvtoUsluga>();
        for (AvtoUsluga u : avto.getPerformance_service()){
            AvtoUsluga of = dataManager.create(AvtoUsluga.class);
            of.setAvtomobil(a);
            of.setTitle(u.getTitle());
            us.add(of);
            //dataManager.save(nf);
        }
        a.setPerformance_service(us);

        SaveContext saveContext = new SaveContext();
        for (Foto entity : fs) {
            saveContext.saving( entity);
        }
        for (Oborudovanie entity : os) {
            saveContext.saving( entity);
        }
        for (AvtoUsluga entity : us) {
            saveContext.saving( entity);
        }
        saveContext.saving(a);
        EntitySet s = dataManager.save(saveContext);
        Avtomobil r = s.get(a);


        Avto av = new Avto();
        av.setZayavka_id(r.getZayavka().getId().toString());
        av.setMarka_avto(r.getMarka());
        av.setNomer_avto(r.getNomer());
        av.setDate(r.getDate());
        av.setComment("Выполнил");
        List<O> b = new ArrayList<O>();
        for (Oborudovanie o : avto.getBarcode()){
            O ob = new O();
            ob.setCode(o.getCode());
            b.add(ob);
        }
        av.setBarcode(b);
        List<U> usl = new ArrayList<U>();
        for (AvtoUsluga o : avto.getPerformance_service()){
            U u = new U();
            u.setTitle(o.getTitle());
            usl.add(u);
        }
        av.setPerformance_service(usl);
        List<F> fo = new ArrayList<F>();
        for (Foto o : avto.getFotos()){
            F u = new F();
            u.setFile(o.getFile().toString());
            fo.add(u);
        }
        av.setFotos(fo);
        av.setStatus("VYPOLNENA");
        R re = new R();
        re.setReport(av);
        boolean sent =  sendToBpium("https://autoconnect.bpium.ru/api/webrequest/mobilapp", re);
        if(!sent){
            a.setStatus("BIPIUM_ERROR");
            dataManager.save(a);
        }
        return sent;


    }

    private static boolean sendToBpium(String url, Object re) {
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
            System.out.print(json);
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(stringHttpResponse -> {
                        ok.set(stringHttpResponse.statusCode() == 200);
                        System.out.println(stringHttpResponse);}).join();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return  ok.get();
    }
}
class Avto{
    private String zayavka_id;
    private String marka_avto;
    private String nomer_avto;
    private Date date;
    private String comment;
    private List<O> barcode;
    private List<U> performance_service;
    private List<F> fotos;
    private String status;

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
class C{
    C(String username, String comment, List<CF> fotos){
        this.comment = comment;
        this.username = username;
        this.fotos = fotos;
    }
    private String username;
    private String comment;
    private List<CF> fotos;

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public List<CF> getFotos() {
        return fotos;
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
class U{
   private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    private ZayavkaVypolnena report;
    public RZ(ZayavkaVypolnena zayavkaVypolnena) {
        report = zayavkaVypolnena;
    }

    public ZayavkaVypolnena getReport() {
        return report;
    }

    public void setReport(ZayavkaVypolnena report) {
        this.report = report;
    }
}
class ZayavkaVypolnena{
    public ZayavkaVypolnena(String zayavka_id, Date date, String status) {
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