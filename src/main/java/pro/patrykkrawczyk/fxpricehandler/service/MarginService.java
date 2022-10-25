package pro.patrykkrawczyk.fxpricehandler.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.patrykkrawczyk.fxpricehandler.config.MarginProperties;
import pro.patrykkrawczyk.fxpricehandler.domain.MarketRate;
import pro.patrykkrawczyk.fxpricehandler.service.dto.MarketRateDto;

/**
 * Service Implementation for manipulation commission of {@link MarketRate}.
 */
@Slf4j
@Service
@AllArgsConstructor
public class MarginService {

    private final MarginProperties marginProperties;

    /**
     * Add commission to bid and ask of a MarketRate
     *
     * @param marketRateDto the dto to modify.
     * @return the modified dto with updated values.
     */
    public MarketRateDto addCommission(MarketRateDto marketRateDto) {
        log.debug("Request to set commission for MarketRateDto : {}", marketRateDto);

        var margin = marginProperties.getValue();
        var bidWithMargin = marketRateDto.getBid().subtract(margin);
        var askWithMargin = marketRateDto.getAsk().add(margin);

        return MarketRateDto.builder()
                .feedId(marketRateDto.getFeedId())
                .currencyPair(marketRateDto.getCurrencyPair())
                .bid(bidWithMargin)
                .ask(askWithMargin)
                .timestamp(marketRateDto.getTimestamp())
                .margin(margin)
                .build();
    }
}
