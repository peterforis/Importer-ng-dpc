package hu.dpc.phee.importerng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("hu.dpc.phee.importerng.db.model")
public class ImporterNgApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImporterNgApplication.class, args);
    }

}
