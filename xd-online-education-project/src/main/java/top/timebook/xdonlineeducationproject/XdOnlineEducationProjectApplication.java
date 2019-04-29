package top.timebook.xdonlineeducationproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.timebook.xdonlineeducationproject.mapper")
public class XdOnlineEducationProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(XdOnlineEducationProjectApplication.class, args);
    }
}
