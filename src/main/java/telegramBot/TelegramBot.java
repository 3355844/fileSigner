package telegramBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
	
	/*
	 * @PostConstruct public void init() { logger.info("begin TelegramBot init");
	 * logger.info("call first  time onUpdateReceived"); onUpdateReceived(new
	 * Update()); logger.info("TelegramBot up success"); }
	 */
	
	@Value("${bot.username}")
	private String botUserName;
	@Value("${bot.token}")
	private String botToken;

	@Override
	public void onUpdateReceived(Update update) {
		logger.info("botUserName: " + botUserName + ", botToken: " + botToken);
		if (update.hasMessage()) {
			Message message = update.getMessage();
			if (message.hasText()) {
				String text = message.getText();
				logger.info(text);
				SendMessage sm = new SendMessage();
				sm.setText("input message : " + text);
				sm.setChatId(message.getChatId());
				try {
					execute(sm);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getBotUsername() {
		return botUserName;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}
}
