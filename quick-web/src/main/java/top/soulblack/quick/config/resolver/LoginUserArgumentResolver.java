package top.soulblack.quick.config.resolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.soulblack.quick.common.annotation.LoginUser;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String USER_NAME_HEADER = "username";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String userName = webRequest.getHeader(USER_NAME_HEADER);
        if (StringUtils.isBlank(userName)) {
            throw new RuntimeException("用户不存在");
        }
        // 获取用户
        return null;
    }
}
