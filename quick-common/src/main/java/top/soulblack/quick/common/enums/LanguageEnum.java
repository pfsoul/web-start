package top.soulblack.quick.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
@AllArgsConstructor
public enum LanguageEnum {

    ENGLISH("en", Locale.ENGLISH),
    CHINESE("ch", Locale.CHINESE),
    THAI("th", new Locale("th"));

    private final String language;

    private final Locale locale;

    public static LanguageEnum byHeader(String name){
        return Arrays.stream(LanguageEnum.values())
                .filter(v -> v.getLanguage().equals(name))
                .findFirst()
                .orElse(CHINESE);
    }
}
