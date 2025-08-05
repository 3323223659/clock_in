<template>
  <div class="loading-container" v-if="visible">
    <div class="loading-dots">
      <div class="dot" :style="{ animationDelay: '0s' }"></div>
      <div class="dot" :style="{ animationDelay: '0.2s' }"></div>
      <div class="dot" :style="{ animationDelay: '0.4s' }"></div>
    </div>
    <div class="loading-text">{{ text }}</div>
  </div>
</template>

<script>
export default {
  name: "Loading",
  props: {
    text: {
      type: String,
      default: "Loading...",
    },
    visible: {
      type: Boolean,
      default: true,
    },
  },
};
</script>

<style scoped>
.loading-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(0, 0, 0, 0.7);
  padding: 20px;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 10000;
}

.loading-dots {
  display: flex;
  justify-content: center;
  align-items: center;
}

.dot {
  width: 10px;
  height: 10px;
  background-color: #ffcc00;
  border-radius: 50%;
  margin: 0 5px;
  animation: colorChange 1.5s infinite, bounce 1.5s infinite;
}

@keyframes colorChange {
  0%,
  100% {
    background-color: #ffcc00;
  }
  50% {
    background-color: #ff9900;
  }
}

@keyframes bounce {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.loading-text {
  margin-top: 15px;
  color: #fff;
  font-size: 16px;
}
</style>
