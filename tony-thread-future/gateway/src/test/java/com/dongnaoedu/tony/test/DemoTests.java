package com.dongnaoedu.tony.test;

import java.io.FileInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;

// JAVA多线程 - 生产者消费者模型 - 趣味Demo
public class DemoTests {
	// wait/notify必须先拿到test对象的锁
	Object test = new Object();

	// wait / notify 测试
	@Test
	public void testWait() throws InterruptedException {
		System.out.println("这是一个Allen老师和微信附近的女神，酒店约会的故事");

		final Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// allen 堵车
					Thread.sleep(1000L);
					synchronized (test) {
						// 消费者
						System.out.println("Allen老师，到了酒店门口");
						test.wait(); // 消费者
						System.out.println("确认过眼神,我遇上对的人。愉快的度过了一个晚上");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		final Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (test) {
					test.notifyAll();// 生产者
					System.out.println("女神到了酒店门口");
				}
			}
		});

		// 启动线程
		thread1.start();
		thread2.start();

		// 主线程等待上面线程执行完毕后，再结束
		thread1.join();
		thread2.join();

	}

	// park / unpark 生产者 消费者模型
	@Test
	public void testPark() throws InterruptedException {
		System.out.println("这是一个Allen和微信附近的女神，酒店约会的故事");

		final Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000L);
					System.out.println("Allen老师，到了酒店门口");
					// 直接让当前线程进入等待，不执行后续代码
					LockSupport.park();
					System.out.println("确认过眼神,我遇上对的人。愉快的度过了一个晚上");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		final Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("女神到了酒店门口");
					// 直接将指定的线程唤醒
					LockSupport.unpark(thread1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// 启动线程
		thread1.start();
		thread2.start();

		// 主线程等待上面线程执行完毕后，再结束
		thread1.join();
		thread2.join();

	}

	@Test
	public void test() throws Exception {
		FileInputStream file = new FileInputStream(
				"D:\\cloud\\githubs\\jhipster-registry\\node_modules\\node-gyp\\test\\fixtures\\ca.crt");
		CertificateFactory ft = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) ft.generateCertificate(file);
		certificate.checkValidity(new Date(System.currentTimeMillis() + 10000000000000000L));
		PublicKey publicKey = certificate.getPublicKey();
		Encoder b64 = Base64.getEncoder();
		System.out.println("-----BEGIN PUBLIC KEY-----");
		System.out.println(new String(b64.encode(publicKey.getEncoded())));
		System.out.println("-----END PUBLIC KEY-----");

	}
}
