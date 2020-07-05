package handlers;

import com.vdurmont.emoji.EmojiParser;
import models.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import services.BotService;

import java.util.ArrayList;
import java.util.List;

public class FAQHandler {

    public SendMessage handleUpdate(Update update, User userData) {
        SendMessage message = new SendMessage();
        int userId = update.getMessage().getFrom().getId();
        if (userData.getLanguage() == Language.KAZ) {
            message = kazHandle(update, userData);
        } else {
            message = rusHandle(update, userData);
        }
        return message;
    }

    public SendMessage rusHandle(Update update, User userData) {
        SendMessage message = new SendMessage();
        int userId = update.getMessage().getFrom().getId();
        String inputMessage = update.getMessage().getText();
        if (inputMessage.equals(EmojiParser.parseToUnicode(":house: Вернуться в меню"))) {
            message = handleMessage(update, userData);
            message.setText(EmojiParser.parseToUnicode("Выберите нужную опцию из меню:arrow_down:"));
            return message;
        }
        message.setText(EmojiParser.parseToUnicode("Выберите нужную опцию из меню:arrow_down:"));

        message = handleMessage(update, userData);
        return message;
    }

    public SendMessage kazHandle(Update update, User userData) {
        SendMessage message = new SendMessage();
        int userId = update.getMessage().getFrom().getId();
        String inputMessage = EmojiParser.parseToUnicode(update.getMessage().getText());
        if (inputMessage.equals(EmojiParser.parseToUnicode(":house: Басты мәзір"))) {
            message = handleMessage(update, userData);
            message.setText(EmojiParser.parseToUnicode("Мәзірде берілген опцияны таңдаңыз:arrow_down:"));
            return message;
        }
        message.setText(EmojiParser.parseToUnicode("Мәзірде берілген опцияны таңдаңыз:arrow_down:"));

        message = handleMessage(update, userData);
        return message;
    }


    private SendMessage handleMessage(Update update, User userData) {
        long chat_id = update.getMessage().getChatId();
        SendMessage message = new SendMessage().setChatId(chat_id);
        Language language = userData.getLanguage();

        if (language == Language.RUS) {
            message.setText(EmojiParser.parseToUnicode("Выберите нужную опцию из меню:arrow_down:"));
        } else if (language == Language.KAZ) {
            message.setText(EmojiParser.parseToUnicode("Мәзірде берілген опцияны таңдаңыз:arrow_down:"));
        }
        String message_text = update.getMessage().getText();

//        if (message_text.equals("Сұрақ-жауап (FAQ)") || message_text.equals("Вопрос-ответ (FAQ)")) {
        if (message_text.equals(EmojiParser.parseToUnicode("Қазақша :kz:"))
                || message_text.equals(EmojiParser.parseToUnicode("Русский :ru:"))
                || message_text.equals(EmojiParser.parseToUnicode(":house: Вернуться в меню"))
                || message_text.equals(EmojiParser.parseToUnicode(":house: Басты мәзір"))
        ) {
            ReplyKeyboardMarkup keyboardMarkup = keyboardMarkupSettings();
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            List<String> categories = BotService.getInstance().
                    fetchCategoriesByParentId(0L, language);
            for (String category : categories) {
                row.add(category);
                keyboard.add(row);
                row = new KeyboardRow();
            }
            if (language == Language.RUS) {
                String answer = EmojiParser.parseToUnicode(":house: Вернуться в меню");
                row.add(answer);
                keyboard.add(row);
                row = new KeyboardRow();
                row.add("Поменять язык");
            } else if (language == Language.KAZ) {
                String answer = EmojiParser.parseToUnicode(":house: Басты мәзір");
                row.add(answer);
                keyboard.add(row);
                row = new KeyboardRow();
                row.add("Тілді ауыстыру");
            }
            keyboard.add(row);
            keyboardMarkup.setKeyboard(keyboard);
            message.setReplyMarkup(keyboardMarkup);
            return message;

        } else {
            ReplyKeyboardMarkup keyboardMarkup = keyboardMarkupSettings();
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            List<String> categories = BotService.getInstance().
                    fetchCategoriesByParentCategoryName(message_text, language);
            if (!categories.isEmpty()) {
                for (String category : categories) {
                    row.add(category);
                    keyboard.add(row);
                    row = new KeyboardRow();
                }
            }
            List<String> questions = BotService.getInstance().
                    fetchQuestionsByCategoryName(message_text, language);
            if (!questions.isEmpty()) {
                for (String question : questions) {
                    row.add(question);
                    keyboard.add(row);
                    row = new KeyboardRow();
                }
            }

            if (questions.isEmpty() && categories.isEmpty()) {
                message.setText(handleQuestion(update, userData));
                row.add(MainMenuButton(language));
                keyboard.add(row);
            } else {
                row.add(MainMenuButton(language));
                keyboard.add(row);
            }

            if (language == Language.RUS) {
                row = new KeyboardRow();
                row.add("Поменять язык");
            } else {
                row = new KeyboardRow();
                row.add("Тілді ауыстыру");
            }

            keyboard.add(row);
            keyboardMarkup.setKeyboard(keyboard);
            message.setReplyMarkup(keyboardMarkup);
            return message;
        }

    }

    public ReplyKeyboardMarkup keyboardMarkupSettings() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public String MainMenuButton(Language language) {
        String answer = "";
        if (language == Language.RUS) {
            answer = EmojiParser.parseToUnicode(":house: Вернуться в меню");

        } else if (language == Language.KAZ) {
            answer = EmojiParser.parseToUnicode(":house: Басты мәзір");
        }
        return answer;
    }

    public String handleQuestion(Update update, User userData) {
        Language language = userData.getLanguage();
        String responseToUser;
        responseToUser = BotService.getInstance().fetchAnswerByQuestion(update.getMessage().getText(), language);
        return responseToUser;
    }


}
