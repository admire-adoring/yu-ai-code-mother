package cn.iocoder.cloud.yuaicodemother;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.iocoder.cloud.yuaicodemother.mapper")
public class YuAiCodeMotherApplication {

    public static void main (String[] args) {
        SpringApplication.run(YuAiCodeMotherApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  YuAiCodeMotherApplication启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n"
        );
    }

}
