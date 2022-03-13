package com.example.airportservice;

import com.example.airportservice.dto.MetarDTO;
import com.example.airportservice.dto.SubscriptionDTO;
import com.example.airportservice.scheduler.ScheduleService;
import com.example.airportservice.services.StationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AirportServiceApplication.class)
public class AirportServiceApplicationTests extends AbstractTestNGSpringContextTests {

    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(AirportServiceApplication.class);
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    StationService stationService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeClass
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test(priority = 1)
    public void testStoreMetarData() throws Exception {
        stationService.getStationFromNWS();
        MetarDTO metarDTO = new MetarDTO();
        metarDTO.setData("AABP 230230Z 27009KT 5000 HZ SCT010 SCT020 BKN100 24/23 Q1008 NOSIG");
        String json = new ObjectMapper().writeValueAsString(metarDTO);
        MvcResult mvcResult = mockMvc.perform(
                        post("/airport/KCKN/METAR").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn();
        LOG.info("Response : {}", mvcResult.getResponse().getContentAsString());
    }

    @Test(priority = 2)
    public void testStoreMetarDataFailure() throws Exception {
        MetarDTO metarDTO = new MetarDTO();
        metarDTO.setData("test");
        String json = new ObjectMapper().writeValueAsString(metarDTO);
        MvcResult mvcResult = mockMvc.perform(
                        post("/airport/TEST/METAR").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andReturn();
        LOG.info("Response : {}", mvcResult.getResponse().getContentAsString());
    }

    @Test(priority = 3)
    public void testTestStoreMetarData() throws Exception {
        stationService.getStationFromNWS();

        MetarDTO metarDTO = new MetarDTO();
        metarDTO.setIcaoCode("KCKN");
        metarDTO.setData("test");
        List<MetarDTO> metarDTOList = new ArrayList<>();
        metarDTOList.add(metarDTO);
        String json = new ObjectMapper().writeValueAsString(metarDTOList);
        MvcResult mvcResult = mockMvc.perform(
                        post("/airport/METAR").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn();
        LOG.info("Response : {}", mvcResult.getResponse().getContentAsString());
    }

    @Test(priority = 4)
    public void testGetMetarData() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/airport/KCKN/METAR"))
                .andExpect(status().isOk())
                .andReturn();
        LOG.info("Response : {}", mvcResult.getResponse().getContentAsString());
    }

    @Test(priority = 5)
    public void testSubscribe() throws Exception {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setIcaoCode("KCKN");
        subscriptionDTO.setActive(true);
        String json = new ObjectMapper().writeValueAsString(subscriptionDTO);
        MvcResult mvcResult = mockMvc.perform(
                        post("/airport/subscriptions").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn();
        LOG.info("Response : {}", mvcResult.getResponse().getContentAsString());
    }

    @Test(priority = 6)
    public void testSubscribeInvalidCode() throws Exception {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setIcaoCode("TEST");
        subscriptionDTO.setActive(true);
        String json = new ObjectMapper().writeValueAsString(subscriptionDTO);
        MvcResult mvcResult = mockMvc.perform(
                        post("/airport/subscriptions").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andReturn();
        LOG.info("Response : {}", mvcResult.getResponse().getContentAsString());
    }

    @Test(priority = 6)
    public void testGetSubscriptions() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/airport/subscriptions"))
                .andExpect(status().isOk())
                .andReturn();
        LOG.info("Response : {}", mvcResult.getResponse().getContentAsString());
    }

    @Test(priority = 7)
    public void testUnsubscribe() throws Exception {
        mockMvc.perform(delete("/airport/subscriptions/KCKN"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test(priority = 8)
    public void testGetStationFromDB() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/airport/stations/KC"))
                .andExpect(status().isOk())
                .andReturn();
        LOG.info("Response : {}", mvcResult.getResponse().getContentAsString());
    }

    @Test(priority = 9)
    public void testRefreshStationSummary() throws Exception {
        scheduleService.refreshStationSummary();
    }

    @Test(priority = 10)
    public void testRefreshSubscribedStationMetarData() throws Exception {
        scheduleService.scheduleFixedRateTask();
    }

    @Test(priority = 11)
    public void testRefreshStationSummaryFailure() throws Exception {
        stationService.metarDataUrl = "";
        scheduleService.refreshStationSummary();
    }

    @Test(priority = 12)
    public void testRefreshSubscribedStationMetarDataFailure() throws Exception {
        scheduleService.url = "";
        scheduleService.scheduleFixedRateTask();
    }


}