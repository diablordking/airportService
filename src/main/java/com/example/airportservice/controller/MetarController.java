package com.example.airportservice.controller;

import com.example.airportservice.dto.MetarDTO;
import com.example.airportservice.exceptions.MetarException;
import com.example.airportservice.models.Metar;
import com.example.airportservice.models.MetarMetrics;
import com.example.airportservice.services.MetarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/airport")
public class MetarController {

    /**
     * Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(MetarController.class);

    @Autowired
    MetarService metorService;

    /**
     * Endpoint to store METAR data for a station/icao code
     *
     * @param icaoCode icaoCode
     * @param metarDTO metarDTO
     * @throws MetarException MetarException
     */
    @PostMapping("/{icaoCode}/METAR")
    public void storeMetarData(@PathVariable("icaoCode") String icaoCode, @RequestBody MetarDTO metarDTO) throws MetarException {
        metorService.storeMetarData(icaoCode, new Metar(metarDTO));
    }

    /**
     * Endpoint to store METAR data for list of station/icao code
     *
     * @param metarList metarList
     * @throws MetarException MetarException
     */
    @PostMapping("/METAR")
    public void storeMetarData(@RequestBody List<Metar> metarList) throws MetarException {
        metorService.storeMetarData(metarList);
    }

    /**
     * Endpoint to get METAR data for a Station/icao code
     *
     * @param icaoCode icaoCode
     * @return Metar
     * @throws MetarException MetarException
     */
    @GetMapping("/{icaoCode}/METAR")
    public ResponseEntity getMetarData(
            @PathVariable("icaoCode") String icaoCode,
            @RequestParam(required = false) boolean subset,
            @RequestParam(required = false) String columns
    ) throws MetarException {
        if (subset) {
            if (StringUtils.hasText(columns)) {
                MetarMetrics metarMetrics = metorService.getMetarMetricsData(icaoCode);
                if (metarMetrics != null) {
                    List<String> columnList = Arrays.asList(columns.split(","));
                    Map<String, String> metarMetricsMap = new HashMap<>();
                    metarMetricsMap.put("icaoCode", metarMetrics.getIcaoCode());
                    metarMetricsMap.put("timestamp", metarMetrics.getTimestamp().toString());
                    columnList.forEach(column -> {
                        if (column.equalsIgnoreCase("wind_strength")) {
                            metarMetricsMap.put("wind_strength", metarMetrics.getWindStrength());
                        } else if (column.equalsIgnoreCase("temperature")) {
                            metarMetricsMap.put("temperature", metarMetrics.getTemperature());
                        } else if (column.equalsIgnoreCase("visibility")) {
                            metarMetricsMap.put("visibility", metarMetrics.getVisibility());
                        }
                    });
                    return new ResponseEntity(metarMetricsMap, HttpStatus.OK);
                } else {
                    return new ResponseEntity("Invalid Station Code", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity("Columns Missing, Specify in comma separated value", HttpStatus.BAD_REQUEST);
            }
        } else {
            Metar metar = metorService.getMetarData(icaoCode);
            if (metar != null) {
                return new ResponseEntity(metar, HttpStatus.OK);
            } else {
                return new ResponseEntity("Invalid Station Code", HttpStatus.BAD_REQUEST);
            }
        }
    }

}
