---
title: ssh配置公钥
date: 2019-10-27
author: Firefly
cover: false
top: false
toc: true
categories:
- 经验
tags:
  - 脚本
---





# ssh的使用

## 背景

ssh连接服务器是经常要操作的步骤，可当每次都要手动输入 ```ssh user@ip ``` ，然后加上密码连接可谓是及其地麻烦，虽然有xs的记录密码，但当要将服务器分享给他人时，为了方便别人，可以配置公钥，配好后对方直接使用ssh username，就可以直接连上，密码都不用输入。

## 原理
<!-- more -->
。。。 不会，

## 操作

### pc端

要使用vps的pc端，cmd输入    ``` ssh-keygen -t rsa```，如果已经创建过就不用了，直接下两步，不然之前配的秘钥就换了，比如git配置的ssh密码。

![](https://raw.githubusercontent.com/Fierygit/picbed/master/ssh1.jpg)

这里我就不创建了，创建好按照提示进入创建的目录,一般默认就行，在用户目录下。

![](https://raw.githubusercontent.com/Fierygit/picbed/master/ssh2.jpg)

新建一个 config文件

![](https://raw.githubusercontent.com/Fierygit/picbed/master/ssh3.jpg)

注意是没有后缀的

![](https://raw.githubusercontent.com/Fierygit/picbed/master/ssh4.jpg)

其中

```
Host 为服务器别名,取一个方便记的
HostName 为服务器域名(ip)
User 为登录的账号
```

pc端操作完毕！

### vps

终端输入    ``` ssh-keygen -t rsa```   , 回车回车回车。。。。

![](https://raw.githubusercontent.com/Fierygit/picbed/master/sshshiyong.jpg)

```ls -a``` 可以查看到有一个 .ssh文件夹

进入,将刚才pc端创建的  id_ras.pub 放到 到 authorized_keys文件里面。

![](https://raw.githubusercontent.com/Fierygit/picbed/master/ssh5.jpg)

搞定！！！ 

之后直接在cmd里输入 ``` ssh Host (为服务器别名,取一个方便记的) ```就可以直接连上，不用输入密码。

![](https://raw.githubusercontent.com/Fierygit/picbed/master/ssh6.jpg)



配合vscode的ssh连接，简直不能再方便了！！！！

只要需要三步，就可以直接键代码跑到服务器，像操作自己的电脑一样，真的真的真的太太太太太方便了，谁用谁会爱上它！！！！！（当然vscode要下插件）

![](https://raw.githubusercontent.com/Fierygit/picbed/master/ssh7.jpg)