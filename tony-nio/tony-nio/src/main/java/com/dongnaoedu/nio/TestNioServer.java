package com.dongnaoedu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * 第二版 待测试
 * @author Tony
 *
 */
public class TestNioServer {

	// work 线程
	public static WorkThread[] workThreads = new WorkThread[3];

	public static void main(String[] args) throws Exception {
		// 创建一个线程，监听8080端口
		Thread bossThread = new BossThread(8080);
		bossThread.start();

		// 启动一定数量的work线程
		for (int i = 0; i < workThreads.length; i++) {
			workThreads[i] = new WorkThread();
			workThreads[i].start();
		}

		// 这代码的意思是 main线程等待boss线程执行完毕后再关闭
		bossThread.join();
	}

}

class BossThread extends Thread {
	private static ServerSocketChannel serverSocketChannel;

	public static Selector selector;

	/** 绑定服务端口，并且初始化一个selector */
	public BossThread(int port) {
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			// 开启并监听端口
			serverSocketChannel.bind(new InetSocketAddress(port));
			System.out.println("NIO启动:" + port);

			// 选择器，根据指定的条件，选择需要的东西
			// 获取一个选择器
			selector = Selector.open();

			// 在这个socket服务端通道上面，添加刚刚获取的选择器
			// 增加一个过滤条件：OP_ACCEPT 建立新的连接
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
		}
	}

	/** 处理accept新建立的链接 */
	@Override
	public void run() {
		// serversocket
		try {
			while (true) {

				// 根据查询器已有的条件去查询(去操作系统底层管理socket连接的地方查询)
				int count = selector.select(100);
				if (count == 0) {
					continue;
				}

				// 获取查询结果
				Set<SelectionKey> selected = selector.selectedKeys();
				// 遍历查询结果
				Iterator<SelectionKey> iter = selected.iterator();
				while (iter.hasNext()) {
					// 被封装的查询结果
					SelectionKey key = iter.next();
					if (!key.isAcceptable()) {
						continue;
					}
					// 在服务端通道中，取出新的socket连接
					SocketChannel clientSocketChannel = serverSocketChannel.accept();
					// 设置为非阻塞模式
					clientSocketChannel.configureBlocking(false);
					
					// 随机或者其他方式，选择一个work线程去处理这个新连接
					int random = new Random().nextInt(TestNioServer.workThreads.length);
					WorkThread workThread = TestNioServer.workThreads[random];
					workThread.processSocketChannel(clientSocketChannel);

					System.out.println("有新的连接啦");
				}
				// 清除查询结果
				selected.clear();
				// 清除正在被处理的记录
				selector.selectNow();
			}

		} catch (Exception e) {
		}
	}
}

/** I/O处理 */
class WorkThread extends Thread {

	public static Selector selector;

	/** 初始化一个selector */
	public WorkThread() throws IOException {
		// 获取一个选择器
		selector = Selector.open();
		System.out.println(this.getName() + " [work线程启动");
	}

	/** 注册监听事件 */
	public void processSocketChannel(SocketChannel socketChannel) throws Exception {
		// 设置为非阻塞模式
		socketChannel.configureBlocking(false);
		// 监听read事件：OP_READ 有数据传输
		socketChannel.register(selector, SelectionKey.OP_READ);
	}

	/** 处理有数据的链接 */
	@Override
	public void run() {
		try {
			while (true) {

				// 根据查询器已有的条件去查询(去操作系统底层管理socket连接的地方查询)
				int count = selector.select(100);
				if (count == 0) {
					continue;
				}

				// 获取查询结果
				Set<SelectionKey> selected = selector.selectedKeys();
				// 遍历查询结果
				Iterator<SelectionKey> iter = selected.iterator();
				while (iter.hasNext()) {
					// 被封装的查询结果
					SelectionKey key = iter.next();
					if (!key.isReadable()) {
						continue;
					}
					// 如果是isReadable，表明这个查询结果所使用的查询条件是OP_READ，代表某个连接有数据传输过来了

					// 从查询结果中，获取到对应的有数据传输的socket连接
					SocketChannel clientSocketChannel = (SocketChannel) key.channel();
					// 取消查询，表示这个socket连接当前正在被处理，不需要被查询到
					// 设置为非阻塞模式
					clientSocketChannel.configureBlocking(false);
					{
						// 申请一个缓冲区
						ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
						// 将数据读到缓冲区
						clientSocketChannel.read(byteBuffer);
						
						//TODO 请求
						// 接下来对请求数据的处理属于业务流程，不应该放在I/O线程里面，需要自己创建业务线程池去处理
						// 将缓冲区数据读出来并转化为字符串
						String request = new String(byteBuffer.array());
						System.out.println(this.getName() + " 收到新数据，请求内容：" + request);
						// 清除缓冲区
//						byteBuffer.clear();
						
						// TODO 响应
						// 放入一个缓冲区
						ByteBuffer wrap = ByteBuffer.wrap("hello-word ".getBytes());
						// 使用通道，将数据响应给客户端
						clientSocketChannel.write(wrap);

						// 处理完之后，又继续监听该链接
						clientSocketChannel.register(selector, SelectionKey.OP_READ);
					}

				}
				// 清除查询结果
				selected.clear();
				// 清除正在被处理的记录
				selector.selectNow();
			}

		} catch (IOException e) {
		}
	}
}
