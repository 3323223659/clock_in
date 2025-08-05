<template>
  <view class="app-page">
    <el-link
      icon="el-icon-arrow-left"
      :underline="false"
      @click="goBack"
      class="go-back"
      >返回</el-link
    >
    <view v-if="status === 1" class="result">
      <i class="el-icon-success" style="font-size: 120px; color: green"></i>
      <h2 style="margin-top: 20px; font-weight: 500; letter-spacing: 6px">
        签到成功
      </h2>
    </view>
    <view v-else-if="status === 2" class="result">
      <i class="el-icon-warning" style="font-size: 120px; color: #ffd700"></i>
      <h2 style="margin-top: 20px; font-weight: 500; letter-spacing: 6px">
        你已迟到
      </h2>
    </view>
    <view v-else-if="status === 3" class="result">
      <i class="el-icon-info" style="font-size: 120px; color: green"></i>
      <h2 style="margin-top: 20px; font-weight: 500; letter-spacing: 6px">
        你已请假
      </h2>
    </view>
    <view v-else class="result">
      <i class="el-icon-error" style="font-size: 120px; color: red"></i>
      <h2 style="margin-top: 20px; font-weight: 500">签到失败,请重试</h2>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      status: "",
    };
  },
  mounted() {
    this.status = this.$route.query.status | "0";
  },
  methods: {
    goBack() {
      this.$router.push("index");
    },
  },
};
</script>

<style scoped>
.app-page {
  padding: 20px;
  background-color: #f5f5f5;
  height: 100vh;
  overflow: hidden;
}

.go-back {
  margin-top: 1%;
  font-size: 18px;
}

.result {
  margin: 10% auto;
  text-align: center; /* 居中对齐 */
}
</style>
