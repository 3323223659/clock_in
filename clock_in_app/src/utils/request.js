import { baseUrl } from "@/config";

// 封装请求方法
const request = (url, method, data) => {
  return new Promise((resolve, reject) => {
    console.log(baseUrl + url);
    uni.request({
      url: baseUrl + url,
      method: method,
      data: data,
      header: {
        "Content-Type": "application/json",
        Authorization: uni.getStorageSync("token"), // 如果需要token验证
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(res);
        }
      },
      fail: (err) => {
        reject(err);
      },
    });
  });
};

// 导出 GET 和 POST 方法
export const get = (url, params) => request(url, "GET", params);
export const post = (url, data) => request(url, "POST", data);
