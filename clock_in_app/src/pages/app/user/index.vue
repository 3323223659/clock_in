<template>
  <view class="app-page">
    <view
      class="calendar-section"
      @touchstart="touchStart"
      @touchend="touchEnd"
    >
      <view class="calendar">
        <view class="calendar-header">
          <text @tap="changeMonth(-1)" class="arrow">&lt;</text>
          <text class="current-month">{{ currentMonthYear }}</text>
          <text @tap="changeMonth(1)" class="arrow">&gt;</text>
        </view>
        <view class="calendar-weekdays">
          <text
            v-for="day in ['一', '二', '三', '四', '五', '六', '日']"
            :key="day"
            >{{ day }}</text
          >
        </view>
        <view class="calendar-days">
          <view
            v-for="date in calendarDates"
            :key="date.toISOString()"
            :class="[
              'calendar-day',
              {
                current: isCurrentDate(date),
                'has-class': hasClass(date),
                'other-month': date.getMonth() !== currentDate.getMonth(),
                selected: isSelectedDate(date),
              },
            ]"
            @tap="selectDate(date)"
          >
            {{ isCurrentDate(date) ? "今" : date.getDate() }}
          </view>
        </view>
      </view>
    </view>
    <view class="today-courses">
      <text class="section-title">{{ selectedDateFormatted }} 课程</text>
      <scroll-view scroll-y class="course-list">
        <template v-if="todayCourses.length > 0">
          <view
            v-for="course in todayCourses"
            :key="course.id"
            class="course-item"
          >
            <view class="course-icon">
              <image
                :src="getStatusIcon(course.attendanceStatus)"
                :alt="course.attendanceStatus"
                mode="aspectFit"
              ></image>
            </view>
            <view class="course-info">
              <text class="course-name">{{ course.courseName }}</text>
              <text class="course-details"
                >{{ course.startTime }} | {{ course.endTime }}</text
              >
            </view>
            <view
              :class="[
                'attendance-status',
                getStatusClass(course.attendanceStatus),
              ]"
            >
              <text>{{ getStatusText(course.attendanceStatus) }}</text>
            </view>
          </view>
        </template>
        <template v-else>
          <view class="no-courses">
            <image
              src="/static/images/不上课.png"
              mode="aspectFit"
              class="no-courses-icon"
            ></image>
            <text class="no-courses-text">今天没有课程</text>
          </view>
        </template>
      </scroll-view>
    </view>
    <view class="current-course">
      <text class="course-label">当前课程</text>
      <text class="course-name">{{ currentCourse.name }}</text>
      <text v-if="currentCourse.time" class="course-time">{{
        currentCourse.time
      }}</text>
      <text v-if="currentCourse.location" class="course-location">{{
        currentCourse.location
      }}</text>
      <text v-if="currentCourse.status" class="course-status">{{
        currentCourse.status
      }}</text>
    </view>
  </view>
</template>

<script>
import "../../../static/style/app-user-index.css";
export default {
  data() {
    return {
      currentDate: new Date(),
      selectedDate: new Date(),
      todayCourses: [],
      resData: [], // 初始化为空数组
      classes: [
        { date: new Date(2023, 4, 15), name: "高等数学" },
        { date: new Date(2023, 10, 3), name: "线性代数" },
        // 添加更多课程...
      ],
      touchStartX: 0,
      currentCourse: {
        name: "",
        time: "",
        location: "",
        status: "",
      },
    };
  },
  computed: {
    currentMonthYear() {
      return `${this.currentDate.getFullYear()}年${
        this.currentDate.getMonth() + 1
      }月`;
    },
    calendarDates() {
      const year = this.currentDate.getFullYear();
      const month = this.currentDate.getMonth();
      const firstDay = new Date(year, month, 1);
      const lastDay = new Date(year, month + 1, 0);
      const dates = [];

      for (let i = 1 - firstDay.getDay(); i <= 42 - firstDay.getDay(); i++) {
        dates.push(new Date(year, month, i));
      }

      return dates;
    },
    selectedDateFormatted() {
      return `${this.selectedDate.getFullYear()}年${
        this.selectedDate.getMonth() + 1
      }月${this.selectedDate.getDate()}日`;
    },
  },
  methods: {
    getStatusIcon(status) {
      const icons = {
        1: "/static/images/卡通鸡.png",
        2: "/static/images/卡通牛.png",
        3: "/static/images/鹿卡通动物.png",
        0: "/static/images/卡通企鹅.png",
      };
      return icons[status] || "/static/imags/default-icon.png";
    },
    getStatusClass(status) {
      const classes = {
        1: "checked-in",
        2: "late",
        3: "leave",
        0: "not-checked-in",
      };
      return classes[status] || "unknown";
    },
    getStatusText(status) {
      const texts = {
        1: "已打卡",
        2: "迟到",
        3: "请假",
        0: "未打卡",
      };
      return texts[status] || "未知状态";
    },
    changeMonth(delta) {
      this.currentDate = new Date(
        this.currentDate.getFullYear(),
        this.currentDate.getMonth() + delta,
        1
      );
    },
    isCurrentDate(date) {
      const today = new Date();
      return (
        date.getDate() === today.getDate() &&
        date.getMonth() === today.getMonth() &&
        date.getFullYear() === today.getFullYear()
      );
    },
    isSelectedDate(date) {
      return (
        date.getDate() === this.selectedDate.getDate() &&
        date.getMonth() === this.selectedDate.getMonth() &&
        date.getFullYear() === this.selectedDate.getFullYear()
      );
    },
    hasClass(date) {
      return this.classes.some(
        (cls) =>
          cls.date.getDate() === date.getDate() &&
          cls.date.getMonth() === date.getMonth() &&
          cls.date.getFullYear() === date.getFullYear()
      );
    },
    selectDate(date) {
      this.selectedDate = date;
      // 过滤并更新 todayCourses
      this.todayCourses = this.resData.filter((course) => {
        const courseDate = new Date(course.sessionDate);
        return (
          courseDate.getFullYear() === date.getFullYear() &&
          courseDate.getMonth() === date.getMonth() &&
          courseDate.getDate() === date.getDate()
        );
      });
      console.log(`选中日期: ${this.selectedDateFormatted}`);
    },
    touchStart(e) {
      this.touchStartX = e.touches[0].clientX;
    },
    touchEnd(e) {
      const touchEndX = e.changedTouches[0].clientX;
      const diff = touchEndX - this.touchStartX;

      // 如果滑动距离大于50像素，则切换月份
      if (Math.abs(diff) > 50) {
        if (diff > 0) {
          // 向右滑动，上一个月
          this.changeMonth(-1);
        } else {
          // 向左滑动，下一个月
          this.changeMonth(1);
        }
      }
    },
    getCurrentCourse() {
      const courseList = uni.getStorageSync("resData");
      console.log(courseList);
      const now = new Date();
      const currentDate = now.toISOString().split("T")[0]; // 获取当前日期，格式为 "YYYY-MM-DD"
      const currentTime = now.toTimeString().split(" ")[0]; // 获取当前时间，格式为 "HH:MM:SS"

      let currentCourse = null;

      courseList.forEach((course) => {
        if (
          course.sessionDate === currentDate &&
          course.startTime <= currentTime &&
          course.endTime > currentTime
        ) {
          currentCourse = course;
        }
      });

      if (currentCourse) {
        this.currentCourse.name = currentCourse.courseName;
        this.currentCourse.time = `${currentCourse.startTime.slice(
          0,
          5
        )} - ${currentCourse.endTime.slice(0, 5)}`;
        this.currentCourse.location = currentCourse.courseLocation;
        this.currentCourse.status = this.getStatusText(
          currentCourse.attendanceStatus
        );
      } else {
        this.currentCourse.name = "当前无课程";
        this.currentCourse.time = "";
        this.currentCourse.location = "";
        this.currentCourse.status = "";
      }
    },
  },
  created() {
    // 从本地存储中获取 resData
    const resData = uni.getStorageSync("resData");
    if (resData) {
      this.resData = resData;
    }
    this.selectDate(new Date());
  },
};
</script>

<style scoped></style>
