package pro.patrykkrawczyk.fxpricehandler.service.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import pro.patrykkrawczyk.fxpricehandler.FxPriceHandlerApp;
import pro.patrykkrawczyk.fxpricehandler.config.MarginProperties;
import pro.patrykkrawczyk.fxpricehandler.domain.MarketRate;
import pro.patrykkrawczyk.fxpricehandler.repository.MarketRateRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static pro.patrykkrawczyk.fxpricehandler.web.rest.TestUtil.timestampToString;

/**
 * Integration tests for the {@link CsvMessageListener} message listener.
 */
@SpringBootTest(classes = FxPriceHandlerApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class CsvMessageListenerIT {

    @Autowired
    private MarketRateRepository marketRateRepository;

    @Autowired
    private CsvMessageListener csvMessageListener;

    @MockBean
    private MarginProperties marginProperties;

    @Value("classpath:samples/market-rates.csv")
    private File marketRatesFile;

    @Test
    @Transactional
    public void processMessage() throws IOException {
        // given
        var fileContent = Files.readString(marketRatesFile.toPath());

        var mockMargin = BigDecimal.ZERO;
        when(marginProperties.getValue()).thenReturn(mockMargin);

        // when
        csvMessageListener.onMessage(fileContent);

        // then
        var feedToMarketRate = marketRateRepository.findAll()
                .stream()
                .collect(Collectors.toMap(MarketRate::getFeedId, Function.identity()));

        var marketRate106 = feedToMarketRate.get("106");

        assertMarketRate(
                marketRate106,
                "106",
                "EUR/USD",
                BigDecimal.valueOf(11000, 4),
                BigDecimal.valueOf(12000, 4),
                "01-06-2020 12:01:01:001",
                mockMargin
        );

        var marketRate107 = feedToMarketRate.get("107");
        assertMarketRate(
                marketRate107,
                "107",
                "EUR/JPY",
                BigDecimal.valueOf(11960, 2),
                BigDecimal.valueOf(11990, 2),
                "01-06-2020 12:01:02:002",
                mockMargin
        );

        var marketRate108 = feedToMarketRate.get("108");
        assertMarketRate(
                marketRate108,
                "108",
                "GBP/USD",
                BigDecimal.valueOf(12500, 4),
                BigDecimal.valueOf(12560, 4),
                "01-06-2020 12:01:02:002",
                mockMargin
        );

        var marketRate109 = feedToMarketRate.get("109");
        assertMarketRate(
                marketRate109,
                "109",
                "GBP/USD",
                BigDecimal.valueOf(12499, 4),
                BigDecimal.valueOf(12561, 4),
                "01-06-2020 12:01:02:100",
                mockMargin
        );

        var marketRate110 = feedToMarketRate.get("110");
        assertMarketRate(
                marketRate110,
                "110",
                "EUR/JPY",
                BigDecimal.valueOf(11961, 2),
                BigDecimal.valueOf(11991, 2),
                "01-06-2020 12:01:02:110",
                mockMargin
        );
    }

    private void assertMarketRate(MarketRate marketRate, String feedId, String currencyPair, BigDecimal bid,
                                  BigDecimal ask, String timestamp, BigDecimal margin) {

        assertAll(
                () -> assertNotNull(marketRate.getId()),
                () -> assertEquals(feedId, marketRate.getFeedId()),
                () -> assertEquals(currencyPair, marketRate.getCurrencyPair()),
                () -> assertEquals(bid, marketRate.getBid()),
                () -> assertEquals(ask, marketRate.getAsk()),
                () -> assertEquals(timestamp, timestampToString(marketRate.getTimestamp())),
                () -> assertEquals(margin, marketRate.getMargin())
        );
    }
}
