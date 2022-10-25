package pro.patrykkrawczyk.fxpricehandler.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.patrykkrawczyk.fxpricehandler.domain.MarketRate;
import pro.patrykkrawczyk.fxpricehandler.service.MarketRateService;
import pro.patrykkrawczyk.fxpricehandler.service.dto.MarketRateDto;
import pro.patrykkrawczyk.fxpricehandler.web.util.ResponseUtil;

import java.util.List;

/**
 * REST controller for managing {@link MarketRate}.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MarketRateResource {

    private final MarketRateService marketRateService;

    /**
     * {@code GET  /market-rates} : get all the market-rates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of market-rates in body.
     */
    @GetMapping("/market-rates")
    public ResponseEntity<List<MarketRateDto>> getMarketRates() {
        log.debug("REST request to get a list of MarketRate");

        var marketRates = marketRateService.findAll();

        return ResponseEntity.ok().body(marketRates);
    }

    /**
     * {@code GET  /market-rates/{left}/{right}} : get the market-rate by currencyPair.
     *
     * @param left  the left side of currencyPair of the MarketRateDto to retrieve.
     * @param right the right side of currencyPair of the MarketRateDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marketRateDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = "/market-rates/{left}/{right}")
    public ResponseEntity<MarketRateDto> getMarketRate(@PathVariable String left, @PathVariable String right) {
        var currencyPair = String.format("%s/%s", left, right).toUpperCase();

        log.debug("REST request to get MarketRate : {}", currencyPair);

        var marketRateDto = marketRateService.findOne(currencyPair);

        return ResponseUtil.wrapOrNotFound(marketRateDto);
    }
}
