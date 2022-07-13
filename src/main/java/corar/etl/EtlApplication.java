package corar.etl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class EtlApplication {

	private final static Logger LOGGER = LoggerFactory.getLogger(EtlApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EtlApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init(){
		LOGGER.info("Aplicacion iniciada");
	}


}
