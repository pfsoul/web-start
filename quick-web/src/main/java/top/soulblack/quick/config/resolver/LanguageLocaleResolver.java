package top.soulblack.quick.config.resolver;


import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import top.soulblack.quick.common.enums.LanguageEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class LanguageLocaleResolver extends AcceptHeaderLocaleResolver {

    private static final String LANGUAGE_HEADER = "language";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String language = request.getHeader(LANGUAGE_HEADER);
        if (StringUtils.isBlank(language)) {
            return super.resolveLocale(request);
        }
        LanguageEnum languageEnum = LanguageEnum.byHeader(language);
        if (languageEnum == null) {
            return super.resolveLocale(request);
        }
        return languageEnum.getLocale();
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
