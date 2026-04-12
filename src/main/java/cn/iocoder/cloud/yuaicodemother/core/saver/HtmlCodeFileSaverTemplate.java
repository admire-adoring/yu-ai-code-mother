package cn.iocoder.cloud.yuaicodemother.core.saver;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.cloud.yuaicodemother.ai.model.HtmlCodeResult;
import cn.iocoder.cloud.yuaicodemother.exception.BusinessException;
import cn.iocoder.cloud.yuaicodemother.exception.ErrorCode;
import cn.iocoder.cloud.yuaicodemother.model.enums.CodeGenTypeEnum;

/**
 * @Description HTML代码文件保存器
 * @Author Liu Yang
 * @Date 2026/4/12 15:58
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        // 保存 HTML 文件
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        // HTML 代码不能为空
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码内容不能为空");
        }
    }
}
