package pro.patrykkrawczyk.fxpricehandler.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.*;
import pro.patrykkrawczyk.fxpricehandler.domain.MarketRate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link MarketRate} entity.
 */
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MarketRateDto implements Serializable {

    @CsvBindByPosition(position = 0)
    private String feedId;

    @CsvBindByPosition(position = 1)
    private String currencyPair;

    @CsvBindByPosition(position = 2)
    private BigDecimal bid;

    @CsvBindByPosition(position = 3)
    private BigDecimal ask;

    @CsvBindByPosition(position = 4)
    @CsvDate(value = "dd-MM-yyyy HH:mm:ss:SSS")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss:SSS")
    private LocalDateTime timestamp;

    private BigDecimal margin;
}
