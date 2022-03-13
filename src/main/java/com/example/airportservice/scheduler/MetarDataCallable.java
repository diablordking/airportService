package com.example.airportservice.scheduler;

/*
 * *
 *  * Project: ${PROJECT_NAME}
 *  * Package: ${PACKAGE_NAME}
 *  * Desc:
 *  * User: Varun Chandresekar
 *  * Date: ${DATE}
 *  * Time: ${TIME}
 *  * Copyright (c) 2022.
 *
 *
 */


import com.example.airportservice.models.Metar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.Callable;


public class MetarDataCallable implements Callable<Metar> {

    /**
     * Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(MetarDataCallable.class);

    private final String url;
    private final String icaoCode;

    /**
     * Constructor to pass url and icao Code
     *
     * @param url      url
     * @param icaoCode icaoCode
     */
    public MetarDataCallable(String url, String icaoCode) {
        this.url = url + icaoCode + ".TXT";
        this.icaoCode = icaoCode;
    }

    @Override
    public Metar call() throws Exception {
        Metar metar = null;
        try {
            String metarData = new RestTemplate().getForObject(url, String.class);
            if (StringUtils.hasText(metarData)) {
                metar = new Metar();
                String[] metarDataLine = metarData.split("\n");
                metar.setIcaoCode(icaoCode);
                metar.setData(metarDataLine[1]);
                metar.setCreatedTime(new Timestamp(new Date(metarDataLine[0]).getTime()));
            }
        } catch (Exception e) {
            logger.error("Failed to fetch Metar data for ICAO Code : {}", icaoCode, e);
        }
        return metar;
    }
}
