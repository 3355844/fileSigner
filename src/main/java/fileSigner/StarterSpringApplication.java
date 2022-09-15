package fileSigner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import telegramBot.TelegramBot;

@SpringBootApplication
public class StarterSpringApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(StarterSpringApplication.class);

	public static void main(String[] args) {
		logger.info("=======FILE_SIGNER_APPLICATION_START ======");
		long time = System.currentTimeMillis();
		SpringApplication.run(StarterSpringApplication.class, args);
		logger.info("=======FILE_SIGNER_APPLICATION_RUNNIING by {} ======", System.currentTimeMillis() - time);
	}

	@Bean(name = "telegramBot", initMethod = "init")
	public TelegramBot getTelegramBot() {
		return new TelegramBot();
	}

	/*
	 * @Override protected SpringApplicationBuilder
	 * configure(SpringApplicationBuilder builder) { return
	 * builder.sources(StarterSpringApplication.class); }
	 */

}
