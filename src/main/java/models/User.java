package models;

import handlers.Language;

public class User {
    private int id;
    private Language language;

    public User(int id, Language language) {
        setId(id);
        setLanguage(language);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
