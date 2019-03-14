import java.sql.Connection;
import java.sql.ResultSet;
import java.util.concurrent.CountDownLatch;

import com.dongnaoedu.tony.OnePool;
import com.dongnaoedu.tony.TonyJdbcConnect;
import com.dongnaoedu.tony.TonyPoolInterface;

/**
 * 线程池测试
 *
 */
public class PoolTests {
	final static int threadnums = 1000;

	// java.util.concurrent juc
	private final static CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(threadnums);

	public static void main(String[] args) throws Exception {

		TonyPoolInterface tonyPool = new OnePool();
		tonyPool.init(20, 30000, 10);

		for (int i = 0; i < threadnums; i++) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					TonyJdbcConnect tonyJdbcConnect = null;
					try {
						// 等待计数器到0，再执行后面的代码
						COUNT_DOWN_LATCH.await();

						String sql = "select ticket_seq from tb_ticket limit 1";
						tonyJdbcConnect = tonyPool.getResource();

						Connection connection = tonyJdbcConnect.getConnection();
						ResultSet result = connection.createStatement().executeQuery(sql);
						result.next();
						System.out.println(
								Thread.currentThread().getName() + "查询结果=====" + result.getString("ticket_seq"));
						result.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					} finally {
						tonyPool.returnConnect(tonyJdbcConnect);
					}
				}
			});
			thread.start();
			COUNT_DOWN_LATCH.countDown();
		}

	}
}
