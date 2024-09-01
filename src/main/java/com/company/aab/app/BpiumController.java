package com.company.aab.app;
import com.company.aab.entity.Zayavka;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import io.jmix.core.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/dlyaChajnikov")
public class BpiumController {
    private final DataManager dataManager;
    private final EntityImportExport entityImportExport;
    private final FlutterServiceBean fl;
    private final EntityImportPlans entityImportPlans;

    public BpiumController(DataManager dataManager, EntityImportExport entityImportExport, EntityImportPlans entityImportPlans, FlutterServiceBean fl) {

        this.dataManager = dataManager;
      this.entityImportExport = entityImportExport;
          this.fl = fl;
        this.entityImportPlans = entityImportPlans;
    }

    @PostMapping("/bpium")
    public String hi(@RequestBody String body) throws Exception {

        byte[] bytes = body.getBytes();
        body = new String(bytes, StandardCharsets.UTF_8);
        body = "["+body+"]";


        EntityImportPlan importPlan = entityImportPlans.builder(Zayavka.class)
                .addLocalProperties()
                .build();
        Collection<Object> j = entityImportExport.importEntitiesFromJson(body, importPlan);
        ZayavkaDTO z = (ZayavkaDTO) j;
        Zayavka r = fl.saveZayavka(z);
        return entityImportExport.exportEntitiesToJSON(List.of(r));
       // return body;

    }
    @GetMapping("/flutter")
    public String zayavki(@Param("username") String username){

        return
        entityImportExport
                .exportEntitiesToJSON(
                        Collections.singleton(dataManager.unconstrained()
                                .load(Zayavka.class).all().list()));

    }
}