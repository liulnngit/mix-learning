import java.util.Random;

import org.junit.Test;

// 通过wait/notify 实现生产者和消费者模式
public class ConditionTests {

	// 女神正在做的事
	Object action = new Object();

	// 女神的生活
	String[] ideals = new String[] { "做头发", "修电脑", "旅游" };

	// 即将开展的事情
	String ing = null;

	// 备胎，加女神好友，然后观察女神的朋友圈动态
	class LoserThread extends Thread {
		public LoserThread(String name) {
			super(name);
		}

		public void run() {
			synchronized (action) {
				System.out.println(getName() + ",女神通过了你的好友申请");
				try {
					action.wait();
					// 如果被女神翻牌子
					System.out.println(getName() + "现在很开心,女神喊我去:" + ing);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 女神
	class NvshenThread extends Thread {
		public void run() {
			System.out.println("女神在想，接下来去干什么事...准备发动态");
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (action) {
				ing = ideals[new Random().nextInt(3)];
				System.out.println("女神发了动态");
				// 通知
				action.notifyAll();
			}
		}
	};

	@Test
	public void objectTest() throws InterruptedException {
		NvshenThread notifyThread = new NvshenThread();
		LoserThread waitThread02 = new LoserThread("高富帅");
		LoserThread waitThread03 = new LoserThread("皮几万");
		LoserThread waitThread01 = new LoserThread("Tony");
		LoserThread waitThread00 = new LoserThread("Mike");

		notifyThread.start();
		waitThread00.start();
		waitThread01.start();
		waitThread02.start();
		waitThread03.start();

		// join的作用（主线程会等待下面5个线程执行完毕后，才结束）
		notifyThread.join();
		waitThread00.join();
		waitThread01.join();
		waitThread02.join();
		waitThread03.join();
	}
}
