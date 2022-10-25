package pro.patrykkrawczyk.fxpricehandler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.patrykkrawczyk.fxpricehandler.domain.MarketRate;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketRateRepository extends JpaRepository<MarketRate, Long> {

    @Query(value = "SELECT DISTINCT ON (mr.currency_pair) * FROM market_rate mr " +
            "ORDER BY mr.currency_pair, mr.timestamp DESC", nativeQuery = true)
    List<MarketRate> findAllLatest();

    //    @Query(value = "SELECT DISTINCT ON (mr.currency_pair) * FROM market_rate mr " +
//            "ORDER BY mr.currency_pair, mr.timestamp DESC", nativeQuery = true)
    Optional<MarketRate> findFirstByCurrencyPairOrderByTimestampDesc(String currencyPair);
}
