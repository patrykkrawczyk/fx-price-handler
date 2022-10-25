package pro.patrykkrawczyk.fxpricehandler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.patrykkrawczyk.fxpricehandler.domain.MarketRate;
import pro.patrykkrawczyk.fxpricehandler.repository.MarketRateRepository;
import pro.patrykkrawczyk.fxpricehandler.service.dto.MarketRateDto;
import pro.patrykkrawczyk.fxpricehandler.service.mapper.MarketRateMapper;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link MarketRate}.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MarketRateService {

    private final MarketRateRepository marketRateRepository;
    private final MarketRateMapper marketRateMapper;
    private final MarginService marginService;

    /**
     * Save a MarketRate.
     *
     * @param marketRateDto the entity to save.
     * @return the persisted entity.
     */
    public MarketRateDto save(MarketRateDto marketRateDto) {
        log.debug("Request to save MarketRate : {}", marketRateDto);

        var dtoWithMargin = marginService.addCommission(marketRateDto);
        var marketRate = marketRateMapper.toEntity(dtoWithMargin);
        var result = marketRateRepository.save(marketRate);

        return marketRateMapper.toDto(result);
    }

    /**
     * Get all the MarketRates.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MarketRateDto> findAll() {
        log.debug("Request to get all MarketRates");

        var marketRates = marketRateRepository.findAllLatest();

        return marketRateMapper.toDto(marketRates);
    }

    /**
     * Get one MarketRate by currencyPair.
     *
     * @param currencyPair the currencyPair of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MarketRateDto> findOne(String currencyPair) {
        log.debug("Request to get MarketRate by currencyPair: {}", currencyPair);

        return marketRateRepository.findFirstByCurrencyPairOrderByTimestampDesc(currencyPair)
                .map(marketRateMapper::toDto);
    }
}
