package handlers;

public enum Language {
    ENG("en"), RUS("ru"), KAZ("kz");

    String language;

    Language(String language){
        this.language = language;
    }

    public String getLanguage(){
        return language;
    }

    public static Language convert(String st) {
        switch (st) {
            case "kz": return Language.KAZ;
            case "en": return Language.ENG;
            default: return Language.RUS;
        }
    }
}
