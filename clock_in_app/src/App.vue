<template>
  <view>
    <router-view></router-view>
    <CustomTabBar v-if="showTabBar"></CustomTabBar>
  </view>
</template>

<script>
import CustomTabBar from "./components/CustomTabBar.vue";

export default {
  components: {
    CustomTabBar,
  },
  computed: {
    showTabBar() {
      // 检查当前路由是否包含 'web'
      console.log(this.$route.path);

      console.log(this.$route.path.includes("login"));
      if (this.$route.path.includes("login")) {
        return false;
      } else if (this.$route.path.includes("web")) {
        return false;
      } else {
        return true;
      }
    },
  },
};
</script>

<style>
/* 样式保持不变 */
</style>
