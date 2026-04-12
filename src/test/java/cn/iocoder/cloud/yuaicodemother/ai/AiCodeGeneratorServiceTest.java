package cn.iocoder.cloud.yuaicodemother.ai;

import cn.iocoder.cloud.yuaicodemother.ai.model.HtmlCodeResult;
import cn.iocoder.cloud.yuaicodemother.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode () {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("请生成一个HTML页面，做个程序员的博客，不超过20行");
        Assertions.assertNotNull(result)  ;
    }

    @Test
    void generateMultiFileCode () {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode("请生成一个HTML页面，功能是程序员的留言板，不超过50行");
        Assertions.assertNotNull(result)  ;
    }
}
