package pro.patrykkrawczyk.fxpricehandler.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.patrykkrawczyk.fxpricehandler.domain.MarketRate;
import pro.patrykkrawczyk.fxpricehandler.service.dto.MarketRateDto;

import java.math.BigDecimal;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

public class MarketRateMapperTest {

    private MarketRateMapper marketRateMapper;

    @BeforeEach
    public void setUp() {
        marketRateMapper = new MarketRateMapperImpl();
    }

    @Test
    public void testDtoToEntity() {
        var dto = MarketRateDto.builder()
                .feedId("test-id")
                .currencyPair("EUR/PLN")
                .bid(BigDecimal.ONE)
                .ask(BigDecimal.TEN)
                .timestamp(now())
                .margin(BigDecimal.ZERO)
                .build();

        var entity = marketRateMapper.toEntity(dto);

        assertThat(entity.getFeedId()).isEqualTo(dto.getFeedId());
        assertThat(entity.getCurrencyPair()).isEqualTo(dto.getCurrencyPair());
        assertThat(entity.getBid()).isEqualTo(dto.getBid());
        assertThat(entity.getAsk()).isEqualTo(dto.getAsk());
        assertThat(entity.getTimestamp()).isEqualTo(dto.getTimestamp());
        assertThat(entity.getMargin()).isEqualTo(dto.getMargin());
    }

    @Test
    public void testEntityToDto() {
        var entity =  MarketRate.builder()
                .id(1L)
                .feedId("test-id")
                .currencyPair("EUR/PLN")
                .bid(BigDecimal.ONE)
                .ask(BigDecimal.TEN)
                .timestamp(now())
                .margin(BigDecimal.ZERO)
                .build();

        var dto = marketRateMapper.toDto(entity);

        assertThat(dto.getFeedId()).isEqualTo(entity.getFeedId());
        assertThat(dto.getCurrencyPair()).isEqualTo(entity.getCurrencyPair());
        assertThat(dto.getBid()).isEqualTo(entity.getBid());
        assertThat(dto.getAsk()).isEqualTo(entity.getAsk());
        assertThat(dto.getTimestamp()).isEqualTo(entity.getTimestamp());
        assertThat(dto.getMargin()).isEqualTo(entity.getMargin());
    }
}
