import handlers.FAQHandler;
import handlers.Language;
import models.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import services.UserService;
import services.interfaces.IUserService;

import java.util.ArrayList;
import java.util.List;


public class TestAITUBot extends TelegramLongPollingBot {

    private final IUserService userData = new UserService();
    private final FAQHandler faqHandler = new FAQHandler();

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        } else if (update.hasCallbackQuery()) {
        }
    }

    private void handleMessage(Update update) {
        long chat_id = update.getMessage().getChatId();
        SendMessage message = new SendMessage().setChatId(chat_id);
        int user_id = update.getMessage().getFrom().getId();

        Language language = userData.getUsersLanguage(user_id);
        User user = new User(user_id, language);
        if (language == null) {
            language = Language.RUS;
            user.setLanguage(language);
            userData.createUser(user);
        }

        String message_text = update.getMessage().getText();
        if (message_text.equals("/start")) {

            message_text = "Astana IT University \n\n Приветствую Вас, абитуриент! " +
                    "Выберите язык для продолжения...\n\n Қош келдіңіз, құрметті " +
                    "абитуриент. Жалғастыру үшін тілді таңдаңыз...";
            message.setText(message_text);
            ReplyKeyboardMarkup keyboardMarkup = languageKeyboard();
            message.setReplyMarkup(keyboardMarkup);
        }
//        else if (message_text.equals("Подача документов") || message_text.equals("Құжаттар тапсыру")) {
//            message.setText("Попробуйте позже | Кейінірек көріңіз");
//            ReplyKeyboardMarkup keyboardMarkup = menuKeyboard(update, userData);
//            message.setReplyMarkup(keyboardMarkup);
//
//        }
        else if (message_text.equals("Сұрақ-жауап (FAQ)") || message_text.equals("Вопрос-ответ (FAQ)")) {
            message = faqHandler.handleUpdate(update, user);

        } else if (message_text.equals(EmojiParser.parseToUnicode("Қазақша :kz:"))) {
            if (user.getLanguage() != Language.KAZ) {
                user.setLanguage(Language.KAZ);
                userData.updateUser(user);
            }
            message = faqHandler.handleUpdate(update, user);
//            message.setText(EmojiParser.parseToUnicode("Мәзірде берілген опцияны таңдаңыз:arrow_down:"));
//            message.setReplyMarkup(menuKeyboard(update, user));

        } else if (message_text.equals(EmojiParser.parseToUnicode("Русский :ru:"))) {
            if (user.getLanguage() != Language.RUS) {
                user.setLanguage(Language.RUS);
                userData.updateUser(user);
            }
            message = faqHandler.handleUpdate(update, user);
//            message.setText(EmojiParser.parseToUnicode("Выберите нужную опцию из меню:arrow_down:"));
//            message.setReplyMarkup(menuKeyboard(update, user));

        }
//        else if (message_text.equals(EmojiParser.parseToUnicode(":house: Вернуться в меню"))) {
//            message.setText(EmojiParser.parseToUnicode("Выберите нужную опцию из меню:arrow_down:"));
//            message.setReplyMarkup(menuKeyboard(update, user));
//
//        } else if (message_text.equals(EmojiParser.parseToUnicode(":house: Басты мәзір"))) {
//            message.setText(EmojiParser.parseToUnicode("Мәзірде берілген опцияны таңдаңыз:arrow_down:"));
//            message.setReplyMarkup(menuKeyboard(update, user));
//
//        }
        else {
            message = faqHandler.handleUpdate(update, user);

        }
        sendMessage(message);
    }


    private ReplyKeyboardMarkup languageKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        String answer = EmojiParser.parseToUnicode("Қазақша :kz:");
        row.add(answer);
        answer = EmojiParser.parseToUnicode("Русский :ru:");
        row.add(answer);
//        answer = EmojiParser.parseToUnicode("English :gb:");
//        row.add(answer);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);

        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup menuKeyboard(Update update, User userData) {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        if (userData.getLanguage() == Language.RUS) {
//            row.add("Подача документов");
//            keyboard.add(row);
            row = new KeyboardRow();
            row.add("Вопрос-ответ (FAQ)");
            keyboard.add(row);
        } else {
//            row.add("Құжаттар тапсыру");
//            keyboard.add(row);
            row = new KeyboardRow();
            row.add("Сұрақ-жауап (FAQ)");
            keyboard.add(row);
        }
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;

    }


    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
//

    public String getBotUsername() {
        return "aghia7bot";
    }

    public String getBotToken() {
        return "1192545406:AAFGz89vt10kKuNtfDxbeiTsfEkT2CPV_b8";
    }
}