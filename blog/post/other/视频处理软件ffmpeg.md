---
title: ffmpeg
date: 2019-10-22
author: Firefly
top: false
cover: true
img: 
coverImg: 
toc: true
mathjax: false
categories: 工具

tags:
  - 树莓派
  - 工具
---

# ffmpeg

转载

ffmpeg的使用方式：
ffmpeg [options] [[infile options] -i infile]... {[outfile options] outfile}...

**常用参数说明：**
主要参数：
-i 设定输入流
-f 设定输出格式
-ss 开始时间

<!-- more -->
**视频参数：**
-b 设定视频流量，默认为200Kbit/s
-r 设定帧速率，默认为25
-s 设定画面的宽与高
-aspect 设定画面的比例
-vn 不处理视频
-vcodec 设定视频编解码器，未设定时则使用与输入流相同的编解码器

**音频参数：**
-ar 设定采样率
-ac 设定声音的Channel数
-acodec 设定声音编解码器，未设定时则使用与输入流相同的编解码器
-an 不处理音频

### 1.视频格式转换

（其实格式转换说法不太准确，但大家都这么叫，准确的说，应该是视频容器转换）
比如一个avi文件，想转为mp4，或者一个mp4想转为ts。
ffmpeg -i input.avi output.mp4
ffmpeg -i input.mp4 output.ts
我目测这个已经能满足很多人的需求了。

### 2.提取音频
比如我有一个“晓松奇谈”，可是我不想看到他的脸，我只想听声音， 地铁上可以听，咋办？
ffmpeg -i 晓松奇谈.mp4 -acodec copy -vn output.aac
上面的命令，默认mp4的audio codec是aac，如果不是会出错，咱可以暴力一点，不管什么音频，都转为最常见的aac。
ffmpeg -i 晓松奇谈.mp4 -acodec aac -vn output.aac

### 3.提取视频
我目测有些IT员工，特别是做嵌入式的，比如机顶盒，想debug一下，没有音频的情况下，播放一个视频几天几夜会不会crash，这时候你需要一个纯视频文件，可以这么干。
ffmpeg -i input.mp4 -vcodec copy -an output.mp4

### 4.视频剪切
经常要测试视频，但是只需要测几秒钟，可是视频却有几个G，咋办？切啊！
下面的命令，就可以从时间为00:00:15开始，截取5秒钟的视频。
ffmpeg -ss 00:00:15 -t 00:00:05 -i input.mp4 -vcodec copy -acodec copy output.mp4
-ss表示开始切割的时间，-t表示要切多少。上面就是从开始，切5秒钟出来。

### 5.码率控制
码率控制对于在线视频比较重要。因为在线视频需要考虑其能提供的带宽。

那么，什么是码率？很简单：
bitrate = file size / duration
比如一个文件20.8M，时长1分钟，那么，码率就是：
biterate = 20.8M bit/60s = 20.8*1024\*1024\*8 bit/60s= 2831Kbps
一般音频的码率只有固定几种，比如是128Kbps，
那么，video的就是
video biterate = 2831Kbps -128Kbps = 2703Kbps。

说完背景了。好了，来说ffmpeg如何控制码率。
ffmpg控制码率有3种选择，-minrate -b:v -maxrate
-b:v主要是控制平均码率。
比如一个视频源的码率太高了，有10Mbps，文件太大，想把文件弄小一点，但是又不破坏分辨率。
ffmpeg -i input.mp4 -b:v 2000k output.mp4
上面把码率从原码率转成2Mbps码率，这样其实也间接让文件变小了。目测接近一半。
不过，ffmpeg官方wiki比较建议，设置b:v时，同时加上 -bufsize
-bufsize 用于设置码率控制缓冲器的大小，设置的好处是，让整体的码率更趋近于希望的值，减少波动。（简单来说，比如1 2的平均值是1.5， 1.49 1.51 也是1.5, 当然是第二种比较好）
ffmpeg -i input.mp4 -b:v 2000k -bufsize 2000k output.mp4

-minrate -maxrate就简单了，在线视频有时候，希望码率波动，不要超过一个阈值，可以设置maxrate。
ffmpeg -i input.mp4 -b:v 2000k -bufsize 2000k -maxrate 2500k output.mp4

### 6.视频编码格式转换

比如一个视频的编码是MPEG4，想用H264编码，咋办？
ffmpeg -i input.mp4 -vcodec h264 output.mp4
相反也一样
ffmpeg -i input.mp4 -vcodec mpeg4 output.mp4

当然了，如果ffmpeg当时编译时，添加了外部的x265或者X264，那也可以用外部的编码器来编码。（不知道什么是X265，可以Google一下，简单的说，就是她不包含在ffmpeg的源码里，是独立的一个开源代码，用于编码HEVC，ffmpeg编码时可以调用它。当然了，ffmpeg自己也有编码器）
ffmpeg -i input.mp4 -c:v libx265 output.mp4
ffmpeg -i input.mp4 -c:v libx264 output.mp4

### 7.只提取视频ES数据

这个可能做开发的人会用到，顺便提一下吧。
ffmpeg –i input.mp4 –vcodec copy –an –f m4v output.h264

### 8.过滤器的使用
这个我在另一篇博客提到了，这里贴一下吧。

#### 8.1 将输入的1920x1080缩小到960x540输出:
ffmpeg -i input.mp4 -vf scale=960:540 output.mp4
//ps: 如果540不写，写成-1，即scale=960:-1, 那也是可以的，ffmpeg会通知缩放滤镜在输出时保持原始的宽高比。

#### 8.2 为视频添加logo
比如，我有这么一个图片

想要贴到一个视频上，那可以用如下命令：
./ffmpeg -i input.mp4 -i iQIYI_logo.png -filter_complex overlay output.mp4
结果如下所示：

要贴到其他地方？看下面：
右上角：
./ffmpeg -i input.mp4 -i logo.png -filter_complex overlay=W-w output.mp4
左下角：
./ffmpeg -i input.mp4 -i logo.png -filter_complex overlay=0:H-h output.mp4
右下角：
./ffmpeg -i input.mp4 -i logo.png -filter_complex overlay=W-w:H-h output.mp4

#### 8.3 去掉视频的logo
有时候，下载了某个网站的视频，但是有logo很烦，咋办？有办法，用ffmpeg的delogo过滤器。
语法：-vf delogo=x:y:w:h[:t[:show]]
x:y 离左上角的坐标
w:h logo的宽和高
t: 矩形边缘的厚度默认值4
show：若设置为1有一个绿色的矩形，默认值0。

ffmpeg -i input.mp4 -vf delogo=0:0:220:90:100:1 output.mp4
结果如下所示：

### 9.抓取视频的一些帧，存为jpeg图片
比如，一个视频，我想提取一些帧，存为图片，咋办？
ffmpeg -i input.mp4 -r 1 -q:v 2 -f image2 pic-%03d.jpeg
-r 表示每一秒几帧
-q:v表示存储jpeg的图像质量，一般2是高质量。
如此，ffmpeg会把input.mp4，每隔一秒，存一张图片下来。假设有60s，那会有60张。

60张？什么？这么多？不要不要。。。。。不要咋办？？
可以设置开始的时间，和你想要截取的时间呀。
ffmpeg -i input.mp4 -ss 00:00:20 -t 10 -r 1 -q:v 2 -f image2 pic-%03d.jpeg
-ss 表示开始时间
-t表示共要多少时间。
如此，ffmpeg会从input.mp4的第20s时间开始，往下10s，即20~30s这10秒钟之间，每隔1s就抓一帧，总共会抓10帧。

怎么样，好用吧。^^

### **其他小众的用法**
1.输出YUV420原始数据
对于一下做底层编解码的人来说，有时候常要提取视频的YUV原始数据。
怎么坐？很简答：
ffmpeg -i input.mp4 output.yuv
怎么样，是不是太简单啦？！！！哈哈

如果你想问yuv的数据，如何播放，我不会告诉你，RawPlayer挺好用的！！

那如果我只想要抽取某一帧YUV呢？
简单，你先用上面的方法，先抽出jpeg图片，然后把jpeg转为YUV。
比如：
你先抽取10帧图片。
ffmpeg -i input.mp4 -ss 00:00:20 -t 10 -r 1 -q:v 2 -f image2 pic-%03d.jpeg
结果：

`-rw-rw-r-- 1 chenxf chenxf    296254  7月 20 16:08 pic-001.jpeg`
`-rw-rw-r-- 1 chenxf chenxf    300975  7月 20 16:08 pic-002.jpeg`
`-rw-rw-r-- 1 chenxf chenxf    310130  7月 20 16:08 pic-003.jpeg`
`-rw-rw-r-- 1 chenxf chenxf    268694  7月 20 16:08 pic-004.jpeg`
`-rw-rw-r-- 1 chenxf chenxf    301056  7月 20 16:08 pic-005.jpeg`
`-rw-rw-r-- 1 chenxf chenxf    293927  7月 20 16:08 pic-006.jpeg`
`-rw-rw-r-- 1 chenxf chenxf    340295  7月 20 16:08 pic-007.jpeg`
`-rw-rw-r-- 1 chenxf chenxf    430787  7月 20 16:08 pic-008.jpeg`
`-rw-rw-r-- 1 chenxf chenxf    404552  7月 20 16:08 pic-009.jpeg`
`-rw-rw-r-- 1 chenxf chenxf    412691  7月 20 16:08 pic-010.jpeg`

然后，你就随便挑一张，转为YUV:
ffmpeg -i pic-001.jpeg -s 1440x1440 -pix_fmt yuv420p xxx3.yuv
如果-s参数不写，则输出大小与输入一样。

当然了，YUV还有yuv422p啥的，你在-pix_fmt 换成yuv422p就行啦！

2. H264编码profile & level控制
背景知识
先科普一下profile&level吧，知道的请飘过。（这里讨论最常用的H264）
H.264有四种画质级别,分别是baseline, extended, main, high：
　　1、Baseline Profile：基本画质。支持I/P 帧，只支持无交错（Progressive）和CAVLC；
　　2、Extended profile：进阶画质。支持I/P/B/SP/SI 帧，只支持无交错（Progressive）和CAVLC；(用的少)
　　3、Main profile：主流画质。提供I/P/B 帧，支持无交错（Progressive）和交错（Interlaced），
　　　 也支持CAVLC 和CABAC 的支持；
　　4、High profile：高级画质。在main Profile 的基础上增加了8x8内部预测、自定义量化、 无损视频编码和更多的YUV 格式；
H.264 Baseline profile、Extended profile和Main profile都是针对8位样本数据、4:2:0格式(YUV)的视频序列。在相同配置情况下，High profile（HP）可以比Main profile（MP）降低10%的码率。
根据应用领域的不同，Baseline profile多应用于实时通信领域，Main profile多应用于流媒体领域，High profile则多应用于广电和存储领域。

下图清楚的给出不同的profile&level的性能区别。
profile


level


2.1 ffmpeg如何控制profile&level
举3个例子吧
ffmpeg -i input.mp4 -profile:v baseline -level 3.0 output.mp4

ffmpeg -i input.mp4 -profile:v main -level 4.2 output.mp4

ffmpeg -i input.mp4 -profile:v high -level 5.1 output.mp4

如果ffmpeg编译时加了external的libx264，那就这么写：
ffmpeg -i input.mp4 -c:v libx264 -x264-params "profile=high:level=3.0" output.mp4

从压缩比例来说，baseline< main < high，对于带宽比较局限的在线视频，可能会选择high，但有些时候，做个小视频，希望所有的设备基本都能解码（有些低端设备或早期的设备只能解码baseline），那就牺牲文件大小吧，用baseline。自己取舍吧！

苹果的设备对不同profile的支持。


2.2. 编码效率和视频质量的取舍(preset, crf)
除了上面提到的，强行配置biterate，或者强行配置profile/level，还有2个参数可以控制编码效率。
一个是preset，一个是crf。
preset也挺粗暴，基本原则就是，如果你觉得编码太快或太慢了，想改改，可以用profile。
preset有如下参数可用：

ultrafast, superfast, veryfast, faster, fast, medium, slow, slower, veryslow and placebo.
编码加快，意味着信息丢失越严重，输出图像质量越差。

CRF(Constant Rate Factor): 范围 0-51: 0是编码毫无丢失信息, 23 is 默认, 51 是最差的情况。相对合理的区间是18-28.
值越大，压缩效率越高，但也意味着信息丢失越严重，输出图像质量越差。

举个例子吧。
ffmpeg -i input -c:v libx264 -profile:v main -preset:v fast -level 3.1 -x264opts crf=18
(参考自：https://trac.ffmpeg.org/wiki/Encode/H.264)

2.3. H265 (HEVC)编码tile&level控制
背景知识
和H264的profile&level一样，为了应对不同应用的需求，HEVC制定了“层级”(tier) 和“等级”(level)。
tier只有main和high。
level有13级，如下所示：

不多说，直接给出怎么用。（supposed你用libx265编码）
ffmpeg -i input.mp4 -c:v libx265 -x265-params "profile=high:level=3.0" output.mp4

原文链接：https://blog.csdn.net/newchenxf/article/details/51384360