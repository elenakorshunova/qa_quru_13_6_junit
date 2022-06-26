package guru.qa;

public enum EnumWiki {
    SUMMER("Лето"),
    WINTER("Зима");

    public final String desc;

    EnumWiki(String desc) {
        this.desc = desc;
    }
}