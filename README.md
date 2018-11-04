#### 零、前言

>1.很久以前在慕课网看过鸿洋的五子棋实现的视频，由于是教学，功能比较简单。[详情可见](https://www.imooc.com/learn/641)
2.然后我基于此拓展了一些功能，比如音效、自定义网格数，选择图片设置背景、截图、悔棋等。
3.最想做的当然是联网对战啦，当时实力不济，只好暂放，现在回来看看，感觉可以做。
4.核心是在每次绘制时将坐标点传给服务端，然后服务端再将数据发送给两个手机，在视图上显示。
5.该应用可以开启服务端，也可以连接服务端,具体如下：

![五子棋.png](https://upload-images.jianshu.io/upload_images/9414344-dd8db4a9ccb6d9c0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


##### 本文着重于介绍：

>1.通过文件记录点位和打开时复原数据
2.基于TCP的Socket实现两个手机间的数据交互，完成两个手机的网络对战
3.五子棋的具体实现比较基础，就不在这贴了，会说明一下重要的方法接口，文尾附上github源码地址，可自行查看



##### 网络对战的流程概要：

![流程概览.png](https://upload-images.jianshu.io/upload_images/9414344-9561962b73a2dded.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



##### 五子棋的接口(public)方法

```
start();//重新开局
backStep();//悔棋

getCurrentPos()//获取落点
getWhites()//获取白子集合
getBlacks()//获取黑子集合

//根据点位来设置棋盘
public void setPoints(ArrayList<Point> whites, ArrayList<Point> blacks)

结束回调接口：OnGameOverListener ：void gameOver(boolean isWhiteWin)
绘制回调接口：OnDrawListener：void drawing(boolean isWhite)
```

##### 落点数据双向共享

![落点数据双向共享.png](https://upload-images.jianshu.io/upload_images/9414344-d8486a8dc72c224b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 最终效果实现一次点击，两个手机同步显示

![最终效果.png](https://upload-images.jianshu.io/upload_images/9414344-0ff1d07c9c3732c9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



