import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Created by Аглиуллины on 21.08.2017.
 */
public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new BotManager());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
