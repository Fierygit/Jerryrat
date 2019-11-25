# <center>Jerryrat</center>

[toc]

---



## 背景

2019，编程新技术实验要求写一个简单的操作数据库的网页，结果环境搭建半天，Tomcat环境配置太太太南了，想到之前学习过高淇老师的服务器搭建教学，于是乎，敲下此个Jerryrat, 简化环境的搭建！

你也许不知道Tomcat，但你一定知道一只叫Tom的cat， tom是大哥，rat是小弟，Jerrycat由此而来！

---






## 使用 

###  1. 静态文件

静态文件直接放到 WebContent文件夹下，访问时直接加到URL后缀即可！

### 2. servlet

servlet麻烦一点，首先配置WebContent/config文件夹下面的web.xml文件：

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app>
	<servlet>
		<servlet-name>index</servlet-name>
		<servlet-class>Index</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>index</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
	<--! ----------------------    -->
</web-app>
```

以上的配置文件索引：  / -> index  -> Index    

当输入ip:port/8888/  就会运行Index.class, 默认找 Servlet文件夹。

### 3. 支持方法

1.  post 

   。。。。。。

2.  get

   。。。。。。

。。。。。。。。

---





## 部署

Jerrycat 部署只需要两条命令！！！

首先构建如下目录：

![image-20191125101834709](./images/image-20191125101834709.png)

运行第一条命令：`javac MyServer/*/*.java`， 编译该文件下面的所有文件。

- server文件夹下生成如下文件：

![image-20191125102257256](./images/image-20191125102257256.png)

- Servlet文件夹下生成如下文件：

![image-20191125102412677](./images/image-20191125102412677.png)

- WebContent文件夹下是静态文件：

![image-20191125102454520](./images/image-20191125102454520.png)

最后运行命令： `java MyServer/server/Server`

![image-20191125102601907](./images/image-20191125102601907.png)

Jerryrat 就跑起来了！！！

如果要Jerryrat后台一直跑，请运行  `nohup java MyServer/server/Server &` 

按两次回车，输出信息将保存在  nohup.out

查看运行的结果：

![image-20191125110225413](./images/image-20191125110225413.png)

---






## 问题



#### 项目中

- chrome本地测试的时候，服务器会出现阻塞问题，这个在我的知识范围之外！ 下学期学完计网回来。
- 端口等信息写死了， 没有配置一个配置文件，目前没有一个好的想法



#### 部署中

- 国内服务器设有安全组，请打开它
- 防火墙， 关了它
- 确保你的端口没有被占用！

- 如果端口被占用，[firefly](www.fireflying.top) 搜索 **关闭端口 **解决
---






## 前景

1. 附加 IOC， AOP， 实现一个基于此服务器的简易Spring  // todo  this holiday
2. 传输图片和视频， 学完计网回来！！！
3. 自定义一个简单的blog框架，实现简单的静态网站！！！
4. Tom的诙谐幽默，Jerry的聪明才智。

---
