package com.example.airportservice.controller;


import com.example.airportservice.dto.SubscriptionDTO;
import com.example.airportservice.exceptions.MetarException;
import com.example.airportservice.models.Subscription;
import com.example.airportservice.services.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/airport")
public class SubscriptionController {

    /**
     * Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    SubscriptionService subscriptionService;

    /**
     * Endpoint to subscribe station/icaoCode for Metar data
     *
     * @param subscriptionDTO subscriptionDTO
     * @throws MetarException MetarException
     */
    @PostMapping("/subscriptions")
    public void subscribe(@RequestBody SubscriptionDTO subscriptionDTO) throws MetarException {
        subscriptionService.subscribe(new Subscription(subscriptionDTO));
        subscriptionService.evictSubscriptions();
    }

    /**
     * Endpoint to get Metar Data subscriptions for station/icaoCode
     *
     * @return Subscription list
     */
    @GetMapping("/subscriptions")
    public List<Subscription> getSubscriptions() {
        return subscriptionService.getSubscriptions();
    }

    /**
     * Endpoint to unsubscribe station/icaoCode for Metar Data
     *
     * @param icaoCode icaoCode
     * @throws MetarException MetarException
     */
    @DeleteMapping("/subscriptions/{icaoCode}")
    public void unsubscribe(@PathVariable("icaoCode") String icaoCode) throws MetarException {
        subscriptionService.unsubscribe(icaoCode);
        subscriptionService.evictSubscriptions();
    }

    /**
     * Endpoint to activate / deactivate station/icaoCode for Metar Data
     *
     * @param icaoCode        icaoCode
     * @param subscriptionDTO subscriptionDTO
     * @throws MetarException
     */
    @PutMapping("/subscriptions/{icaoCode}")
    public void activate(@PathVariable("icaoCode") String icaoCode, @RequestBody SubscriptionDTO subscriptionDTO) throws MetarException {
        Subscription subscription = new Subscription();
        subscription.setIcaoCode(icaoCode);
        subscription.setActive(subscriptionDTO.getActive());
        subscriptionService.subscribe(subscription);
        subscriptionService.evictSubscriptions();
    }


}
