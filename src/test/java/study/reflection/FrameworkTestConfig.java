package study.reflection;

import com.diy.app.annotation.Bean;
import com.diy.app.annotation.Configuration;

@Configuration
public class FrameworkTestConfig {
    @Bean
    public String testMessage () {
        return "DI Sucess";
    }
}
