package fileSigner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class StarterSpringApplication extends SpringBootServletInitializer {

	private static final Logger logger = LogManager.getLogger(StarterSpringApplication.class);
	
	public static void main(String[] args) {
		logger.info("=======RUN_FILE_SIGNER_APPLICATION======");
		SpringApplication.run(StarterSpringApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(StarterSpringApplication.class);
	}

}
