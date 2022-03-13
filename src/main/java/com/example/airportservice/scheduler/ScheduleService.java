package com.example.airportservice.scheduler;

import com.example.airportservice.exceptions.MetarException;
import com.example.airportservice.models.Metar;
import com.example.airportservice.models.Subscription;
import com.example.airportservice.services.MetarService;
import com.example.airportservice.services.StationService;
import com.example.airportservice.services.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ScheduleService {

    protected static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);
    @Value("${station.metar.data.url}")
    public String url; // Made it public for covering Integration test suite for failure test cases
    @Autowired
    StationService stationService;
    @Value("${metar.data.refresh.threadCount}")
    int threadCount;
    @Value("${metar.data.refresh.timeout}")
    int timeout;
    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    MetarService metarService;

    @Scheduled(fixedDelay = 15000, initialDelay = 8000)
    public void scheduleFixedRateTask() throws Exception {
        try {
            List<Subscription> subscriptionList = subscriptionService.getSubscriptionsIsActive();
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            List<MetarDataCallable> metarDataCallables = new ArrayList<>();
            subscriptionList.forEach(subscription ->
                    metarDataCallables.add(new MetarDataCallable(url, subscription.getIcaoCode()))
            );
            List<Future<Metar>> metarFutureList = executorService.invokeAll(metarDataCallables);
            executorService.shutdown();
            if (!executorService.awaitTermination(timeout, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }

            List<Metar> metarList = new ArrayList<>();
            metarFutureList.forEach(metarFuture -> {
                try {
                    Metar metar = metarFuture.get();
                    if (metar != null) {
                        metarList.add(metar);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Failed to get Metar data", e);
                    Thread.currentThread().interrupt();
                }
            });

            // Storing the Metar Data
            metarService.storeMetarData(metarList);

        } catch (InterruptedException e) {
            logger.error("Error while refreshing Metar data for subscribed stations");
            Thread.currentThread().interrupt();
        }
    }

    /***
     *
     */
    @Scheduled(fixedDelayString = "${station.metar.data.refresh.interval}")
    public void refreshStationSummary() {
        try {
            stationService.getStationFromNWS();
        } catch (MetarException e) {
            logger.error("Error while refreshing Station data");
        }
    }


}