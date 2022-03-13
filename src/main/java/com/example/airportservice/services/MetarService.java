package com.example.airportservice.services;

import com.example.airportservice.exceptions.MetarException;
import com.example.airportservice.models.Metar;
import com.example.airportservice.models.MetarMetrics;
import com.example.airportservice.repository.MetarMetricsRepository;
import com.example.airportservice.repository.MetarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MetarService {

    /**
     * Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(MetarService.class);

    @Autowired
    MetarRepository metarRepository;

    @Autowired
    MetarMetricsRepository metarMetricsRepository;

    @Autowired
    StationService stationService;

    /**
     * API to store Metar data to database
     *
     * @param icaoCode icaoCode
     * @param metar    metar
     * @throws MetarException MetarException
     */
    @CacheEvict(value = {"MetarData"})
    public void storeMetarData(String icaoCode, Metar metar) throws MetarException {
        stationService.validateStation(icaoCode);
        metar.setIcaoCode(icaoCode);
        metarRepository.save(metar);
        parseMetarData(metar.getData(), metar.getCreatedTime());
    }

    /**
     * API to persist Metar data list to database
     *
     * @param metarList metarList
     */
    @CacheEvict(value = {"MetarData"})
    public void storeMetarData(List<Metar> metarList) {
        metarRepository.saveAll(metarList);
        metarList.forEach(metar -> parseMetarData(metar.getData(), metar.getCreatedTime()));
    }

    /**
     * API to get Metar data by icao Code
     *
     * @param icaoCode icaoCode
     * @return Metar data
     */
    @Cacheable("MetarData")
    @Transactional
    public Metar getMetarData(String icaoCode) {
        return metarRepository.findByIcaoCode(icaoCode);
    }

    @Transactional
    public MetarMetrics getMetarMetricsData(String icaoCode) {
        return metarMetricsRepository.findByIcaoCode(icaoCode);
    }

    private void parseMetarData(String data, Timestamp timestamp) {
        MetarMetrics metarMetrics = new MetarMetrics();
        if (StringUtils.hasText(data)) {
            metarMetrics.setTimestamp(timestamp);
            List<String> dataList = Stream.of(data.split(" "))
                    .map(String::trim)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(dataList)) {
                metarMetrics.setIcaoCode(dataList.get(0));
                dataList = dataList.subList(2, dataList.size());
                boolean isVisibility = false;
                for (String metric : dataList) {

                    // Visibility
                    if (isVisibility) {
                        isVisibility = false;
                        metarMetrics.setVisibility(metric);
                    }

                    // Wind Strength
                    if (metric.substring(metric.length() - 2).equalsIgnoreCase("KT")) {
                        metarMetrics.setWindStrength(metric);
                        isVisibility = true;
                    }

                    // Temperature
                    if (metric.chars().filter(ch -> ch == '/').count() == 1) {
                        metarMetrics.setTemperature(metric);
                        break;
                    }
                }

            }
            metarMetricsRepository.save(metarMetrics);
        }
    }
}
