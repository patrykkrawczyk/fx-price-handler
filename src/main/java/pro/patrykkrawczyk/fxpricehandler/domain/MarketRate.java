package pro.patrykkrawczyk.fxpricehandler.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "market_rate")
public class MarketRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "feed_id")
    private String feedId;

    @Column(name = "currency_pair")
    private String currencyPair;

    @Column(name = "bid")
    private BigDecimal bid;

    @Column(name = "ask")
    private BigDecimal ask;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "margin")
    private BigDecimal margin;
}
