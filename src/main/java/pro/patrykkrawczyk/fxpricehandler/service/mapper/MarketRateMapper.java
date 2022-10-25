package pro.patrykkrawczyk.fxpricehandler.service.mapper;


import org.mapstruct.Mapper;
import pro.patrykkrawczyk.fxpricehandler.domain.MarketRate;
import pro.patrykkrawczyk.fxpricehandler.service.dto.MarketRateDto;

/**
 * Mapper for the entity {@link MarketRate} and its DTO {@link MarketRateDto}.
 */
@Mapper(componentModel = "spring")
public interface MarketRateMapper extends EntityMapper<MarketRateDto, MarketRate> {

}
