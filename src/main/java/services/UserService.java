package services;

import handlers.Language;
import models.User;
import repositories.UserRepository;
import repositories.interfaces.IUserRepository;
import services.interfaces.IUserService;

public class UserService implements IUserService {
    private final IUserRepository userRepo = new UserRepository();

    @Override
    public Language getUsersLanguage(int user_id) {
        return userRepo.getUserLanguage(user_id);
    }

    @Override
    public void createUser(User user) {
        userRepo.add(user);
    }

    @Override
    public void updateUser(User user) {
        userRepo.update(user);
    }
}
