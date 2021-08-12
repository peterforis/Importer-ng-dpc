package hu.dpc.phee.importerng;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImporterConfiguration {

    @Bean
    public JoltParser JoltParser() {
        return new JoltParser();
    }

}
