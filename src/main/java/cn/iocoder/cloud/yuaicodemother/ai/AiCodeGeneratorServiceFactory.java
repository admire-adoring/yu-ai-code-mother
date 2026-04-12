package cn.iocoder.cloud.yuaicodemother.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiCodeGeneratorServiceFactory {

    //        @Value("${langchain4j.open-ai.chat-model.base-url}")
    //        private String baseUrl;
    //
    //        @Value("${langchain4j.open-ai.chat-model.api-key}")
    //        private String apiKey;
    //
    //        @Value("${langchain4j.open-ai.chat-model.model-name}")
    //        private String modelName;
    //
    //        @Bean
    //        public ChatModel chatModel() {
    //            return OpenAiChatModel.builder()
    //                    .baseUrl(baseUrl)
    //                    .apiKey(apiKey)
    //                    .modelName(modelName)
    //
    //                    .build();
    //        }
    //
    //        @Bean
    //        public AiCodeGeneratorService aiCodeGeneratorService(ChatModel chatModel) {
    //            return AiServices.create(AiCodeGeneratorService.class, chatModel);
    //        }
    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;

    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService () {
        //        return AiServices.create(AiCodeGeneratorService.class, chatModel);
        return AiServices.builder(AiCodeGeneratorService.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .build();
    }
}
