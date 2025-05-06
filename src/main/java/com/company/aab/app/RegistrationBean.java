package com.company.aab.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class RegistrationBean {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    public boolean hasCompany(String name){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
        Integer count = jdbcTemplate.queryForObject("select count(*) from mten_tenant where name=:name", namedParameters, Integer.class );
        return count != 0;

    }
    public void newCompany(String name) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
        jdbcTemplate.update("INSERT INTO mten_tenant VALUES(uuid(),1, now(), 'admin', null, null, null, null, :name, :name)", namedParameters);
    }
}