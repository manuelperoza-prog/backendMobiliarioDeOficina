package java27.backend.mobiliariodeoficina;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class MobiliariodeoficinaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobiliariodeoficinaApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Caracas"));
	}
}
