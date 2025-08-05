<template>
  <div class="app-layout">
    <router-view></router-view>
    <nav class="tab-nav">
      <router-link to="/index" class="tab-item">
        <i class="icon-home"></i>
        首页
      </router-link>
      <router-link to="/app" class="tab-item">
        <i class="icon-calendar"></i>
        课程日历
      </router-link>
    </nav>
  </div>
</template>

<style scoped>
.app-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.tab-nav {
  display: flex;
  justify-content: space-around;
  padding: 10px;
  background-color: #f8f9fa;
  border-top: 1px solid #e0e0e0;
}

.tab-item {
  text-decoration: none;
  color: #333;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.icon-home,
.icon-calendar {
  font-size: 24px;
  margin-bottom: 5px;
}
</style>
