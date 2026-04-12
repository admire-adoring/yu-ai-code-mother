package cn.iocoder.cloud.yuaicodemother.core.parser;



import cn.iocoder.cloud.yuaicodemother.exception.BusinessException;
import cn.iocoder.cloud.yuaicodemother.exception.ErrorCode;
import cn.iocoder.cloud.yuaicodemother.model.enums.CodeGenTypeEnum;

/**
 * @Description 代码解析执行器
 * 根据代码生成类型执行相应的解析逻辑
 * @Author Liu Yang
 * @Date 2026/4/12 15:54
 */
public class CodeParserExecutor {

    private static final HtmlCodeParser htmlCodeParser = new HtmlCodeParser();

    private static final MultiFileCodeParser multiFileCodeParser = new MultiFileCodeParser();

    /**
     * 执行代码解析
     *
     * @param codeContent 代码内容
     * @param codeGenType 代码生成类型
     * @return 解析结果（HtmlCodeResult 或 MultiFileCodeResult）
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlCodeParser.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parseCode(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType);
        };
    }
}
