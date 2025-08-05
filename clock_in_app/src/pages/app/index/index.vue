<template>
  <view class="container">
    <view class="header">
      <view class="user-info" @click="toggleDropdown">
        <image class="avatar" :src="currentAvatar"></image>
        <view class="user-details">
          <text class="username">{{ studentName || "未登录" }}</text>
          <text class="student-info">{{ studentClass }}</text>
          <text class="student-info">{{ studentId }}</text>
        </view>
        <view
          class="dropdown-arrow"
          :class="{ 'arrow-up': isDropdownVisible }"
        ></view>
      </view>
      <view class="date-info">
        <text class="date">{{ formattedDate }}</text>
        <text class="day">{{ currentDay }}</text>
      </view>
    </view>

    <view v-if="isDropdownVisible" class="dropdown-menu">
      <view class="dropdown-item" @click.stop="handleLogout">退出登录</view>
    </view>

    <view class="main-content">
      <view class="clock-in-section">
        <text class="clock-in-question">今天你打卡了吗？</text>
        <view class="image-container">
          <image
            class="cartoon-image"
            :class="{ shake: isShaking }"
            :src="currentImage"
            @click="toggleImage"
          ></image>
          <view class="confetti-container" v-if="showConfetti">
            <view
              v-for="n in 50"
              :key="n"
              class="confetti-item"
              :style="getRandomStyle()"
            ></view>
          </view>
        </view>
        <view class="current-course">
          <text class="course-label">当前课程</text>
          <text class="course-name">{{ currentCourse.name }}</text>
          <text class="course-time">{{ currentCourse.time }}</text>
        </view>
        <view class="button-container">
          <button class="clock-in-btn" @click="scanQRCode">
            <text class="btn-text">扫码打卡</text>
          </button>
          <view class="floating-animals-container">
            <image
              v-for="(animal, index) in floatingAnimals"
              :key="index"
              :src="animal.src"
              class="floating-animal"
              :style="animal.style"
            ></image>
          </view>
        </view>
        <!-- <button @click="checkIn">签到</button> -->
        <Scan v-model="showScan" @success="getScan" />
        <Notification
          v-if="show"
          :message="errorMessage"
          type="success"
          :duration="1000"
          icon="✔"
          @close="onClose"
        />
      </view>
    </view>
  </view>
</template>

<script>
import { checkIn } from "@/api/attendance";
import Scan from "./Scan.vue";
import Notification from "@/components/Notification.vue";
import "../../../static/style/app-index.css";
import AMapLoader from "@amap/amap-jsapi-loader";

export default {
  components: {
    Scan,
    Notification,
  },
  name: "Index",
  data() {
    return {
      showScan: false,
      show: false,
      flushTime: "",
      sessionIds: null,
      status: "",
      currentDate: new Date(),
      studentName: "张三",
      studentClass: "人工智能22-3",
      studentId: "202200000000",
      currentCourse: {
        name: "目前没课、好好休息",
        time: "",
      },
      isDropdownVisible: false,
      isShaking: false,
      showConfetti: false,
      currentImage: "/static/images/猫猫小动物.png",
      imageIndex: 0,
      images: [
        "/static/images/猫猫小动物.png",
        "/static/images/卡通牛.png",
        "/static/images/卡通乌龟.png",
        "/static/images/鹿卡通动物.png",
      ],
      currentAvatar: "/static/images/卡通牛.png",
      avatars: [
        "/static/images/卡通牛.png",
        "/static/images/猫猫小动物.png",
        "/static/images/卡通乌龟.png",
        "/static/images/鹿卡通动物.png",
      ],
      floatingAnimals: [],
      currentPosition: {
        lat: null,
        lng: null,
      },
      AMap: null,
      errorMessage: "退出成功",
    };
  },
  computed: {
    formattedDate() {
      const year = this.currentDate.getFullYear();
      const month = String(this.currentDate.getMonth() + 1).padStart(2, "0");
      const day = String(this.currentDate.getDate()).padStart(2, "0");
      return `${year}-${month}-${day}`;
    },
    currentDay() {
      const days = [
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六",
        "星期日",
      ];
      return days[this.currentDate.getDay()];
    },
  },
  mounted() {
    this.checkLoginStatus();
    this.getCurrentCourse();
    this.randomizeAvatar();
  },
  methods: {
    getCurrentCourse() {
      const courseList = uni.getStorageSync("resData");
      console.log(courseList);
      const date = this.formattedDate;
      const now = new Date();
      courseList.forEach((element) => {
        if (element.sessionDate === date) {
          const startTime = new Date(
            `${element.sessionDate}T${element.startTime}`
          );
          const endTime = new Date(`${element.sessionDate}T${element.endTime}`);

          if (now >= startTime && now <= endTime) {
            console.log(element);
            this.currentCourse.name = element.courseName;
            this.currentCourse.time = `${element.startTime} - ${element.endTime}`;
          }
        }
      });
    },
    scanQRCode() {
      console.log("扫码打卡");
      this.showScan = !this.showScan;
      // this.$router.push("/qrCodeTest");
    },
    async getScan(qrCodeMessage) {
      // todo 扫码的结果
      // alert(qrCodeMessage);
      const data = JSON.parse(qrCodeMessage);
      this.flushTime = await this.formatTime(data.flushTime);
      this.sessionIds = JSON.stringify(data.sessionIds);
      this.checkIn();
    },
    formatTime(dateString) {
      const date = new Date(dateString);
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      const seconds = String(date.getSeconds()).padStart(2, "0");
      return `${hours}:${minutes}:${seconds}`;
    },
    checkLoginStatus() {
      const isLoggedIn = uni.getStorageSync("isLoggedIn") === "true";
      if (!isLoggedIn) {
        this.studentName = "未登录";
        this.studentClass = "";
        this.studentId = "";
      } else {
        this.studentName =
          uni.getStorageSync("studentName") || this.studentName;
        this.studentClass =
          uni.getStorageSync("studentClass") || this.studentClass;
        this.studentId = uni.getStorageSync("studentId") || this.studentId;
      }
    },
    toggleDropdown() {
      this.isDropdownVisible = !this.isDropdownVisible;
    },
    handleLogout() {
      console.log("Logout button clicked");

      uni.removeStorageSync("token");
      uni.removeStorageSync("isLoggedIn");
      uni.removeStorageSync("studentName");
      uni.removeStorageSync("studentClass");
      uni.removeStorageSync("studentId");

      this.studentName = "未登录";
      this.studentClass = "";
      this.studentId = "";

      this.isDropdownVisible = false;
      this.show = true;
      setTimeout(() => {
        this.$router.push("/app/login");
      }, "500");
    },
    async checkIn() {
      // const location = "110.920595, 110.924010";
      // alert(uni.getStorageSync("id"));
      await this.getLocation();
      const location =
        this.currentPosition.lat + "," + this.currentPosition.lng;
      console.log("先到这里");

      alert(uni.getStorageSync("id"));
      alert(this.sessionIds);
      alert(location);
      alert(this.flushTime);
      const resData = await checkIn(
        uni.getStorageSync("id"),
        this.sessionIds,
        location,
        this.flushTime
      );
      this.status = resData;
      alert(this.status);
      this.$router.push({
        path: "/app/checkIn",
        query: {
          status: this.status,
        },
      });
    },
    async checkIn() {
      // const location = "110.920595, 110.924010";
      // alert(uni.getStorageSync("id"));
      await this.getLocation();
      const location =
        this.currentPosition.lat + "," + this.currentPosition.lng;
      console.log("先到这里");

      // alert(uni.getStorageSync("id"));
      // alert(this.sessionIds);
      // alert(location);
      // alert(this.flushTime);
      const resData = await checkIn(
        uni.getStorageSync("id"),
        this.sessionIds,
        location,
        this.flushTime
      );
      this.status = resData;
      // alert(this.status);
      this.$router.push({
        path: "/app/checkIn",
        query: {
          status: this.status,
        },
      });
    },
    async getLocation() {
      console.log("先到这里123");

      this.errorMessage = null;

      // 加载高德地图API
      try {
        const AMap = await AMapLoader.load({
          key: "b3ffe779e7e8620a015eaeb55e5f9876",
          version: "2.0",
          plugins: ["AMap.Geolocation", "AMap.CitySearch"],
        });
        this.AMap = AMap;

        // 首先尝试使用浏览器的Geolocation API
        if ("geolocation" in navigator) {
          try {
            const position = await new Promise((resolve, reject) => {
              navigator.geolocation.getCurrentPosition(resolve, reject, {
                enableHighAccuracy: true,
                timeout: 5000,
                maximumAge: 0,
              });
            });
            const { longitude, latitude } = position.coords;
            this.currentPosition = { lng: longitude, lat: latitude };
            // alert(JSON.stringify(this.currentPosition));
          } catch (error) {
            console.error("浏览器定位失败", error);
            // alert("浏览器定位失败");
            await this.tryAMapGeolocation();
          }
        } else {
          await this.tryAMapGeolocation();
        }
      } catch (e) {
        this.errorMessage = "地图加载失败：" + e.message;
      }
    },
    async tryAMapGeolocation() {
      console.log("进入定位");
      // alert("进入定位");
      const geolocation = new this.AMap.Geolocation({
        enableHighAccuracy: true,
        timeout: 10000,
        zoomToAccuracy: true,
        useNative: true,
        GeoLocationFirst: true,
      });

      try {
        const result = await new Promise((resolve, reject) => {
          geolocation.getCurrentPosition((status, result) => {
            if (status === "complete") {
              resolve(result);
            } else {
              reject(result);
            }
          });
        });
        const { lng, lat } = result.position;
        this.currentPosition = { lng, lat };
        console.log(this.currentPosition);
      } catch (error) {
        console.error("高德地图定位失败", error);
        // alert("高德地图定位失败");
        await this.fallbackToIPLocation();
      }
    },
    async fallbackToIPLocation() {
      console.log("既然你");
      // alert("既然你");
      const citySearch = new this.AMap.CitySearch();
      try {
        const result = await new Promise((resolve, reject) => {
          citySearch.getLocalCity((status, result) => {
            if (status === "complete" && result.info === "OK") {
              resolve(result);
            } else {
              reject(result);
            }
          });
        });
        this.errorMessage = "精确定位失败，已使用IP定位：" + result.city;
        const [lng, lat] = result.rectangle.split(";")[0].split(",");
        this.currentPosition = { lng, lat };
      } catch (error) {
        this.errorMessage = "所有定位方法均失败，请检查网络连接或浏览器设置";
      }
    },
    toggleImage() {
      let newIndex;
      do {
        newIndex = Math.floor(Math.random() * this.images.length);
      } while (newIndex === this.imageIndex && this.images.length > 1);

      this.imageIndex = newIndex;
      this.currentImage = this.images[this.imageIndex];
      this.shakeImage();
      this.addFloatingAnimals();
    },
    shakeImage() {
      this.isShaking = true;
      this.showConfetti = true;
      setTimeout(() => {
        this.isShaking = false;
      }, 500);
      setTimeout(() => {
        this.showConfetti = false;
      }, 3000);
    },
    getRandomStyle() {
      const colors = [
        "#ff0000",
        "#00ff00",
        "#0000ff",
        "#ffff00",
        "#ff00ff",
        "#00ffff",
      ];
      return {
        backgroundColor: colors[Math.floor(Math.random() * colors.length)],
        left: `${Math.random() * 100}%`,
        top: `${Math.random() * 100}%`,
        transform: `scale(${Math.random() * 0.5 + 0.5})`,
        animationDuration: `${Math.random() * 2 + 1}s`,
        animationDelay: `${Math.random() * 0.5}s`,
      };
    },
    randomizeAvatar() {
      const randomIndex = Math.floor(Math.random() * this.avatars.length);
      this.currentAvatar = this.avatars[randomIndex];
    },
    addFloatingAnimals() {
      const numberOfAnimals = 5; // 可以调整数量
      for (let i = 0; i < numberOfAnimals; i++) {
        const animal = {
          src: this.currentImage,
          style: this.getRandomFloatingStyle(),
        };
        this.floatingAnimals.push(animal);
      }
      // 动画结束后清除动物,时间缩短到1.5秒
      setTimeout(() => {
        this.floatingAnimals = [];
      }, 1500);
    },
    getRandomFloatingStyle() {
      const startX = Math.random() * 100;
      const startY = Math.random() * 100;
      const endX = Math.random() * 200 - 50; // 范围从 -50% 到 150%
      const endY = Math.random() * 200 - 50; // 范围从 -50% 到 150%
      const duration = Math.random() * 0.5 + 0.5; // 0.5-1秒的随机持续时间

      return {
        position: "absolute", // 改回absolute定位
        left: `${startX}%`,
        top: `${startY}%`,
        width: "30rpx", // 减小图标大小
        height: "30rpx",
        transform: `translate(-50%, -50%) scale(${Math.random() * 0.5 + 0.5})`,
        opacity: 1,
        transition: `all ${duration}s ease-out`,
        "--endX": `${endX}%`,
        "--endY": `${endY}%`,
      };
    },
  },
};
</script>
