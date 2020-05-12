package wb.im.control;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"wb.im"})
@MapperScan("wb.im.domain.mapper")
@Slf4j
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class);
        log.info("消息系统服务端启动完成");
    }
}
