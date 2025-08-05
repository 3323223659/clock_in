package com.ruoyi.web.controller.clock_in.app;

import com.ruoyi.clock_in.domain.AuthDto;
import com.ruoyi.clock_in.domain.vo.AuthVo;
import com.ruoyi.clock_in.service.AuthService;
import com.ruoyi.common.core.domain.AjaxResult;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.Base64;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/9/29 下午3:18
 */
@RequestMapping("/app/clock_in")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/v1/login")
    public AjaxResult login(@RequestBody AuthDto authDto) {
        System.out.println(authDto);
        AuthVo login = authService.login(authDto);
        if ("/login!welcome.action".equals(login.getMsg())) {
            return AjaxResult.success(login);
        } else {
            return AjaxResult.error(login.getMsg(),login);
        }
    }
}
