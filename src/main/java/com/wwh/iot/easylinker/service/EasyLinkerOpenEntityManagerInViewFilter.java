package com.wwh.iot.easylinker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wwhai on 2017/10/5.
 */
@Service
public class EasyLinkerOpenEntityManagerInViewFilter extends OpenEntityManagerInViewFilter {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    protected EntityManagerFactory lookupEntityManagerFactory(HttpServletRequest request) {
        return entityManagerFactory;
    }

}

