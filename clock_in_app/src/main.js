import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'; // 引入 ElementUI
import 'element-ui/lib/theme-chalk/index.css'; // 引入 ElementUI 样式

Vue.config.productionTip = false
Vue.use(ElementUI)

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
