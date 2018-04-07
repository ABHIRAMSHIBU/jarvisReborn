package tg;
import jarvisReborn.Details;
import jarvisReborn.GUI;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
public class Telegram extends TelegramLongPollingBot {
	GUI gui;
    public Telegram(GUI gui) {
    	this.gui=gui;
    }
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
        	SendMessage message;
        	String data=update.getMessage().getText();
        	if(data.equals("/runs")) {
        		message = new SendMessage();
        		message.setChatId(update.getMessage().getChatId());
        		message.setText("Yup...!");
        		gui.title.setText("Runs called");
        	}
        	else if(data.equals("/about")){
        		message = new SendMessage();
        		message.setChatId(update.getMessage().getChatId());
        		message.setText(Details.about);
        		gui.title.setText("About called");
        	}
        	else {
	            message = new SendMessage() // Create a SendMessage object with mandatory fields
	                    .setChatId(update.getMessage().getChatId())
	                    .setText(update.getMessage().getText());
        	}
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        // TODO
        return "test";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "375881606:AAFhUyU6s1-AoL0FM3lib48PZNtltb_S2Pc";
    }
}