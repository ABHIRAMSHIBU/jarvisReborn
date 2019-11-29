/*
Copyleft (C) 2018  ARCtotal
Copyleft (C) 2018  Abhiram Shibu

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package tg;

import jarvisReborn.Core;
import jarvisReborn.Details;
import jarvisReborn.GUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import org.telegram.telegrambots.meta.api.methods.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import CommandHandlers.MainCMDHandler;
import Sockets.PythonServer;

import java.lang.Runtime;
public class Telegram extends TelegramLongPollingBot {
	GUI gui;
	PythonServer python;

	public Telegram(GUI gui) {
		this.gui = gui;
		python = Core.python;
	}

	@Override
	public void onUpdateReceived(Update update) {
		boolean voice = false;
		// We check if the update has a message and the message has text
		if (update.hasMessage()) {
			// System.out.println("Telegram Some sort of message got into");
			try {
				update.getMessage().getVoice().getDuration();
				voice = true;
			} catch (NullPointerException e) {
				voice = false;
			}
			if (voice) {

				// System.out.println(""+update.getMessage().getVoice().getFileId());
				GetFile file = new GetFile(); // TO get a file first getfile needs to be there
				file.setFileId(update.getMessage().getVoice().getFileId()); // set the file id in getfile
				try {
					file.validate(); // Idk why we even need this shit
					org.telegram.telegrambots.meta.api.objects.File teleTempFile = null; // Create a (telegram)File pointer
					try {
						teleTempFile = execute(file); // Execute GetFile and we get (telegram)File pointer
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// System.out.println(""+file2.getFilePath());
					try {
						File teleInputFile = downloadFile(teleTempFile.getFilePath()); // Then we need to get path and
																						// download which will be
																						// (java)File
						FileInputStream inputStream = new FileInputStream(teleInputFile); // Start Input from file
						File outputFile = new File("/tmp/test.ogg"); // Create output file
						FileOutputStream outputStream2 = new FileOutputStream(outputFile); // Create output stream to
																							// new file
						while (inputStream.available() > 0) { // If file input buffer is not empty then
							outputStream2.write(inputStream.read()); // Read the file byte by byte
						}
						inputStream.close();
						outputStream2.close();
						File testFile = new File("/tmp/test.wav");
						if (testFile.exists()) {
							testFile.delete();
						}
						testFile = new File("/tmp/RESULT.txt");
						if (testFile.exists()) {
							testFile.delete();
						}
						// System.out.println("Trying to convert");
						Runtime.getRuntime().exec("ffmpeg -i /tmp/test.ogg /tmp/test.wav");
						// System.out.print("Exited");
						// File speechFile= new File("/tmp/test.flac");
						// SR sr = new SR("/tmp/test.flac");
						Runtime.getRuntime().exec("python src/SR.py");
						long time = System.currentTimeMillis();
						File result = new File("/tmp/RESULT.txt");
						while (result.length() == 0) {
							// System.out.println("Retrying!");
							// result = new File("/tmp/RESULT.txt");
							Thread.sleep(500);
						}
						time = System.currentTimeMillis() - time;
						System.out.println("SR cputime:" + (time / 1000.0));
						result = new File("/tmp/RESULT.txt");
						String temp;
						BufferedReader br = new BufferedReader(new FileReader(result));
						temp = br.readLine();
						System.out.println("Decoded message is:" + temp);
						br.close();
						SendMessage message = new SendMessage();
						message.setChatId(update.getMessage().getChatId());
						message.setText("Time taken :" + time / 1000.0);
						execute(message);
						message = new SendMessage();
						message.setChatId(update.getMessage().getChatId());
						message.setText("Decoded :" + temp);
						execute(message);
						// Cleanup needed
						if (temp.toLowerCase().contains("light")) {
							if (temp.toLowerCase().contains("on")) {
								MainCMDHandler c = new MainCMDHandler("$set 13 1 " + Details.MCU, null);
								if (c.output.contains("13 on")) {
									temp = "Lights on";
								} else {
									temp = "Data mismatch! Error:3 Data:" + c.output;
								}
							} else if (temp.toLowerCase().contains("off")) {
								MainCMDHandler c = new MainCMDHandler("$set 13 0 " + Details.MCU, null);
								if (c.output.contains("13 off")) {
									temp = "Lights off";
								} else {
									temp = "Data mismatch! Error:3 Data:" + c.output;
								}
							} else {
								temp = "Please check command! Error code:1";
							}
						} else if (temp.toLowerCase().contains("fan")) {
							if (temp.toLowerCase().contains("on")) {
								MainCMDHandler c = new MainCMDHandler("$set 14 1 " + Details.MCU, null);
								if (c.output.contains("14 on")) {
									temp = "Fan on";
								} else {
									temp = "Data mismatch! Error:3 Data:" + c.output;
								}
							} else if (temp.toLowerCase().contains("off")) {
								MainCMDHandler c = new MainCMDHandler("$set 14 0 " + Details.MCU, null);
								if (c.output.contains("14 off")) {
									temp = "Fan off";
								} else {
									temp = "Data mismatch! Error:3 Data:" + c.output;
								}
							} else {
								temp = "Please check command! Error code:1";
							}
						} else {
							temp = "Please check command! Error code:2";
						}
						message = new SendMessage();
						message.setChatId(update.getMessage().getChatId());
						message.setText(temp);
						execute(message);
						/**
						 * Damn exceptions idk how they got added or how it works
						 */
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (TelegramApiValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (update.hasMessage() && update.getMessage().hasText()) {
			//
			SendMessage message = null;
			String data = update.getMessage().getText();
			if (data.equals("/runs")) {
				message = new SendMessage();
				message.setChatId(update.getMessage().getChatId());
				message.setText("Yup...!");
				gui.title.setText("Runs called");
			} else if (data.equals("/about")) {
				message = new SendMessage();
				message.setChatId(update.getMessage().getChatId());
				message.setText(Details.about);
				gui.title.setText("About called");
			} else if (data.toLowerCase().contains("run")) { // Cleanup needed
				String recvMessage = data;
				int z = recvMessage.indexOf(" ");
				recvMessage = recvMessage.substring(z + 1);
				if (recvMessage.toLowerCase().contains("light")) {
					if (recvMessage.toLowerCase().contains("on")) {
						MainCMDHandler c = new MainCMDHandler("$set 13 1 " + Details.MCU, null);
						if (c.output.contains("13 on")) {
							recvMessage = "Lights on";
						} else {
							recvMessage = "Data mismatch! Error:3 Data:" + c.output;
						}
					} else if (recvMessage.toLowerCase().contains("off")) {
						MainCMDHandler c = new MainCMDHandler("$set 13 0 " + Details.MCU, null);
						if (c.output.contains("13 off")) {
							recvMessage = "Lights off";
						} else {
							recvMessage = "Data mismatch! Error:3 Data:" + c.output;
						}
					} else {
						recvMessage = "Please check command! Error code:1";
					}
				} else if (recvMessage.toLowerCase().contains("fan")) {
					if (recvMessage.toLowerCase().contains("on")) {
						MainCMDHandler c = new MainCMDHandler("$set 14 1 " + Details.MCU, null);
						if (c.output.contains("14 on")) {
							recvMessage = "Fan on";

						} else {
							recvMessage = "Data mismatch! Error:3 Data:" + c.output;
						}
					} else if (recvMessage.toLowerCase().contains("off")) {
						MainCMDHandler c = new MainCMDHandler("$set 14 0 " + Details.MCU, null);
						if (c.output.contains("14 off")) {
							recvMessage = "Fan off";
						} else {
							recvMessage = "Data mismatch! Error:3 Data:" + c.output;
						}
					} else {
						recvMessage = "Please check command! Error code:1";
					}
				} else {
					recvMessage = "Please check command! Error code:2";
				}
				message = new SendMessage();
				message.setChatId(update.getMessage().getChatId());
				message.setText(recvMessage);
			} else if (data.length() == 1) {
				try {
					int pin = Integer.valueOf(data);
					Details.MCU = data;
					message = new SendMessage();
					message.setChatId(update.getMessage().getChatId());
					message.setText("ROOM set to " + data);
				} catch (Exception e) {
					message = new SendMessage();
					message.setChatId(update.getMessage().getChatId());
					message.setText("ROOM not set!");
				}
			} else {
				String recvMessage = update.getMessage().getText();
				Core.python.active.readString=null;
				Core.python.active.writeEnable = true;
				Core.python.active.writeString = recvMessage;
				while(true) {
					//System.out.println(Core.python.active.readString);
					recvMessage=Core.python.active.readString;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(recvMessage!=null) {
						break;
					}
				}
//				message = new SendMessage() // Create a SendMessage object with mandatory fields
//						.setChatId(update.getMessage().getChatId()).setText(recvMessage);
				message = new SendMessage() // Create a SendMessage object with mandatory fields
						.setChatId(update.getMessage().getChatId()).setText(recvMessage);
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
		return "323264313:AAHm79FmA2badJjhVW_RDXLxauZpMe2p5I8";
	}
}
