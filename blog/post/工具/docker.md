docker的使用步骤：

1. 安装 Docker
2. 去Docker仓库找到这个软件对应的镜像
3. 使用Docker运行这个镜像，这个镜像就会生成一个Docker容器
4. 对容器的启动停止就对软件启动和重启

## 安装

内核版本： 3.10 以上

`uname -r`

安装Docker：

安装： `yum install docker`

启动：`systemctl start docker` 

查看版本：`docker -v`

开机自启：`systemctl enable docker`

停止：`systemctl stop docker`

查找： `docker search name`

下载：`docker pull mysql`

查看所有镜像：  `docker images`

删除：`docker rmi ID`

## 容器操作

软件镜像（QQ）>> 运行镜像 >> 产生一个容器（运行的QQ）

运行  `docker --name myname -d name(-d 后台)`

查看已经运行  `docker ps`

停止容器： `docker stop ID`

启动容器： `docker start ID`

映射端口：`docker run -d -p port:port   name`

查看日志： `docker logs name`



## 安装mysql

`docker pull mysql`

`docker run -p port:port --name mysql01 -e MYSQL_ROOT_PASSWORD=123456 -d mysql ` 

