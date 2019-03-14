package com.tony.test.dubbo.consumer;

/**
 * 数字与字节数组转换工具
 * <p>
 * short 占2字节数组
 * </p>
 * <p>
 * int 占4字节
 * </p>
 * <p>
 * long 占8字节
 * </p>
 * 
 * @author Tony
 *
 */
public class ByteUtil {
	/** short转2字节数组 */
	public static byte[] short2bytes(short v) {
		byte[] b = new byte[4];
		b[1] = (byte) v;
		b[0] = (byte) (v >>> 8);
		return b;
	}

	/** int转4字节数组 */
	public static byte[] int2bytes(int v) {
		byte[] b = new byte[4];
		b[3] = (byte) v;
		b[2] = (byte) (v >>> 8);
		b[1] = (byte) (v >>> 16);
		b[0] = (byte) (v >>> 24);
		return b;
	}

	/** long转8字节数组 */
	public static byte[] long2bytes(long v) {
		byte[] b = new byte[8];
		b[7] = (byte) v;
		b[6] = (byte) (v >>> 8);
		b[5] = (byte) (v >>> 16);
		b[4] = (byte) (v >>> 24);
		b[3] = (byte) (v >>> 32);
		b[2] = (byte) (v >>> 40);
		b[1] = (byte) (v >>> 48);
		b[0] = (byte) (v >>> 56);
		return b;
	}

	/** 字节数组转字符串 */
	public static String bytesToHexString(byte[] bs) {
		if (bs == null || bs.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		String tmp = null;
		for (byte b : bs) {
			tmp = Integer.toHexString(Byte.toUnsignedInt(b));
			if (tmp.length() < 2) {
				sb.append(0);
			}
			sb.append(tmp);
		}
		return sb.toString();
	}
}
