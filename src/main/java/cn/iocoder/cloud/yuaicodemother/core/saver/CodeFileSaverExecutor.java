package cn.iocoder.cloud.yuaicodemother.core.saver;

import cn.iocoder.cloud.yuaicodemother.ai.model.HtmlCodeResult;
import cn.iocoder.cloud.yuaicodemother.ai.model.MultiFileCodeResult;
import cn.iocoder.cloud.yuaicodemother.exception.BusinessException;
import cn.iocoder.cloud.yuaicodemother.exception.ErrorCode;
import cn.iocoder.cloud.yuaicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * @Description
 * 代码文件保存执行器
 * 根据代码生成类型执行相应的保存逻辑
 * @Author Liu Yang
 * @Date 2026/4/12 15:59
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaver = new MultiFileCodeFileSaverTemplate();

    /**
     * 执行代码保存
     *
     * @param codeResult  代码结果对象
     * @param codeGenType 代码生成类型
     * @return 保存的目录
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType,Long appId) {
        return switch (codeGenType) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult,appId);
            case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult,appId);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType);
        };
    }
}
