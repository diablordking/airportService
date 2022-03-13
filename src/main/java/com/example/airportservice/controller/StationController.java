package com.example.airportservice.controller;


import com.example.airportservice.exceptions.MetarException;
import com.example.airportservice.models.Station;
import com.example.airportservice.services.StationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/airport")
public class StationController {

    /**
     * Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(StationController.class);

    @Autowired
    StationService stationService;

    /**
     * Endpoint to get Station info with starts with Keyword search
     *
     * @param startsWith startsWith
     * @return Station list
     * @throws MetarException MetarException
     */
    @GetMapping("/stations/{startsWith}")
    public List<Station> getStationFromDB(
            @PathVariable("startsWith") String startsWith) throws MetarException {
        return stationService.getStationStartsWithFromDB(startsWith);
    }
}
