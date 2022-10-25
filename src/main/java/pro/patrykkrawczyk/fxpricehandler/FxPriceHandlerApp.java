package pro.patrykkrawczyk.fxpricehandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pro.patrykkrawczyk.fxpricehandler.config.MarginProperties;

@EnableConfigurationProperties(MarginProperties.class)
@SpringBootApplication
public class FxPriceHandlerApp {

    public static void main(String[] args) {
        SpringApplication.run(FxPriceHandlerApp.class, args);
    }
}
