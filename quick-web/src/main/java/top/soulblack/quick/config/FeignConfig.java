package top.soulblack.quick.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import top.soulblack.quick.log.FeignLog;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
@Configuration
public class FeignConfig {

    @Bean
    @Primary
    Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    Logger feignLogger() {
        return new FeignLog();
    }
}
