<template>
  <div>
    测hi是
    <el-button @click="getTest">取 消</el-button>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import {
  listUser,
  test,
  getUser,
  delUser,
  addUser,
  updateUser,
} from "@/api/clock_in/user";

export default {
  name: "Test",
  data() {
    return {};
  },

  methods: {
    getTest() {
      return test();
    },
  },

  mounted() {
    // 动态更改图标
    const favicon = document.getElementById("favicon");
    if (favicon) {
      favicon.href = "/public/test.ico"; // 替换为你的自定义图标路径
    }
  },
};
</script>

<style lang="scss" scoped></style>
