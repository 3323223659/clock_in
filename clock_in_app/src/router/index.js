import Vue from "vue";
import Router from "vue-router";
import Web from "../pages/web/index.vue";
import Index from "../pages/app/index/index.vue";
import Login from "../pages/app/login/index.vue";
import checkIn from "../pages/app/index/checkIn.vue";
import User from "../pages/app/user/index.vue";

Vue.use(Router);

const routes = [
  {
    path: "/",
    redirect: "/app/index",
  },
  {
    path: "/app/index",
    name: "index",
    component: Index,
    meta: { tab: true, requiresAuth: true },
  },
  {
    path: "/web",
    name: "web",
    component: Web,
    meta: { tab: false },
  },
  {
    path: "/app",
    name: "app",
    component: User,
    meta: { tab: true, requiresAuth: true },
  },
  {
    path: "/app/checkIn",
    name: "checkIn",
    component: checkIn,
    meta: { tab: true, requiresAuth: true },
  },
  {
    path: "/app/test",
    name: "test",
    component: () => import("@/pages/test.vue"),
    meta: { tab: true, requiresAuth: true },
  },
  {
    path: "/app/login",
    name: "login",
    component: Login,
    meta: { tab: false },
  },
];

const router = new Router({
  routes,
});

// 检测设备类型
function isMobile() {
  const userAgentInfo = navigator.userAgent;
  const mobileAgents = [
    "Android",
    "iPhone",
    "SymbianOS",
    "Windows Phone",
    "iPad",
    "iPod",
  ];
  const mobileFlag = mobileAgents.some((mobileAgent) => {
    return userAgentInfo.indexOf(mobileAgent) > 0;
  });

  return mobileFlag;
}

router.beforeEach((to, from, next) => {
  const isMobileDevice = isMobile();

  // 如果是手机浏览器，禁止跳转到 /web/** 路径
  if (isMobileDevice && to.path.startsWith("/web")) {
    next("/app/index");
    return;
  }

  // 如果是电脑打开的，禁止跳转到 /app/** 路径
  if (!isMobileDevice && to.path.startsWith("/app")) {
    next("/web");
    return;
  }

  // 如果没有匹配的路由，根据设备类型导航到各自设备的页面
  if (!to.matched.length) {
    if (isMobileDevice) {
      next("/app/index");
    } else {
      next("/web");
    }
    return;
  }

  // 检查是否需要认证
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    const isLoggedIn = uni.getStorageSync("isLoggedIn") === "true";
    const token = uni.getStorageSync("token");
    if (!token || new Date().getTime() >= Number(token)) {
      uni.removeStorageSync("token");
      uni.removeStorageSync("isLoggedIn");
      uni.removeStorageSync("studentName");
      uni.removeStorageSync("studentClass");
      uni.removeStorageSync("studentId");
      next("/app/login");
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
