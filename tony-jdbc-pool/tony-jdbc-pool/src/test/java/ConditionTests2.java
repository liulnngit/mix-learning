import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

// 通过condition实现生产者和消费者模式
public class ConditionTests2 {

	// 女神正在做的事
	Lock action = new ReentrantLock();
	// 女神给备胎分的组
	Condition it = action.newCondition();
	Condition money = action.newCondition();
	Condition hippop = action.newCondition();

	// 女神的生活
	String[] ideals = new String[] { "做头发", "修电脑", "旅游" };

	// 即将开展的事情
	String ing = null;

	// 备胎，加女神好友，然后观察女神的朋友圈动态
	class LoserThread extends Thread {
		Condition condition;

		public LoserThread(String name, Condition condition) {
			super(name);
			this.condition = condition;
		}

		public void run() {
			action.lock();
			System.out.println(getName() + ",女神通过了你的好友申请");
			try {
				try {
					condition.await();
					// 如果被女神翻牌子
					System.out.println(getName() + "现在很开心,女神喊我去:" + ing);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} finally {
				action.unlock();
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
			action.lock();
			try {
				ing = ideals[new Random().nextInt(3)];

				System.out.println("女神发了动态");
				if ("做头发".equals(ing)) {
					hippop.signalAll();
				} else if ("修电脑".equals(ing)) {
					it.signalAll();
				} else if ("旅游".equals(ing)) {
					money.signalAll();
				}
			} finally {
				action.unlock();
			}
		}
	};

	@Test
	public void objectTest() throws InterruptedException {
		NvshenThread notifyThread = new NvshenThread();
		LoserThread waitThread02 = new LoserThread("高富帅", money);
		LoserThread waitThread03 = new LoserThread("皮几万", hippop);
		LoserThread waitThread01 = new LoserThread("Tony", it);
		LoserThread waitThread00 = new LoserThread("Mike", it);

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
