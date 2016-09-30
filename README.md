# cnmall
## 简介
这是一个模仿菜鸟商城写的Demo<br>
修改了一些原版中不是很友好的代码<br>
对一些素材进行重绘<br>
对720P和1080P分辨率做了简单的适配(原版只支持1280*768)<br>

## 截图
#### PNG
[![home-png]](https://github.com/orage/cnmall/blob/master/app/src/main/java/com/oranges/cnmall/fragment/HomeFragment.java)  
[home-png]:https://github.com/orage/cnmall/blob/master/screenshot/home.png "主页"  
#### GIF
[![home-gif]](https://github.com/orage/cnmall/blob/master/app/src/main/java/com/oranges/cnmall/fragment/HomeFragment.java)  
[home-gif]:https://github.com/orage/cnmall/blob/master/screenshot/home.gif "主页"  

## 目录结构
*app 
  *activity  除MainActivity之外的所有Activity
  *adapter  所有Adapter
  *app  Application
  *bean  entity
  *consts  项目中使用的静态常量
  *fragment  所有Fragment
  *http  okHttp的封装类和回调
  *msg  登录请求以及订单等安全请求 && 相应的状态码和加密的封装
  *provider  购物车的provider
  *utils 工具类
  *widget 自定义控件
*bdpaysdk 百度钱包的sdk
*paysdk 支付sdk

## 一些问题
一些接口没有实现(譬如：我的收藏，下单时选择地址)<br>
对其他分辨率没有适配(尤其是低分辨率的手机显示上会有影响)<br>
标题栏当显示'back'按钮时标题没有居中的问题<br>
项目`首页`搜索功能(模糊查询商品)没有实现<br>
项目分类并非对应类目商品(这个是数据库写数据时没有对应)<br>
项目一些String && dimen常量没有写在对应的String.xml && dimen.xml(写例子的时候图方便了)<br>
项目使用的Fresco框架只做了简单的配置，低配置移动设备可能在图片加载时会引发GC<br>
项目使用的短信sdk内容没做处理，当然这是个细节问题<br>
项目使用的分享sdk内容没做处理，当然这也是个细节问题<br>
项目使用的支付sdk只使用测试环境，因为考虑安全因素个人是没办法申请正式的key<br>

## 已知Bug
1.`我的订单`有时列表滑动返回顶部卡顿(应该是使用了自定义布局的关系)<br>
2.`商品详情`点击购买,订单图片的width && height小于`购物车`结算的订单图片<br>
3.`新建收货地址`所使用的级联菜单显示后面部分的区/县FC(应该是控件本身的问题，ArrayList遍历时改变了list.size())<br>
4.暂时只找到这些，欢迎补充(可能因为这个项目只作用于展示的用途,细节上没有去仔细雕琢,(｀・ω・´) )

## 附件
项目所使用的 [API](http://pan.baidu.com/s/1o8m3KRC) 存放在度盘,提取码: uqe5 
