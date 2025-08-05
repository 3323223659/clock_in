<template>
  <view v-if="isVisible" class="custom-toast" :class="currentType">
    <image :src="iconSrc" class="toast-icon"></image>
    <text class="toast-text">{{ currentMessage }}</text>
  </view>
</template>

<script>
export default {
  name: "CustomToast",
  data() {
    return {
      isVisible: false,
      currentMessage: "",
      currentType: "info",
      currentDuration: 2000,
    };
  },
  computed: {
    iconSrc() {
      const icons = {
        success: "/static/success.png",
        error: "/static/error.png",
        info: "/static/info.png",
        warning: "/static/warning.png",
      };
      return icons[this.currentType];
    },
  },
  methods: {
    show(message, type = "info", duration = 2000) {
      this.currentMessage = message;
      this.currentType = type;
      this.currentDuration = duration;
      this.isVisible = true;

      setTimeout(() => {
        this.isVisible = false;
      }, this.currentDuration);
    },
  },
};
</script>

<style scoped>
.custom-toast {
  position: fixed;
  top: 10%;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(255, 255, 255, 0.95);
  border-radius: 30rpx;
  padding: 20rpx 40rpx;
  display: flex;
  align-items: center;
  z-index: 9999;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translate(-50%, -20rpx);
  }
  to {
    opacity: 1;
    transform: translate(-50%, 0);
  }
}

.toast-icon {
  width: 48rpx;
  height: 48rpx;
  margin-right: 20rpx;
}

.toast-text {
  font-size: 28rpx;
  font-weight: 500;
}

.success {
  border-left: 8rpx solid #4caf50;
}

.error {
  border-left: 8rpx solid #f44336;
}

.info {
  border-left: 8rpx solid #2196f3;
}

.warning {
  border-left: 8rpx solid #ff9800;
}

.success .toast-text {
  color: #4caf50;
}

.error .toast-text {
  color: #f44336;
}

.info .toast-text {
  color: #2196f3;
}

.warning .toast-text {
  color: #ff9800;
}
</style>
