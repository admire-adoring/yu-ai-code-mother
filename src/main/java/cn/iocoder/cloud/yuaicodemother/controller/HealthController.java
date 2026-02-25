package cn.iocoder.cloud.yuaicodemother.controller;

import cn.iocoder.cloud.yuaicodemother.common.BaseResponse;
import cn.iocoder.cloud.yuaicodemother.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 测试接口
 * @Author Liu Yang
 * @Date 2026/2/25 23:40
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/check")
    public BaseResponse <String> check() {
        return ResultUtils.success("ok");
    }
}
