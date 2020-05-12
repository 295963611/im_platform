# im_platform<br>
## 使用springboot + netty + rabbitmq 等 来实现一套im程序<br>
该项目初心 实现一套开源IM供大家使用 自己也从这个项目中巩固以及学习新的知识<br>
## 项目布局<br>
 * im-control 主要用于im 控制层 用于交互 <br>
    * im-stage <br>
 * im-core im主要业务实现<br>
 * im-domain im持久化model mapper<br>
 * im-utils 一些工具类实现<br>
 
 ## 该项目才用netty 实现即时通讯 使用spring 进行管理 以下对核心类进行说明<br>
 * WbWebNettyServer websocket 服务启动类 用于主线程工作线程以及端口<br>
 * WbNettyServer    socket 服务启动类 用于主线程工作线程以及端口<br>
 这边是把websocks 以及socket 分为2个端口启动<br>
 * WbMessageAdapter 业务适配器 用于识别消息进入不同的业务处理 ，以及适配器注册<br>
 * IWbMessageService 业务接口 主要业务实现<br>
   * WbLoginTokenService  登陆im服务<br>
   * WbMessageService     消息收发服务<br>
   * WbHeartBeatService   心跳服务<br>
  
测试页面在 chat.html 初始化2个账户 
