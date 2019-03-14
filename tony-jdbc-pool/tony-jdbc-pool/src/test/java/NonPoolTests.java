import java.sql.Connection;
import java.sql.ResultSet;
import java.util.concurrent.CountDownLatch;

import com.dongnaoedu.tony.TonyJdbcConnect;

public class NonPoolTests {
	final static int threadnums = 100;

	// java.util.concurrent juc
	private final static CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(threadnums);

	public static void main(String[] args) {

		for (int i = 0; i < threadnums; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						// 等待计数器到0，再执行后面的代码
						COUNT_DOWN_LATCH.await();

						String sql = "select ticket_seq from tb_ticket limit 1";
						Connection connection = new TonyJdbcConnect().getConnection();
						ResultSet result = connection.createStatement().executeQuery(sql);
						result.next();
						System.out.println(
								Thread.currentThread().getName() + "查询结果=====" + result.getString("ticket_seq"));

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}).start();

			// 计数器-1
			COUNT_DOWN_LATCH.countDown();
		}

	}
}
