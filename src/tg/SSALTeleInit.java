package tg;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import jarvisReborn.GUI;

public class SSALTeleInit {
	public SSALTeleInit(GUI gui) {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new Telegram(gui));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
