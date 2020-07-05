package repositories.interfaces;

import handlers.Language;
import models.User;

public interface IUserRepository {
    Language getUserLanguage(int user_id);
    void add(User user);

    void update(User user);
}
