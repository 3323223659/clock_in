import { post } from "@/utils/request";

// 登录API
export const login = (data) => {
  return post("/app/clock_in/v1/login", data);
};

// 获取用户信息API
export const getUserInfo = () => {
  return get("/user/info");
};

// 可以继续添加其他用户相关的API
