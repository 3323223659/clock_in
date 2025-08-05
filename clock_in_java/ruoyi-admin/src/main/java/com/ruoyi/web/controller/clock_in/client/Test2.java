package com.ruoyi.web.controller.clock_in.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/9/28 下午9:26
 */
@RestController()
@RequestMapping("/web")
public class Test2 {
    @GetMapping("/")
    public String getWeb() {
        return "web";
    }
}
