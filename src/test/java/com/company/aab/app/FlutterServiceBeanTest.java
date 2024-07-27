package com.company.aab.app;

import com.company.aab.entity.Zayavka;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jmix.core.DataManager;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.mockito.Mockito.mock;

public class FlutterServiceBeanTest {

    @Test
    void testTimeFormat(){
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
        String d = g.toJson(new Date());
        Assert.isEqual(g.toJson(new Date()), "dsds", "kuku");


    }
    @Test
    void testEquals(){
        String s1 = Zayavka.VYPOLNENA;
        String s2 = "VYPOLNENA";


    }

}
