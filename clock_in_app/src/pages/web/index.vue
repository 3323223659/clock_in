<template>
  <div class="attendance-page">
    <div v-if="this.teacherName == null" class="qr-code-section">
      <h2>扫码打卡</h2>
      <div class="qr-code">
        <el-empty description="暂无上课信息"></el-empty>
      </div>
      <div class="class-info">
        <p><strong></strong></p>
        <p><strong></strong></p>
        <p><strong></strong></p>
      </div>
    </div>
    <div v-else class="qr-code-section">
      <h2>扫码打卡</h2>
      <div class="qr-code">
        <img v-if="qrCode" :src="qrCode" alt="扫码打卡二维码" />
      </div>
      <div class="class-info">
        <p><strong>老师：</strong>{{ teacherName }}</p>
        <p><strong>课程：</strong>{{ courseName }}</p>
        <p><strong>班级：</strong>{{ className }}</p>
      </div>
    </div>
    <div v-loading="selectLoading" class="attendance-list">
      <div>
        <el-button
          v-if="students.length > 0"
          type="primary"
          icon="el-icon-refresh"
          @click="refreshStudentList"
          class="refresh-button"
        >
          刷新
        </el-button>
        <h2 style="display: inline-block; margin-left: 35%">打卡状态</h2>
        <h3 style="display: inline-block; margin-left: 10%">教室：</h3>
        <ClassSelect
          style="display: inline-block"
          @class-selected="handleClassSelected"
        ></ClassSelect>
        <!-- 修改刷新按钮，只在有学生数据时显示 -->

        <div class="tab-bar">
          <div
            v-for="tab in tabSummary"
            :key="tab.label"
            :class="['tab-item', { active: activeTab === tab.label }]"
            @click="activeTab = tab.label"
          >
            <span class="tab-label">{{ tab.label }}</span>
            <span class="tab-count">({{ tab.count }})</span>
          </div>
        </div>
        <ul class="student-list">
          <li
            v-for="student in filteredStudents"
            :key="student.id"
            @contextmenu.prevent="showContextMenu($event, student)"
          >
            <span class="student-name">{{ student.name }}</span>
            <span :class="['status', student.status]">{{
              student.status
            }}</span>
          </li>
        </ul>
      </div>
      <div v-if="showMenu" :style="menuStyle" class="context-menu">
        <div
          v-for="status in ['已打卡', '未打卡', '迟到', '请假']"
          :key="status"
          @click="changeStatus(status)"
        >
          {{ status }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getQrCode } from "../../api/common";
import { getStudentList, changeStatus } from "../../api/course";
import ClassSelect from "./WebSelect.vue";
import jsQR from "jsqr";
import "../../static/style/web-index.css"; // 导入新的CSS文件

export default {
  components: {
    ClassSelect,
  },
  data() {
    return {
      loading: false,
      selectLoading: false,
      sessionIds: null,
      classroom: null,
      qrCode: "",
      ip_address: "11.11.11.11",
      location: "广油",
      qrCodeData: {},
      flushTime: "",
      teacherName: null,
      courseName: null,
      className: null,
      students: [],
      activeTab: "未打卡",
      showMenu: false,
      menuStyle: {
        top: "0px",
        left: "0px",
      },
      selectedStudent: null,
      studentListInterval: null,
    };
  },
  computed: {
    tabSummary() {
      const summary = {
        已打卡: 0,
        未打卡: 0,
        迟到: 0,
        请假: 0,
      };
      this.students.forEach((student) => {
        summary[student.status]++;
      });
      return Object.keys(summary).map((key) => ({
        label: key,
        count: summary[key],
      }));
    },
    filteredStudents() {
      return this.students.filter(
        (student) => student.status === this.activeTab
      );
    },
  },
  methods: {
    showContextMenu(event, student) {
      this.showMenu = true;
      this.menuStyle.top = `${event.clientY}px`;
      this.menuStyle.left = `${event.clientX}px`;
      this.selectedStudent = student;
    },
    async changeStatus(newStatus) {
      if (this.selectedStudent) {
        const user = this.selectedStudent;
        if (user.status === newStatus) {
          return;
        }
        let studentIsPresent = 0;
        if (newStatus === "已打卡") {
          studentIsPresent = 1;
        } else if (newStatus === "迟到") {
          studentIsPresent = 2;
        } else if (newStatus === "请假") {
          studentIsPresent = 3;
        } else if (newStatus === "未打卡") {
          studentIsPresent = 0;
        }
        await changeStatus(
          user.name,
          studentIsPresent,
          this.classroom,
          JSON.stringify(this.sessionIds)
        );
        this.selectedStudent.status = newStatus;
      }
      this.showMenu = false;
    },
    async getStudents() {
      const data = await getStudentList(JSON.stringify(this.sessionIds));
      if (data) {
        this.students = data.map((student, index) => {
          return {
            id: index + 1,
            name: student.name,
            status: student.status,
          };
        });

        // 检查是否所有学生都已打卡
        const allCheckedIn = this.students.every(
          (student) => student.status !== "未打卡"
        );
        if (allCheckedIn) {
          // 如果所有学生都已打卡，清除轮询定时器
          clearInterval(this.studentListInterval);
          this.$message.success("所有学生已完成打卡");
        }
      }
    },
    async refreshStudentList() {
      await this.getStudents();
      this.$message.success("学生状态已更新");

      // 在手动刷新后，如果还有未打卡的学生，重新启动轮询
      const hasUncheckedStudents = this.students.some(
        (student) => student.status === "未打卡"
      );
      if (hasUncheckedStudents && !this.studentListInterval) {
        this.startStudentListPolling();
      }
    },
    startStudentListPolling() {
      // 清除可能存在的旧定时器
      clearInterval(this.studentListInterval);
      // 启动新的轮询
      this.studentListInterval = setInterval(() => {
        this.getStudents();
      }, 30000);
    },
    async handleClassSelected(classroom) {
      // 清除之前的定时器
      clearInterval(this.qrCodeAndStudentInterval);
      clearInterval(this.studentListInterval);

      this.classroom = classroom;
      this.selectLoading = true; // 开始选择时显示loading
      await this.generateQRCode();
      if (this.sessionIds != null) {
        await this.getStudents();
        // 每隔8秒调用 generateQRCode 和 getStudents
        this.qrCodeAndStudentInterval = setInterval(() => {
          this.generateQRCode();
          // this.getStudents();
        }, 8000);

        // 启动学生状态轮询
        this.startStudentListPolling();
      }
      this.selectLoading = false; // 选择完成后隐藏loading
    },
    async generateQRCode() {
      try {
        if (this.classroom == null) {
          return;
        }
        // 生成当前日期时间
        // const now = new Date();
        // const futureTime = new Date(now.getTime() + 15 * 60000);
        // const sessionDate = futureTime.toISOString().split('T')[0];
        // const hours = String(futureTime.getHours()).padStart(2, '0');
        // const minutes = String(futureTime.getMinutes()).padStart(2, '0');
        // const seconds = String(futureTime.getSeconds()).padStart(2, '0');
        // const time = `${hours}:${minutes}:${seconds}`;
        const sessionDate = "2024-10-09";
        const time = "10:30:00";
        const data = await getQrCode(this.classroom, sessionDate, time);
        if (data.qrCode == null) {
          this.$message.warning("这里没有上课信息");
          clearInterval(this.qrCodeAndStudentInterval);
          this.teacherName = null;
          this.sessionIds = null;
          this.students = [];
          return;
        }
        const imageUrl = "data:image/png;base64," + data.qrCode;
        this.qrCode = imageUrl;
        this.className = data.className;
        this.teacherName = data.teacherName;
        this.courseName = data.sessionName;
        // 解析二维码
        const qrCodeContent = await this.decodeQRCode(imageUrl);

        // 尝试将二维码内容解析为 JSON 对象
        try {
          this.qrCodeContent = JSON.parse(qrCodeContent);
          const date = new Date(this.qrCodeContent.flushTime);
          this.sessionIds = this.qrCodeContent.sessionIds;
          const hours = date.getHours().toString().padStart(2, "0");
          const minutes = date.getMinutes().toString().padStart(2, "0");
          const seconds = date.getSeconds().toString().padStart(2, "0");
          this.flushTime = `${hours}:${minutes}:${seconds}`;
          console.log(this.flushTime);
        } catch (e) {
          console.error("解析JSON失败:", e);
        }
      } catch (e) {
        console.error("生成二维码失败:", e);
      }
    },
    decodeQRCode(imageUrl) {
      return new Promise((resolve, reject) => {
        const image = new Image();
        image.src = imageUrl;
        image.onload = () => {
          const canvas = document.createElement("canvas");
          const context = canvas.getContext("2d");
          canvas.width = image.width;
          canvas.height = image.height;
          context.drawImage(image, 0, 0, image.width, image.height);

          const imageData = context.getImageData(
            0,
            0,
            image.width,
            image.height
          );
          const code = jsQR(imageData.data, imageData.width, imageData.height);

          if (code) {
            resolve(code.data);
          } else {
            reject("二维码解码失败");
          }
        };
        image.onerror = (error) => {
          reject(error);
        };
      });
    },
  },
  mounted() {
    document.addEventListener("click", () => {
      this.showMenu = false;
    });
  },
  beforeUnmount() {
    document.removeEventListener("click", () => {
      this.showMenu = false;
    });
    // 清除所有定时器
    clearInterval(this.qrCodeAndStudentInterval);
    clearInterval(this.studentListInterval);
  },
};
</script>
