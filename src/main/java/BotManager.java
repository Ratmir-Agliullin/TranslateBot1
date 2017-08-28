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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Аглиуллины on 20.08.2017.
 */
public class BotManager extends TelegramLongPollingBot {
private static int flag=0;
private static String keyString="Choose button";

    private static String rus = null;
    private static String eng = null;
    private void InlineKeyBoardAddEng( Update update){
        long chat_id = update.getMessage().getChatId();
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id).setText("put words");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        rowInline.add(new InlineKeyboardButton().setText("getEnglish").setCallbackData("getEng"));
//        rowInline.add(new InlineKeyboardButton().setText("getRussian").setCallbackData("getRus"));
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
            .setChatId(chat_id).setText(" ");
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
        // Add the first row to the keyboard

        keyboard.add(row);
        KeyboardRow row2 = new KeyboardRow();
        row2.add("yandex en-ru");
        row2.add("yandex ru-en");
        keyboard.add(row2);
        // Set the keyboard to the markup
        keyboardMarkup.setResizeKeyboard(true);
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
        KeyBoard(chatId);
        Message message = update.getMessage();

        if (flag == 0) {
            if (message.getText().equals("get English")) {//InlineKeyBoardAddEng(update);
                flag = 2;
                keyString = "put english word";
            } else if (message.getText().equals("get Russian")) {
                flag = 3;
                keyString = "put russian word";
            } else if (message.getText().equals("Add new")) {
                flag = 1;
                keyString = "put english word";
            } else
                SendText(chatId, "I don't know what you written");
        } else if (flag == 2) {
            try {

                rus = message.getText();
                DBManager.getEngList(rus).stream().forEach(s->SendText(update.getMessage().getChatId(), s));
              //  SendText(update.getMessage().getChatId(), getEnglish(rus));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                flag = 0;
            }
        } else if (flag == 3) {
            try {
                eng = message.getText();

                SendText(update.getMessage().getChatId(),chatId.toString());
                SendText(update.getMessage().getChatId(),getRussian(eng));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                flag = 0;
            }
        } else if (flag == 1) {
            eng = message.getText();
          //     if(DBManager.getUniqueEnglish(eng)==false){
            SendText(update.getMessage().getChatId(), "Введите слово по-русски");
            flag = 4;
      //  } else {
//            SendText(update.getMessage().getChatId(), "Такое слово уже есть в базе. Начните заново");
//            flag = 0;
     //   }
    }

             else
                if(flag==4){
                    keyString = "put russian word";
                    rus = message.getText();
                    //if(DBManager.getUniqueRussian(rus)==false){
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
