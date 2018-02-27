import org.telegram.telegrambots.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.*;
//import org.telegram.telegrambots..*;
class MyAmazingBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
    	if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Jarvis_telegram_bot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "453584242:AAGWcM0iJr9wUGrOYEoBtzFsKlZRKT39sSQ";
    }
}
public class Core {
	public static void main(String[] args) {
		new Core();
		ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
        	botsApi.registerBot(new MyAmazingBot());
	    } catch (TelegramApiException e) {
	    	e.printStackTrace();
	    }
	}
	Core(){
		new GUI();
	}
}