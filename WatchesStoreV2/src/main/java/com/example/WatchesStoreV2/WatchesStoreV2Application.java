package com.example.WatchesStoreV2;

import com.example.WatchesStoreV2.config.RSAKeyRecord;
import com.example.WatchesStoreV2.service.authService.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
public class WatchesStoreV2Application {

	@Autowired
	private AuthService authService;

	public static void main(String[] args) {
		SpringApplication.run(WatchesStoreV2Application.class, args);
	}

	@Component
	public class ApplicationStartup {
		@EventListener(ContextRefreshedEvent.class)
		public void onApplicationEvent() {
			authService.cleanRefreshTokenRevoked();
		}
	}

}
