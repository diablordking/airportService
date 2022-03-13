package com.example.airportservice.services;

import com.example.airportservice.exceptions.MetarException;
import com.example.airportservice.models.Station;
import com.example.airportservice.repository.StationRepository;
import com.example.airportservice.utils.HtmlParser;
import com.example.airportservice.utils.StationHeaders;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class StationService {

    /**
     * Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(StationService.class);
    @Value("${station.metar.data.url}")
    public String metarDataUrl;  // Made it public for covering Integration test suite for failure test cases
    @Autowired
    HtmlParser htmlParser;
    @Autowired
    StationRepository stationRepository;

    /**
     * Get Station info by icaoCode
     *
     * @param icaoCode icaoCode
     * @return Station
     */
    public Station getStation(String icaoCode) {
        return stationRepository.findByIcaoCode(icaoCode);
    }

    /**
     * Validate if the station code is correct
     *
     * @param icaoCode icaoCode
     * @throws MetarException MetarException
     */
    public void validateStation(String icaoCode) throws MetarException {
        if (getStation(icaoCode) == null) {
            throw new MetarException("Invalid Station Code : " + icaoCode);
        }
    }

    /**
     * Get Station data from database starting with
     *
     * @param startsWith startsWith
     * @return Station list
     */
    public List<Station> getStationStartsWithFromDB(String startsWith) {
        return stationRepository.findByIcaoCodeStartsWith(startsWith.toUpperCase());
    }

    /**
     * Get Station data from NWS
     *
     * @return Station list
     * @throws MetarException MetarException
     */
    public List<Station> getStationFromNWS() throws MetarException {
        List<Station> stationList = null;
        try {
            Document document = Jsoup.parse(new URL(metarDataUrl), 30000);
            stationList = parseStationSummary(document);
            stationRepository.saveAll(stationList);
        } catch (Exception e) {
            String errorMessage = "Error while reading Station information from " + metarDataUrl;
            logger.error(errorMessage, e);
            throw new MetarException(errorMessage);
        }
        return stationList;
    }

    private List<Station> parseStationSummary(Document document) {
        List<Station> stationList = new ArrayList<>();
        if (document == null) {
            return stationList;
        }
        List<Map<String, String>> stationSummaryTable = htmlParser.parseTable(document);
        stationList = stationSummaryTable.stream()
                .filter(stationSummaryRow -> stationSummaryRow.get(StationHeaders.NAME.getHeader()).length() == 8)
                .map(stationSummaryRow -> {
                    Station station = new Station();
                    station.setFilename(stationSummaryRow.get(StationHeaders.NAME.getHeader()));
                    station.setIcaoCode(station.getFilename().substring(0, 4));
                    station.setSize(Integer.parseInt(stationSummaryRow.get(StationHeaders.SIZE.getHeader())));
                    station.setLastModified(stationSummaryRow.get(StationHeaders.LAST_MODIFIED.getHeader()));
                    return station;
                })
                .collect(Collectors.toList());
        return stationList;
    }
}
