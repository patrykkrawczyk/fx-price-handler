package pro.patrykkrawczyk.fxpricehandler.config;

import com.opencsv.CSVParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;

@Configuration
public class CsvReaderConfiguration {

    @Bean
    public CSVParser csvParser() {
        return new CSVParser() {
            @Override
            protected String[] parseLine(String nextLine, boolean multi) throws IOException {
                String[] line = super.parseLine(nextLine, multi);
                Arrays.setAll(line, i -> line[i].trim());
                return line;
            }
        };
    }
}
