package pro.patrykkrawczyk.fxpricehandler.web.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pro.patrykkrawczyk.fxpricehandler.FxPriceHandlerApp;
import pro.patrykkrawczyk.fxpricehandler.domain.MarketRate;
import pro.patrykkrawczyk.fxpricehandler.repository.MarketRateRepository;
import pro.patrykkrawczyk.fxpricehandler.service.MarketRateService;
import pro.patrykkrawczyk.fxpricehandler.service.mapper.MarketRateMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pro.patrykkrawczyk.fxpricehandler.web.rest.TestUtil.timestampToString;

/**
 * Integration tests for the {@link MarketRateResource} REST controller.
 */
@SpringBootTest(classes = FxPriceHandlerApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class MarketRateResourceIT {

    @Autowired
    private MarketRateRepository marketRateRepository;

    @Autowired
    private MarketRateMapper marketRateMapper;

    @Autowired
    private MarketRateService marketRateService;

    @Autowired
    private MockMvc restMarketRateMockMvc;

    @Test
    @Transactional
    public void getAllMarketRateLatest() throws Exception {
        // given EUR/PLN
        var currencyPairEurPln = "EUR/PLN";

        var newMarketRateEurPln = marketRateFactory(randomUUID().toString(), currencyPairEurPln, now());
        var oldMarketRateEurPln = marketRateFactory(randomUUID().toString(), currencyPairEurPln,
                now().minusSeconds(60));

        // given USD/JPN
        var currencyPairUsdJpn = "USD/JPN";

        var newMarketRateUsdJpn = marketRateFactory(randomUUID().toString(), currencyPairUsdJpn, now());
        var oldMarketRateUsdJpn = marketRateFactory(randomUUID().toString(), currencyPairUsdJpn,
                now().minusSeconds(60));

        marketRateRepository.saveAll(List.of(newMarketRateEurPln, oldMarketRateEurPln,
                newMarketRateUsdJpn, oldMarketRateUsdJpn));

        var matcherEurPln = String.format("$.[?(@.currencyPair=='%s')]", currencyPairEurPln);
        var matcherUsdJpn = String.format("$.[?(@.currencyPair=='%s')]", currencyPairUsdJpn);

        // then
        restMarketRateMockMvc.perform(get("/api/market-rates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath(matcherEurPln + ".feedId").value(hasItem(newMarketRateEurPln.getFeedId())))
                .andExpect(jsonPath(matcherEurPln + ".currencyPair").value(hasItem(newMarketRateEurPln.getCurrencyPair())))
                .andExpect(jsonPath(matcherEurPln + ".bid").value(hasItem(newMarketRateEurPln.getBid().intValue())))
                .andExpect(jsonPath(matcherEurPln + ".ask").value(hasItem(newMarketRateEurPln.getAsk().intValue())))
                .andExpect(jsonPath(matcherEurPln + ".timestamp").value(hasItem(timestampToString(newMarketRateEurPln.getTimestamp()))))
                .andExpect(jsonPath(matcherEurPln + ".margin").value(hasItem(newMarketRateEurPln.getMargin().intValue())))
                .andExpect(jsonPath(matcherUsdJpn + ".feedId").value(hasItem(newMarketRateUsdJpn.getFeedId())))
                .andExpect(jsonPath(matcherUsdJpn + ".currencyPair").value(hasItem(newMarketRateUsdJpn.getCurrencyPair())))
                .andExpect(jsonPath(matcherUsdJpn + ".bid").value(hasItem(newMarketRateUsdJpn.getBid().intValue())))
                .andExpect(jsonPath(matcherUsdJpn + ".ask").value(hasItem(newMarketRateUsdJpn.getAsk().intValue())))
                .andExpect(jsonPath(matcherUsdJpn + ".timestamp").value(hasItem(timestampToString(newMarketRateUsdJpn.getTimestamp()))))
                .andExpect(jsonPath(matcherUsdJpn + ".margin").value(hasItem(newMarketRateUsdJpn.getMargin().intValue())));
    }

    @Test
    @Transactional
    public void getMarketRateLatest() throws Exception {
        // given
        var currencyPair = "EUR/PLN";

        var newMarketRate = marketRateFactory(randomUUID().toString(), currencyPair, now());
        var oldMarketRate = marketRateFactory(randomUUID().toString(), currencyPair,
                now().minusSeconds(60));

        marketRateRepository.saveAll(List.of(newMarketRate, oldMarketRate));

        // then
        restMarketRateMockMvc.perform(get("/api/market-rates/{currencyPair}", currencyPair))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.feedId").value(newMarketRate.getFeedId()))
                .andExpect(jsonPath("$.currencyPair").value(newMarketRate.getCurrencyPair()))
                .andExpect(jsonPath("$.bid").value(newMarketRate.getBid().intValue()))
                .andExpect(jsonPath("$.ask").value(newMarketRate.getAsk().intValue()))
                .andExpect(jsonPath("$.timestamp").value(timestampToString(newMarketRate.getTimestamp())))
                .andExpect(jsonPath("$.margin").value(newMarketRate.getMargin().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMarketRate() throws Exception {
        // then
        restMarketRateMockMvc.perform(get("/api/market-rates/{left}/{right}", "UNK", "OWN"))
                .andExpect(status().isNotFound());
    }

    private MarketRate marketRateFactory(String feedId, String currencyPair, LocalDateTime timestamp) {
        return MarketRate.builder()
                .feedId(feedId)
                .currencyPair(currencyPair)
                .bid(BigDecimal.ONE)
                .ask(BigDecimal.TEN)
                .timestamp(timestamp)
                .margin(BigDecimal.ZERO)
                .build();
    }
}
