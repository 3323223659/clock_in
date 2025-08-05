<template>
  <div class="notification" :class="type" v-if="visible">
    <div class="content">
      <span class="icon" v-if="icon">{{ icon }}</span>
      <span class="message">{{ message }}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "Notification",
  props: {
    message: {
      type: String,
      required: true,
    },
    type: {
      type: String,
      default: "info",
      validator: (value) =>
        ["info", "success", "warning", "error"].includes(value),
    },
    duration: {
      type: Number,
      default: 3000,
    },
    icon: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      visible: true,
    };
  },
  mounted() {
    if (this.duration > 0) {
      setTimeout(() => {
        this.close();
      }, this.duration);
    }
  },
  methods: {
    close() {
      this.visible = false;
      this.$emit("close");
    },
  },
};
</script>

<style scoped>
.notification {
  position: fixed;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  padding: 5px;
  border: 2px solid #333;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 200px;
  z-index: 10000;
  background-color: #000;
  color: #fff;
  animation: slide-down 0.5s ease-out forwards;
}

@keyframes slide-down {
  from {
    top: -100px;
    opacity: 0;
  }
  to {
    top: 20px;
    opacity: 1;
  }
}

@keyframes fade-out {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}

.notification[style*="--duration"] {
  animation: slide-down 0.5s ease-out forwards, fade-out 0.5s ease-out forwards;
  animation-delay: 0s, calc(var(--duration) - 0.5s);
}

.content {
  display: flex;
  align-items: center;
}

.icon {
  margin-right: 10px;
  font-size: 18px;
}

.message {
  font-size: 14px;
}
</style>
