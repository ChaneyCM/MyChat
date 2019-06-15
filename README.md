#MyChat
版本：v1.5

##博客详解
[http://www.cnblogs.com/hust_wsh/p/5162412.html](http://www.cnblogs.com/hust_wsh/p/5162412.html)
##软件截图
- 用户1聊天截图

	![用户1聊天截图][1]
- 用户2聊天截图

	![用户2聊天截图][2]
- 服务器日志截图

	![服务器日志截图][3]

##开发进度
- **功能进度**
	- *使用Java NIO初步实现客户端和服务器端
	- *进一步清晰化类接口，提高封装性，定义客户端接口
	- *进一步完善Message类，现在可以在本地正确的序列化和反序列化
	- *增加客户端解码和输入
	- *增加服务器端解码和用户列表维护
	- *正确实现Message以序列化的方式传递
	- *实现用户登录
	- *实现登出功能
	- *服务器获取在线用户列表
	- *实现点对点聊天
	- *实现创建聊天室
	- *获取聊天室列表
	- *实现加入聊天室
	- *实现群聊
	- 客户端线程通信

##功能设计
- **功能模块**
	- 服务器端
		- 与客户端建立通信协议
		- 在线用户列表
		- 聊天室列表
	- 客户端
		- 实现登录
		- 实现登出功能
		- 获取在线用户列表
		- 实现点对点聊天
		- 实现创建聊天室
		- 获取聊天室列表
		- 实现加入聊天室
		- 实现群聊
		- 实现注册
		- 使用心跳报文实现更加可靠的连接判断
		
##开发文档
- **2016-1-28**
	- 修正client的remove SelectionKey bug
	- bug：客户端退出之后发现服务器端的TCP连接没有释放，处在CLOSE_WAIT状态，这说明服务器端没有释放
		TCP连接，那么如何判断客户端close了呢？
	- 原来通过read的返回可以确认，read的返回为0表示读取完毕，返回为-1表示客户端close
	- 如何判断服务器有哪些连接现在是CLOSE_WAIT状态
- **2016-1-27**
	- 进一步完善的话应该将userId和userName区分开，将roomId和roomName区分开，前者不能重复，后者可以重复，作为Map的键应该使用long型的userId和roomId，String型的userName和roomName效率要低一些
	- 可以考虑加入用户注册功能，server端连接MySql数据库，将用户信息，聊天室信息全部持久化保存起来
	- 客户端线程通信的问题
	- ？查证是否有必要使用Collections.synchronizedSet来包装Set
	- 使用Iterator<SelectionKey> iterator=mSelector.selectedKeys().iterator()来代替
		for(SelectionKey key:mSelector.selectedKeys())，并使用iterator.remove()方法删除键之后
		，成功解决相继退出客户端时出现的并发错误
	- ？register Selector时只使用了OP_READ，为啥可以向该通道发送数据呢？
- **2016-1-26**
	- 去掉原有的UserEntity Set和SocketChannel到UserEntity的Map
	- 准备实现群聊
	- 如果有多个用户同时请求操作，就可能引发BindMap的并发操作异常，因为BindMap不是线程安全的，所以我需要一个线程安全的集合
	- 准备使用线程安全的Map来代替BindMap
	- 出现的并发错误：
		java.util.ConcurrentModificationException
		at java.util.HashMap$HashIterator.nextNode(HashMap.java:1429)
		at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
		at com.ioilala.chat.ChatServer.run(ChatServer.java:169)
		at java.lang.Thread.run(Thread.java:745)
		出错地点：
		for(SelectionKey sk:mSelector.selectedKeys())
				{
					mSelector.selectedKeys().remove(sk);
		复现条件：多个用户相继退出客户端时
	- *实现创建聊天室
	- *获取聊天室列表
	- *实现加入聊天室
	- *实现群聊
- **2016-1-25**
	- 发现bug：客户端退出，服务器端没有发生异常，进而删除对应的用户
	- 增加了一个SocketChannel对应UserEntity的HashMap，修改用户异常掉线逻辑
	- 继续测试异常掉线或者退出是否能引起服务器端异常进而执行删除用户的逻辑
	- 测试结果：客户端异常掉线（如直接关闭客户端，服务器端可以捕获到异常进而删除用户信息）
	- 还是有一个bug：客户端选择退出登录时，收不到服务器的回传消息，但是登录时却可以收到服务器的
  		回传消息，原来是没有remove key的原因
  	- 成功实现用户点对点聊天，下一步实现创建聊天室
- **2016-1-23**
	- 终于实现以序列化方式传递Message到服务器端，正确解析
	- 着手服务器端用户信息保存和验证
	- 增加服务器端解码和用户列表维护
	- 增加客户端解码和输入
- **2016-1-22**
	- 增加SerializeHelper类封装序列化成字节数组和反序列化成类的功能
	- 增加StringHelper类，封装字符串相关辅助性方法
	- client消息序列化后到server端还不能正确的反序列化
- **2016-1-21**
	- 调试序列化功能
	- 发现在本地可以正确实习
	-**2016-1-19**
	- 增加UserEntity类代表用户实体，封装用户信息
	- 增加可序列化Message类代表server与client之间发送的消息
- **2016-1-18**
	- 使用Java NIO 实现client和server基本框架

[1]: http://images2015.cnblogs.com/blog/209197/201601/209197-20160127102728863-1500063360.png
[2]: http://images2015.cnblogs.com/blog/209197/201601/209197-20160127102748285-1998049914.png
[3]: http://images2015.cnblogs.com/blog/209197/201601/209197-20160127102711363-1772448242.png
  
	
