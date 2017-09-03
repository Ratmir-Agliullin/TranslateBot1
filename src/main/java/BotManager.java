import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Аглиуллины on 20.08.2017.
 */
public class BotManager extends TelegramLongPollingBot {
private static int flag=0;
private static String keyString="Hello";
private static int keyFlag=0;
    private static String rus = null;
    private static String eng = null;
    private void InlineKeyBoardAddEng( Update update){
        long chat_id = update.getMessage().getChatId();
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id).setText("put words");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("New").setCallbackData("newEntity"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            sendMessage(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        finally {
            if  (update.hasCallbackQuery()) {
                String mas = message.getText();
                String eng = mas.split(" ")[0];
                String rus = mas.split(" ")[1];
                try {
                    DBManager.newEntity(eng,rus);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                InlineKeyBoardAddRus(update);
            }
        }

    }

    private void InlineKeyBoardAddRus( Update update){
        long chat_id = update.getMessage().getChatId();
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id).setText(keyString);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        rowInline.add(new InlineKeyboardButton().setText("Add russian").setCallbackData("newEntity"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            sendMessage(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }





    public void SendText(Long chat_id,  String out){



            SendMessage message = new SendMessage().setChatId(chat_id).setText(out);


            try {

                sendMessage(message); // Sending our message object to user

            } catch (TelegramApiException e) {

                e.printStackTrace();

            }

        }


    private void KeyBoard(long chat_id){
        SendMessage   message = new SendMessage() // Create a message object object
            .setChatId(chat_id).setText(keyString);
        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("Add new");
        row.add("get English");
        row.add("get Russian");
//        row.add("yandex en-ru");
//        row.add("yandex ru-en");


        keyboard.add(row);
        row = new KeyboardRow();
        row.add("yandex en-ru");
        row.add("yandex ru-en");
        keyboard.add(row);
        // Set the keyboard to the markup
  //      keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(keyboardMarkup);
        try {
            sendMessage(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        if(keyFlag!=0) keyString=" ";
        DBManager.DBname = "DB_"+chatId.toString();
        try {
            DBManager.CreateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        KeyBoard(chatId);
        Message message = update.getMessage();

        if (flag == 0) {
            if (message.getText().equals("get English")) {
                flag = 2;
                keyFlag=1;
            } else if (message.getText().equals("get Russian")) {
                flag = 3;
                keyFlag=1;
            } else if (message.getText().equals("Add new")) {
                flag = 1;
                keyFlag=1;
            }
            else if (message.getText().equals("yandex en-ru")) {
                flag = 5;
                keyFlag=1;          }
            else if (message.getText().equals("yandex ru-en")) {
                flag = 6; keyFlag=1;
            }
            else {
                SendText(chatId, "I don't know what you written");
                keyFlag=1;
            }
        } else if (flag == 2) {
            try {

                rus = message.getText();
                DBManager.getEngList(rus).stream().forEach(s->SendText(update.getMessage().getChatId(), s));

            } catch (SQLException e) {
                e.printStackTrace();
                SendText(update.getMessage().getChatId(),"Text not found");
                flag = 0;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
                SendText(update.getMessage().getChatId(),"Text not found");
            }finally {
                flag = 0;
            }
        } else if (flag == 3) {
            try {
                eng = message.getText();


                SendText(update.getMessage().getChatId(),getRussian(eng));
            } catch (SQLException e) {
                e.printStackTrace();
                SendText(update.getMessage().getChatId(),"Text not found");
                flag = 0;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (NullPointerException e) {
                e.printStackTrace();
                SendText(update.getMessage().getChatId(),"Text not found");
            }
            finally {
                flag = 0;
            }
        } else if (flag == 5) {
            eng = message.getText();
            try {
                SendText(update.getMessage().getChatId(),YandexManager.getTranslate(eng,"en-ru"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                flag=0;
            }
        }
        else if (flag == 6) {
            rus = message.getText();
            try {
                SendText(update.getMessage().getChatId(),YandexManager.getTranslate(rus,"ru-en"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                flag=0;
            }
        }

        else if (flag == 1) {
            eng = message.getText();

            SendText(update.getMessage().getChatId(), "Введите слово по-русски");
            flag = 4;

    }

             else
                if(flag==4){
                    keyString = "put russian word";
                    rus = message.getText();
                                 try {

                               AddNewEntity(eng,rus);
                           } catch (SQLException e) {
                               e.printStackTrace();
                           } catch (ClassNotFoundException e) {
                               e.printStackTrace();
                           }
                       finally {
                               flag=0;
                               System.out.println("Done");
                               SendText(update.getMessage().getChatId(),"Done");
                           }
                }


    }



    @Override
    public String getBotUsername() {
        return "Bot2";
    }

    @Override
    public String getBotToken() {
        return "367015082:AAHDEcfBder_4Z_oY5LWygwa3qtJlWhzE28";
    }

    public String getEnglish(String rus) throws SQLException, ClassNotFoundException {
        return DBManager.getEng(rus);
    }



    public String getRussian(String eng) throws SQLException, ClassNotFoundException {
        return DBManager.getRus(eng);
    }

  private static void AddNewEntity(String eng,String rus) throws SQLException, ClassNotFoundException {

                DBManager.newEntity(eng,rus);
    }
}
