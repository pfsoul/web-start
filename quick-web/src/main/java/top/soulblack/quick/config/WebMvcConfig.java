package top.soulblack.quick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.soulblack.quick.config.resolver.LanguageLocaleResolver;
import top.soulblack.quick.config.resolver.LoginUserArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 参数解析
     *
     * @param resolvers 解析类
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 请求头中获取登录用户（或自定义） 若从请求头直接获取有水平越权风险，正常流程应为
        resolvers.add(new LoginUserArgumentResolver());

        // 根据语言设置国际化
    }

    /**
     * 新增拦截器 filter -> interceptor -> controllerAdvice -> aspect
     * @param registry 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new LanguageLocaleResolver();
    }
}
