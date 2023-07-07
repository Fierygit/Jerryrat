# <center>Jerryrat2.0</center>

[toc]

---

手动搭建  简易服务器  并跑一个简单漂亮的博客框架



## 背景

有很多框架不好看，决定自己实现一个，锻炼代码能力

---

2019 断断续续敲出了 第一个版本！

实现静态文件的处理。

---

2020 元宵节，第二个版本诞生！

此次更新主要有三点增加：

- 新增了通过线程池实现多线程处理连接， 

- 搭建了 一个blog 框架，使用github的风格, 简约风格, 简约也很简单实现！

- 新增图片的传输

![](https://raw.githubusercontent.com/Fierygit/picbed/master/20200208173208.png)

支持高度自定义！！！

[欢迎访问](http://47.100.139.183/) 






## 使用 （所有参数可更改）

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

2.  get

---



## 部署服务器

Jerrycat 部署只需要两条命令

首先复制构建文件夹：

![](https://raw.githubusercontent.com/Fierygit/picbed/master/20200208180723.png)

运行第一条命令：`javac ./*/*.java`(多文件编译一定要在最高目录索引！)， 编译该文件下面的所有文件。每一个文件夹下会对应生成class文件！

![image-20200208180847722](images/image-20200208180847722.png)

最后在刚才的目录运行命令： `java core/Server`

![image-20200208181027996](images/image-20200208181027996.png)

Jerryrat 就跑起来了！！！

如果要Jerryrat后台一直跑，请运行  `nohup java MyServer/server/Server &` 

按两次回车，输出信息将保存在当前目录的  nohup.out

---



## 部署Blog

将要部署的文件.html文件放置到post中， 这里我用的typaro自动生成的html文件，然后写了了处理的java类，去处理，处理完后放到了， WebContent中。

![](https://raw.githubusercontent.com/Fierygit/picbed/master/20200208181244.png)

当然每次都这样放肯定是不可能的！创建一个中转站， 每次更新blog的时候， 放到github就行了， 

然后如何从github 自动到 WebContent 中呢？ 看我的另一篇 [blog](http://47.100.139.183/post/dir5/file2.html)

每次提交之后也要处理更新上去的.html, 于是我写了一个servlet， 调用写好的处理类！

![](https://raw.githubusercontent.com/Fierygit/picbed/master/20200208183125.png)

每次只需要浏览器运行调用servlet 就行！下面我没有写图形界面， 直接手动请求！

![](https://raw.githubusercontent.com/Fierygit/picbed/master/20200208183225.png)

看最后一句话： success ！

更新blog 只需两部操作， 第一步提交，第二部 调用处理类！




## Jerryrat1 中的问题

#### 项目中

- ~~chrome本地测试的时候，服务器会出现阻塞问题，这个在我的知识范围之外！ 下学期学完计网回来。~~
- 端口等信息写死了， 没有配置一个配置文件，目前没有一个好的想法

#### 部署中

- 国内服务器设有安全组，请打开它
- 防火墙， 关了它
- 确保你的端口没有被占用！

- 如果端口被占用，[firefly](www.fireflying.top) 搜索 **关闭端口 **解决
---






## 前景

1. 附加 IOC， AOP， 实现一个基于此服务器的简易Spring  // todo  this holiday
2. 实现一个ORM 框架， 目标 2020 4月前！

---
