<template>
  <!-- 检查这里的点击事件是否正确绑定 -->
  <view class="custom-tab-bar">
    <view
      v-for="(item, index) in tabList"
      :key="index"
      @click="switchTab(item.pagePath)"
      class="tab-item"
    >
      <!-- 确保图标和文字正确显示 -->
      <image
        :src="
          currentPath === item.pagePath ? item.selectedIconPath : item.iconPath
        "
        class="tab-icon"
      ></image>
      <text :class="{ active: currentPath === item.pagePath }" class="tab-text">
        {{ item.text }}
      </text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentPath: "/pages/index/index",
      tabList: [
        {
          pagePath: "/app/index",
          text: "",
          iconPath: "../static/images/首页 (1).png",
          selectedIconPath: "../static/images/首页.png",
        },
        {
          pagePath: "/app",
          text: "",
          iconPath: "../static/images/用户 (1).png",
          selectedIconPath: "../static/images/用户.png",
        },
      ],
    };
  },
  methods: {
    switchTab(path) {
      // 检查这个方法是否正确实现
      if (this.currentPath !== path) {
        // 修改这里
        this.$router.push(path);
        this.currentPath = path;
      }
    },
  },
};
</script>

<style scoped>
.custom-tab-bar {
  display: flex;
  justify-content: space-around;
  align-items: center;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #ffffff;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.1);
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.tab-icon {
  width: 40rpx; /* 调整图标大小 */
  height: 40rpx; /* 调整图标大小 */
  margin-bottom: 4rpx;
}

.tab-text {
  font-size: 24rpx;
  color: #888;
}

.tab-text.active {
  color: #007aff;
}
</style>
