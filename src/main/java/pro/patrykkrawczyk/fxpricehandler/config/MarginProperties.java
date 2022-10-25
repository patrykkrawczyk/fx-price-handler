package pro.patrykkrawczyk.fxpricehandler.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "app.margin")
public class MarginProperties {

    private BigDecimal value;
}