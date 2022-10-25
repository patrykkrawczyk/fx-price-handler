package pro.patrykkrawczyk.fxpricehandler.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import pro.patrykkrawczyk.fxpricehandler.FxPriceHandlerApp;
import pro.patrykkrawczyk.fxpricehandler.config.MarginProperties;
import pro.patrykkrawczyk.fxpricehandler.repository.MarketRateRepository;
import pro.patrykkrawczyk.fxpricehandler.service.dto.MarketRateDto;

import java.math.BigDecimal;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Integration tests for the {@link MarketRateService} service.
 */
@SpringBootTest(classes = FxPriceHandlerApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class MarketRateServiceIT {

    @Autowired
    private MarketRateRepository marketRateRepository;

    @Autowired
    private MarketRateService marketRateService;

    @MockBean
    private MarginProperties marginProperties;

    @Test
    @Transactional
    public void saveWithMargin() {
        // given
        var mockMargin = BigDecimal.ONE;
        var startingBid = BigDecimal.ONE;
        var startingAsk = BigDecimal.TEN;

        var marketRateDto = MarketRateDto.builder()
                .feedId(randomUUID().toString())
                .currencyPair("EUR/PLN")
                .bid(startingBid)
                .ask(startingAsk)
                .timestamp(now())
                .margin(BigDecimal.ZERO)
                .build();

        when(marginProperties.getValue()).thenReturn(mockMargin);

        // when
        var resultDto = marketRateService.save(marketRateDto);

        // then
        var expectedBid = startingBid.subtract(mockMargin);
        var expectedAsk = startingAsk.add(mockMargin);

        assertAll(
                () -> assertEquals(marketRateDto.getFeedId(), resultDto.getFeedId()),
                () -> assertEquals(marketRateDto.getCurrencyPair(), resultDto.getCurrencyPair()),
                () -> assertEquals(expectedBid, resultDto.getBid()),
                () -> assertEquals(expectedAsk, resultDto.getAsk()),
                () -> assertEquals(marketRateDto.getTimestamp().toString(), resultDto.getTimestamp().toString()),
                () -> assertEquals(mockMargin, resultDto.getMargin())
        );

        var persistedEntity = marketRateRepository.findFirstByCurrencyPairOrderByTimestampDesc(marketRateDto.getCurrencyPair())
                .orElseThrow();

        assertAll(
                () -> assertNotNull(persistedEntity.getId()),
                () -> assertEquals(resultDto.getFeedId(), persistedEntity.getFeedId()),
                () -> assertEquals(resultDto.getCurrencyPair(), persistedEntity.getCurrencyPair()),
                () -> assertEquals(resultDto.getBid(), persistedEntity.getBid()),
                () -> assertEquals(resultDto.getAsk(), persistedEntity.getAsk()),
                () -> assertEquals(resultDto.getTimestamp().toString(), persistedEntity.getTimestamp().toString()),
                () -> assertEquals(resultDto.getMargin(), persistedEntity.getMargin())
        );
    }
}
