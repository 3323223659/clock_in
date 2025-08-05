package com.ruoyi.clock_in.service.impl;

import com.ruoyi.clock_in.domain.AuthDto;
import com.ruoyi.clock_in.domain.ClockInUser;
import com.ruoyi.clock_in.domain.vo.AuthVo;
import com.ruoyi.clock_in.domain.vo.ClockInClassroomSessionVo;
import com.ruoyi.clock_in.mapper.ClockInClassMapper;
import com.ruoyi.clock_in.mapper.ClockInUserMapper;
import com.ruoyi.clock_in.service.AuthService;
import com.ruoyi.clock_in.service.IClassroomSessionService;
import com.ruoyi.common.core.domain.AjaxResult;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/9/29 下午9:34
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private ClockInUserMapper clockInUserMapper;

    @Autowired
    private IClassroomSessionService iclassroomSessionService;

    @Resource
    private ClockInClassMapper clockInClassMapper;

    @Override
    @Transactional
    public AuthVo login(AuthDto authDto) {
        AuthVo authVo = new AuthVo();
        try {
            runCode(new JSONObject()
                    .put("username", authDto.getUsername())
                    .put("password", authDto.getPassword())
                    .put("SchoolIndex", new JSONObject()
                            .put("iconList", new JSONArray())),authVo);
            if ("/login!welcome.action".equals(authVo.getMsg())) {
                ClockInUser clockInUser = clockInUserMapper.selectClockInUserUserName(authDto.getUsername());
                authVo.setId(clockInUser.getId());
                authVo.setUsername(clockInUser.getUsername());
                authVo.setName(clockInUser.getName());
                authVo.setClassName(clockInClassMapper.getClasName(clockInUser.getClassId()));
                authVo.setToken("sdf");
            }

            return authVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authVo;
    }

    public JSONObject isLogin(JSONObject event) throws IOException {
        if (!event.has("username") || !event.has("password")) {
            return new JSONObject().put("msg", "没有账号或者密码");
        }

        String username = event.getString("username");
        String password = event.getString("password");

        // 加密密码
        String encodedPassword = encodeBase64(password);

        OkHttpClient client = new OkHttpClient();

        // 获取 Cookie
        Request getRequest = new Request.Builder()
                .url("https://jwxt.gdupt.edu.cn")
                .build();

        Response getResponse = client.newCall(getRequest).execute();
        String cookie = getResponse.header("Set-Cookie");

        // 发送 POST 请求
        okhttp3.RequestBody postBody = new FormBody.Builder()
                .add("account", username)
                .add("pwd", encodedPassword)
                .build();

        Request postRequest = new Request.Builder()
                .url("https://jwxt.gdupt.edu.cn/login!doLogin.action")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18362")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Accept", "application/json, text/javascript, */*; q=0.01")
                .header("Cookie", cookie)
                .post(postBody)
                .build();

        Response postResponse = client.newCall(postRequest).execute();
        String responseBody = postResponse.body().string();

        JSONObject jsonResponse = new JSONObject(responseBody);
        if ("/login!welcome.action".equals(jsonResponse.getString("msg"))) {
            return new JSONObject().put("msg", "welcome");
        } else {
            return jsonResponse;
        }
    }


    public void runCode(JSONObject args,AuthVo authVo) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        // 加密密码
        String encodedPassword = encodeBase64(args.getString("password"));

        // 获取 Cookie
        Request getRequest = new Request.Builder()
                .url("https://jwxt.gdupt.edu.cn/")
                .build();

        Response getResponse = client.newCall(getRequest).execute();
        String cookie = getResponse.header("Set-Cookie");

        // 登录请求
        okhttp3.RequestBody loginBody = new FormBody.Builder()
                .add("account", args.getString("username"))
                .add("pwd", encodedPassword)
                .add("verifycode", "")
                .build();

        Request loginRequest = new Request.Builder()
                .url("https://jwxt.gdupt.edu.cn/login!doLogin.action")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Accept", "application/json, text/javascript, */*; q=0.01")
                .header("Cookie", cookie)
                .post(loginBody)
                .build();

        Response loginResponse = client.newCall(loginRequest).execute();
        String loginResponseBody = loginResponse.body().string();
        JSONObject loginJsonResponse = new JSONObject(loginResponseBody);

        if ("/login!welcome.action".equals(loginJsonResponse.getString("msg"))) {
            LocalDate courseStartDate = LocalDate.of(2024, 9, 2);
            LocalDate today = LocalDate.now();
            long daysBetween = ChronoUnit.DAYS.between(courseStartDate, today);
            int weekOfCourse = (int) (daysBetween / 7) + 1;
                Request dataRequest = new Request.Builder()
                        .url("https://jwxt.gdupt.edu.cn/xsgrkbcx!getKbRq.action?xnxqdm=202401&zc=" + weekOfCourse)
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .header("Accept", "application/json, text/javascript, */*; q=0.01")
                        .header("Cookie", cookie)
                        .post(new FormBody.Builder().build())
                        .build();
                Response dataResponse = client.newCall(dataRequest).execute();
                String dataResponseBody = dataResponse.body().string();
                authVo.setResData(iclassroomSessionService.analyticSession(dataResponseBody,args.getString("username")));
        }

        authVo.setMsg(loginJsonResponse.getString("msg"));
    }

    private String encodeBase64(String input) {
        byte[] utf8Bytes = input.getBytes();
        return Base64.getEncoder().encodeToString(utf8Bytes);
    }



}

