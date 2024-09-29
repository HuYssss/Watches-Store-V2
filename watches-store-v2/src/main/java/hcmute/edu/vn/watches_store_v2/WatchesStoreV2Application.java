package hcmute.edu.vn.watches_store_v2;

import hcmute.edu.vn.watches_store_v2.config.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
public class WatchesStoreV2Application {

	public static void main(String[] args) {
		SpringApplication.run(WatchesStoreV2Application.class, args);
	}

}
