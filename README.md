# 学生打卡签到管理系统

## 项目简介

本项目是一个基于ruoyi框架实现的完整的学生打卡管理系统，基于前后端分离架构开发。系统包含学生移动端打卡应用、教师网页端管理界面和后端服务，支持二维码扫码打卡、课程管理、考勤统计等功能。适合学校、培训机构等教育场景使用。

## 技术架构

### 前端技术栈
- **移动端**：[UniApp](https://uniapp.dcloud.io/) + Vue2
- **管理端**：Vue2 + Element UI + RuoYi框架
- **网页端**：Vue2 + UniApp H5

### 后端技术栈
- **框架**：Spring Boot + Spring Security
- **数据库**：MySQL + Redis
- **认证**：JWT Token
- **API文档**：Swagger

## 项目结构

```text
clock_in_system/
├── clock_in_app/              # UniApp移动端应用
│   ├── src/
│   │   ├── pages/             # 页面目录
│   │   │   ├── app/           # 学生端页面
│   │   │   └── web/           # 教师网页端页面
│   │   ├── api/               # API接口
│   │   ├── utils/             # 工具类
│   │   └── components/        # 公共组件
│   ├── package.json           # 依赖配置
│   └── pages.json             # 页面路由配置
├── clock_in_java/             # Java后端服务
│   ├── ruoyi-admin/           # 主应用模块
│   ├── ruoyi-common/          # 公共模块
│   ├── ruoyi-framework/       # 框架模块
│   ├── ruoyi-system/          # 系统模块
│   ├── pom.xml                # Maven配置
│   └── ry.sh                  # 启动脚本
├── clock_in_manage_vue/       # Vue管理端
│   ├── src/
│   │   ├── views/             # 页面视图
│   │   ├── api/               # API接口
│   │   ├── components/        # 组件
│   │   └── utils/             # 工具类
│   ├── package.json           # 依赖配置
│   └── vue.config.js          # Vue配置
└── README.md                  # 项目说明文档
```

## 功能特性

### 学生端功能
- 📱 **移动端打卡**：支持扫码打卡、GPS定位验证
- 📅 **课程日历**：查看个人课程安排
- 📊 **考勤记录**：查看个人打卡历史
- 👤 **个人中心**：个人信息管理

### 教师端功能
- 🔍 **实时监控**：实时查看学生打卡状态
- 📋 **考勤管理**：手动修改学生考勤状态
- 📊 **数据统计**：考勤数据分析和导出
- ⚙️ **课程管理**：课程和教室信息管理

### 管理端功能
- 👥 **用户管理**：学生、教师账号管理
- 🏫 **组织架构**：部门、班级管理
- 📚 **课程管理**：课程安排、教室分配
- 📈 **系统监控**：系统运行状态监控

## 快速开始

### 环境要求

- Node.js 14+
- Java 8+
- MySQL 5.7+
- Redis 3.0+
- HBuilderX（推荐）

### 1. 后端部署

```bash
# 进入后端目录
cd clock_in_java

# 配置数据库连接
# 编辑 ruoyi-admin/src/main/resources/application.yml

# 启动Redis服务
redis-server

# 编译打包
mvn clean package

# 启动服务
java -jar ruoyi-admin/target/ruoyi-admin.jar
# 或使用脚本启动
./ry.sh start
```

### 2. 管理端部署

```bash
# 进入管理端目录
cd clock_in_manage_vue

# 安装依赖
npm install

# 启动开发服务
npm run dev

# 构建生产版本
npm run build:prod
```

### 3. 移动端部署

```bash
# 进入移动端目录
cd clock_in_app

# 安装依赖
yarn install

# H5开发调试
yarn serve

# 构建H5版本
yarn build

# 其他平台构建
yarn build:app-plus    # App
yarn build:mp-weixin   # 微信小程序
```

### 4. 使用HBuilderX（推荐）

1. 使用HBuilderX打开 `clock_in_app` 目录
2. 运行到浏览器、微信小程序或App模拟器
3. 配置后端接口地址（src/config/index.js）

## 配置说明

### 后端配置

主要配置文件：`clock_in_java/ruoyi-admin/src/main/resources/application.yml`

```yaml
# 服务端口
server:
  port: 8080

# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ry-vue
    username: root
    password: password

# Redis配置
  redis:
    host: localhost
    port: 6379
```

### 前端配置

移动端配置：`clock_in_app/src/config/index.js`
```javascript
export const baseUrl = "http://localhost:8080"
```

管理端配置：`clock_in_manage_vue/.env.development`
```
VUE_APP_BASE_API = 'http://localhost:8080'
```

## 部署说明

### 生产环境部署

1. **后端部署**
   - 修改生产环境配置
   - 使用 `./ry.sh start` 启动服务
   - 配置Nginx反向代理

2. **前端部署**
   - 构建生产版本：`npm run build:prod`
   - 部署到Web服务器（Nginx/Apache）

3. **移动端发布**
   - H5版本：构建后部署到Web服务器
   - 小程序：提交微信小程序审核
   - App：使用HBuilderX云打包

## 系统截图

### 移动端界面
- 登录页面
- 扫码打卡
- 课程日历
- 个人中心

### 管理端界面
- 实时监控
- 考勤管理
- 数据统计
- 系统设置

## 开发指南

### API接口

主要接口地址：
- 学生打卡：`POST /web/clock_in/v1/sessionAndCheckIn/checkIn`
- 考勤管理：`POST /web/clock_in/v1/sessionAndCheckIn/insertClockInAttendance`

### 数据库设计

主要数据表：
- 用户表（sys_user）
- 课程表（classroom_session）
- 打卡记录表（clock_in_log）
- 考勤表（attendance）

## 常见问题

### Q: 扫码打卡失败？
A: 检查摄像头权限和网络连接，确保二维码清晰可见。

### Q: 定位不准确？
A: 确保GPS权限已开启，在室外环境下使用效果更佳。

### Q: 后端服务启动失败？
A: 检查数据库连接配置和Redis服务状态。

## 免责声明

本项目仅用于学习和交流目的，请勿用于商业用途。使用本项目所产生的任何问题，开发者不承担任何责任。
