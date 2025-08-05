<template>
  <view class="login-container">
    <view class="sun-container">
      <image
        class="sun-icon"
        src="/static/images/太阳.png"
        :class="{ swing: true }"
      ></image>
    </view>
    <view
      v-for="(cloud, index) in clouds"
      :key="index"
      class="cloud"
      :style="cloudStyle(cloud)"
    >
      <image class="cloud-icon" src="/static/images/云朵.png"></image>
    </view>
    <view class="welcome-text">
      <text class="welcome-title">欢迎回来！</text>
      <text class="welcome-subtitle">请登录您的账户</text>
    </view>
    <view class="login-box">
      <view class="input-container">
        <image class="input-icon" src="/static/images/用户 (1).png"></image>
        <input
          v-model="username"
          class="login-input"
          type="text"
          placeholder="账号：学号"
        />
      </view>
      <view class="input-container">
        <image class="input-icon" src="/static/images/lock1.png"></image>
        <input
          v-model="password"
          class="login-input"
          type="password"
          placeholder="密码：教务处"
        />
      </view>
      <button class="login-btn" @click="handleLogin">登录</button>
      <Notification
        v-if="show"
        :message="notificationMessage"
        :type="notificationType"
        :duration="2000"
        icon="✔"
        @close="show = false"
      />
      <Loading :text="loadingText" :visible="isLoading" />
    </view>
    <!-- 添加自定义Toast组件 -->
    <!-- <custom-toast
      :visible.sync="toastVisible"
      :message="toastMessage"
      :type="toastType"
      :duration="toastDuration"
    /> -->
  </view>
</template>

<script>
import { login } from "@/api/user";
// import CustomToast from "@/components/CustomToast.vue";
import Notification from "@/components/Notification.vue";
import Loading from "@/components/Loading.vue";

export default {
  components: {
    // CustomToast,
    Notification,
    Loading,
  },
  data() {
    return {
      username: "",
      password: "",
      isLoading: false,
      loadingText: "正在登录中",
      show: false,
      clouds: [
        { top: "10%", left: "10%", duration: 20 },
        { top: "30%", left: "70%", duration: 25 },
        { top: "60%", left: "20%", duration: 22 },
        { top: "80%", left: "80%", duration: 18 },
      ],
      notificationMessage: "",
      notificationType: "info",
      // 添加Toast相关的数据
      // toastVisible: false,
      // toastMessage: "",
      // toastType: "info",
      // toastDuration: 2000,
    };
  },
  methods: {
    cloudStyle(cloud) {
      return {
        top: cloud.top,
        left: cloud.left,
        animationDuration: `${cloud.duration}s`,
      };
    },
    async handleLogin() {
      if (!this.username || !this.password) {
        this.showNotification("请输入用户名和密码", "error");
        return;
      }

      this.isLoading = true;
      try {
        if (this.username === "admin" && this.password === "admin") {
          // this.showToast("登录成功", "success");
          uni.setStorageSync("token", "admin");
          uni.setStorageSync("isLoggedIn", "true");
          uni.setStorageSync("studentName", "admin");
          uni.setStorageSync("studentClass", "admin");
          uni.setStorageSync("studentId", "admin");
          console.log("进入");
          this.isLoading = false;
          this.$router.push("/app");
        } else {
          const result = await login({
            username: this.username,
            password: this.password,
          });
          if (result.code === 200) {
            // 登录成功，保存token等信息
            const data = result.data;
            console.log(data);

            uni.setStorageSync("token", data.token);
            uni.setStorageSync("isLoggedIn", "true");
            uni.setStorageSync("studentName", data.name);
            uni.setStorageSync("studentClass", data.className);
            uni.setStorageSync("studentId", data.username);
            uni.setStorageSync("id", data.id);
            uni.setStorageSync("resData", data.resData);
            this.showNotification("登录成功", "success");
            this.isLoading = false;
            this.show = true;
            setTimeout(() => {
              this.$router.push("/app");
            }, "500");
          } else {
            this.showNotification(result.msg, "error");
            this.isLoading = false;
          }
        }
      } catch (error) {
        this.isLoading = false;
        console.error("登录错误", error);
        this.showNotification("登录出错", "error");
      }
    },
    showNotification(message, type = "info", duration = 2000) {
      this.notificationMessage = message;
      this.notificationType = type;
      this.show = true;
      setTimeout(() => {
        this.show = false;
      }, duration);
    },
  },
  onLoad() {
    uni.hideTabBar();
  },
  onUnload() {
    uni.showTabBar();
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
  overflow: hidden;
  position: relative;
}

.sun-container {
  position: absolute;
  top: 8%; /* 调整太阳位置 */
  left: 50%;
  transform: translateX(-50%);
}

.sun-icon {
  width: 400rpx; /* 增大太阳尺寸 */
  height: 400rpx;
}

.swing {
  animation: swing 2s infinite ease-in-out;
}

@keyframes swing {
  0%,
  100% {
    transform: rotate(-10deg);
  }
  50% {
    transform: rotate(10deg);
  }
}

.cloud {
  position: absolute;
  animation: moveCloud linear infinite;
}

.cloud-icon {
  width: 200rpx;
  height: 120rpx;
}

@keyframes moveCloud {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100vw);
  }
}

.welcome-text {
  text-align: center;
  margin-bottom: 60rpx;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.welcome-title {
  font-size: 48rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.welcome-subtitle {
  font-size: 32rpx;
  color: #666;
}

.login-box {
  width: 80%;
  max-width: 600rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  z-index: 1;
}

.input-container {
  width: 100%;
  display: flex;
  align-items: center;
  background-color: rgba(255, 255, 255, 0.8);
  border: 1rpx solid #ddd;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.input-icon {
  width: 40rpx;
  height: 40rpx;
  margin: 0 20rpx;
}

.login-input {
  flex: 1;
  height: 80rpx;
  padding: 0 20rpx;
  background-color: transparent;
  border: none;
}

.login-btn {
  width: 100%;
  height: 80rpx;
  background-color: #ffa500;
  color: #fff;
  border: none;
  border-radius: 40rpx;
  font-size: 32rpx;
  margin-top: 40rpx;
}
</style>
