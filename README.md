# 理财管理系统 (Wealth Management System)

基于 SpringBoot + Vue3 的个人与家庭财务管理系统，提供收支记账、分类管理、预算管控、储蓄目标、数据可视化及智能理财建议等一站式服务。

## 📋 功能特性

- 用户注册/登录 (JWT 认证)
- 收支记录管理 (增删改查，支持按月份/分类筛选)
- 自定义收支分类 (支出/收入分类独立管理)
- 双重预算管控 (月度总预算 + 分类预算，实时进度监控)
- 储蓄目标设定与进度跟踪
- 多维度统计分析 (月度趋势折线图、支出构成饼图)
- 智能理财建议 (基于消费数据自动生成)
- 响应式界面，纯 CSS 样式，无需外部图片

## 🛠️ 技术栈

### 后端
- Spring Boot 2.7.x
- MyBatis-Plus 3.5.2
- MySQL 8.0
- JWT (JJWT 0.9.1)
- Maven

### 前端
- Vue 3 (Composition API)
- Vue Router 4
- Element Plus 2.3.x
- ECharts 5.4.x
- Axios
- Node.js 16+

## 🚀 快速开始

### 环境要求
- JDK 1.8 或更高 (推荐 JDK 11)
- Maven 3.6+
- MySQL 8.0
- Node.js 16+
- npm 或 yarn

### 1. 数据库初始化
- 创建数据库 `finance_db` (字符集 utf8mb4)
- 执行 `backend/src/main/resources/db/schema.sql` 中的 SQL 语句

### 2. 配置后端
- 修改 `backend/src/main/resources/application.yml`，设置正确的数据库用户名和密码：
  ```yaml
  spring:
    datasource:
      username: root
      password: your_password
• 在 IDEA 中打开 backend 目录，等待 Maven 依赖下载完成
• 运行 WealthManagementApplication.java 主类

### 3. 启动前端
• 打开终端，进入 frontend 目录
```bash
  #安装依赖
npm install
  #启动开发服务器
npm run serve
```

###4. 访问系统
• 前端地址：http://localhost:8081
• 注册新用户 → 登录 → 开始使用

###📌 注意事项

• 如果后端启动遇到 ExceptionInInitializerError 编译错误，请尝试将 IDEA 编译器切换为 Eclipse ，或升级 JDK 到 11 以上。  
• 默认后端端口为 8080，前端代理已配置指向该端口，无需修改。  
• 所有样式使用纯 CSS 实现，无外部图片资源，如需使用自行配置，存储文件夹已创建。  
