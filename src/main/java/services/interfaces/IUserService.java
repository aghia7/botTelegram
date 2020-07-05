package services.interfaces;

import handlers.Language;
import models.User;

public interface IUserService {
    Language getUsersLanguage(int user_id);

    void createUser(User user);

    void updateUser(User user);
}
