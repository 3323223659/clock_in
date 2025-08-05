<template>
  <div class="app-container home">
    <div class="content-wrapper">
      <template v-if="showAnimation">
        <img
          v-show="showLogo"
          src="@/assets/images/打卡签到.png"
          alt="学生打卡管理系统"
          class="logo animated"
        />
        <h1>
          <span
            v-for="(char, index) in welcomeText"
            :key="index"
            class="bounce-text"
            :style="{ animationDelay: `${index * 0.1}s` }"
            >{{ char }}</span
          >
        </h1>
      </template>
      <template v-else>
        <div class="static-content">
          <img
            src="@/assets/images/打卡签到.png"
            alt="学生打卡管理系统"
            class="logo static"
          />
          <h2>欢迎来到学生打卡管理系统</h2>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      version: "3.8.8",
      welcomeText: "欢迎来到学生打卡管理系统",
      showLogo: false,
      showAnimation: true, // 默认显示动画
    };
  },
  mounted() {
    this.playAnimation();
  },
  methods: {
    playAnimation() {
      const animationDuration = this.welcomeText.length * 100 + 500;
      setTimeout(() => {
        this.showLogo = true;
      }, animationDuration);

      // 动画结束后,隐藏动画内容
      setTimeout(() => {
        this.showAnimation = false;
      }, animationDuration + 2000); // 额外给2秒让用户看清
    },
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
</script>

<style scoped lang="scss">
.home {
  height: 100vh;
  overflow: hidden;

  .content-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
  }

  .logo {
    width: 200px;
    height: auto;
    margin-bottom: 30px;

    &.animated {
      opacity: 0;
      animation: fadeIn 1s ease-in-out forwards;
    }

    &.static {
      opacity: 1;
    }
  }

  .static-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;

    h2 {
      margin-top: 20px;
      font-size: 24px;
    }
  }

  h1 {
    text-align: center;
    font-size: 24px;
  }

  .bounce-text {
    display: inline-block;
    opacity: 0;
    animation: bounce 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  }

  @keyframes bounce {
    0% {
      transform: translateY(0);
      opacity: 0;
    }
    50% {
      transform: translateY(-10px);
      opacity: 1;
    }
    100% {
      transform: translateY(0);
      opacity: 1;
    }
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
}
</style>
