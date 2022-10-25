package pro.patrykkrawczyk.fxpricehandler.service.listener;

import com.opencsv.CSVParser;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.patrykkrawczyk.fxpricehandler.service.MarketRateService;
import pro.patrykkrawczyk.fxpricehandler.service.dto.MarketRateDto;

import java.io.IOException;
import java.io.StringReader;

@Component
@RequiredArgsConstructor
public class CsvMessageListener implements MessageListener {

    private final MarketRateService marketRateService;
    private final CSVParser csvParser;

    @Override
    public void onMessage(String message) throws IOException {
        var stringReader = new StringReader(message);

        var csvReader = new CSVReaderBuilder(stringReader)
                .withCSVParser(csvParser)
                .build();

        new CsvToBeanBuilder<MarketRateDto>(csvReader)
                .withType(MarketRateDto.class)
                .build()
                .parse()
                .forEach(marketRateService::save);
    }
}
