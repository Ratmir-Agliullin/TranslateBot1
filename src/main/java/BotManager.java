import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
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
                .setChatId(chat_id).setText("put russian");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        rowInline.add(new InlineKeyboardButton().setText("getEnglish").setCallbackData("getEng"));
//        rowInline.add(new InlineKeyboardButton().setText("getRussian").setCallbackData("getRus"));
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

        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id);
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

        // Set the keyboard to the markup
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
        long chatId = update.getMessage().getChatId();

        Message message = update.getMessage();
if(message.getText().split(" ")[0].equals("getEng")){
    rus =message.getText().split(" ")[1];
    try {
        SendText(chatId,getEnglish(rus));
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
}
else
if(message.getText().split(" ")[0].equals("getRus")){
    eng = message.getText().split(" ")[1];
    try {
        SendText(chatId,getRussian(eng));
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
} else
if(message.getText().split(" ")[0].equals("addNew")){
    String mas = message.getText().split(" ")[1];
    eng = mas.split(",")[0];
    rus = mas.split(",")[1];
    try {
        AddNewEntity(eng,rus);
        SendText(chatId,"Done");
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
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
