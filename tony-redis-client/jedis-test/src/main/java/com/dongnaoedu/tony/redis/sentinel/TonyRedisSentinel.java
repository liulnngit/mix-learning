package com.dongnaoedu.tony.redis.sentinel;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import redis.clients.jedis.Jedis;

// 手写哨兵服务
public class TonyRedisSentinel {
	static String master;
	// 所有 slave
	static final Vector<String> slaveRedisServers = new Vector<String>();
	// 坏掉的实例
	static final Vector<String> badRedisServers = new Vector<String>();

	public static void main(String[] args) throws Exception {
		// 配置 redis master
		config("127.0.0.1:6380");
		// 定时任务
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				// 核心三部曲在redis官方提供的哨兵中，也是这样实现的
				// 检测 master是否可以 - 老大死了没有
				checkMaster();
				// 更新slave列表
				updateSlaves();
				// 3、检测坏掉的实例是否恢复 正常 - 已经死掉的老大 有没有复活
				checkBadServer();

			}
		}, 3000L, 3000L);

		// 开启端口接收请求
		open();
	}

	// 检查坏道服务器是否恢复正常
	private static void checkBadServer() {
		// 获取所有slave
		Iterator<String> iterator = badRedisServers.iterator();
		while (iterator.hasNext()) {
			String bad = iterator.next();
			try {
				String badHost = bad.split(":")[0];
				int badPort = Integer.parseInt(bad.split(":")[1]);
				Jedis badServer = new Jedis(badHost, badPort);
				badServer.ping();

				// 如果ping没有问题，则挂在当前的master
				badServer.slaveof(master.split(":")[0], Integer.parseInt(master.split(":")[1]));
				badServer.close();

				slaveRedisServers.add(bad);
				iterator.remove();
				System.out.println(bad + " 恢复正常，当前master:" + master);
			} catch (Exception e) {
			}
		}
	}

	private static void updateSlaves() {
		// 获取所有slave
		try {
			String masterHost = master.split(":")[0];
			int masterPort = Integer.parseInt(master.split(":")[1]);
			Jedis jedis = new Jedis(masterHost, masterPort);
			// master 主从 信息 info replication
			String info_replication = jedis.info("replication");
			// 解析info replication
			String[] lines = info_replication.split("\r\n");
			int slaveCount = Integer.parseInt(lines[2].split(":")[1]);
			if (slaveCount > 0) {
				slaveRedisServers.clear();
				for (int i = 0; i < slaveCount; i++) {
					String port = lines[3 + i].split(",")[1].split("=")[1];
					slaveRedisServers.add("127.0.0.1:" + port);
				}
			}
			System.out.println("更新slave列表:" + Arrays.toString(slaveRedisServers.toArray(new String[] {})));
			jedis.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("更新slave失败:" + e.getMessage());
		}
	}

	private static void config(String ms) {
		master = ms;
	}

	private static void checkMaster() {
		// 主从切换
		// 检查状态
		System.out.println("检查master状态:" + master);
		String masterHost = master.split(":")[0];
		int masterPort = Integer.parseInt(master.split(":")[1]);
		try {
			Jedis jedis = new Jedis(masterHost, masterPort);
			jedis.ping();
			jedis.close();
		} catch (Exception e) {
			System.out.println(master + "........挂啦");
			// master挂掉啦
			badRedisServers.add(master);
			// 切换master
			changeMaster();
		}
	}

	/** 切换master */
	private static void changeMaster() {
		Iterator<String> iterator = slaveRedisServers.iterator();
		// 选举算法
		while (iterator.hasNext()) {
			String slave = iterator.next();
			try {
				String slaveHost = slave.split(":")[0];
				int slavePort = Integer.parseInt(slave.split(":")[1]);
				Jedis jedis = new Jedis(slaveHost, slavePort);
				// 不要做小弟了
				jedis.slaveofNoOne();
				jedis.close();
				master = slave;
				System.out.println("产生新的master:" + master);
				break;
			} catch (Exception e) {
				badRedisServers.add(slave);
			} finally {
				iterator.remove();
			}
		}

		// 所有slave切到新的master
		for (String slave : slaveRedisServers) {
			String slaveHost = slave.split(":")[0];
			int slavePort = Integer.parseInt(slave.split(":")[1]);
			Jedis jedis = new Jedis(slaveHost, slavePort);
			// 加入新的master管理
			jedis.slaveof(master.split(":")[0], Integer.parseInt(master.split(":")[1]));

			jedis.close();
		}
	}

	private static void open() throws Exception {
		// SENTINEL get-master-addr-by-name master
		// tcp port
		ServerSocket sentlnel = new ServerSocket(26379);
		System.out.println("java版本哨兵服务启动成功:" + 26379);
		Socket socket;
		while ((socket = sentlnel.accept()) != null) {
			try {
				
				while(true) {

					System.out.println("一个链接....");
					InputStream inputStream = socket.getInputStream();
					byte[] request = new byte[1024];
					inputStream.read(request);
					// 解析 get-master-addr-by-name 请求
					String req = new String(request);
					System.out.println("收到请求：");
					System.out.println(req);
					System.out.println("##############打印结束");
					System.out.println();
					
					String[] params = req.split("\r\n");
					
					if("get-master-addr-by-name".equals(params[4])){
						// 返回结果
						// *2\r\n
						// $9\r\n
						// 127.0.0.1
						// $4\r\n
						// 6380
						String result = "*2\r\n" + 
								"$9\r\n" + 
								master.split(":")[0]+"\r\n" + // master host
								"$4\r\n" + 
								master.split(":")[1]+"\r\n"; // master port
						
						socket.getOutputStream().write(result.getBytes());
					}
					
				
				}
			}catch (Exception e) {
			}
			
		}
		
	}

}
