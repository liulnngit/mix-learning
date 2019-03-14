package com.dongnaoedu.tony.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 从socket封装一个response对象
 * @author 动脑学院.Tony老师
 *
 */
public class ResponseFactory {
	/**
	 * 封装socket对象，并从中信息，构建出response对象
	 * 
	 * @param socket
	 * @return
	 */
	public static HttpServletResponse createResponse(Socket socket) {
		// TODO 实现HttpServletResponse并构建一个response对象
		return new HttpServletResponse() {

			@Override
			public void setLocale(Locale loc) {
			}

			@Override
			public void setContentType(String type) {
			}

			@Override
			public void setContentLength(int len) {
			}

			@Override
			public void setCharacterEncoding(String charset) {
			}

			@Override
			public void setBufferSize(int size) {
			}

			@Override
			public void resetBuffer() {

			}

			@Override
			public void reset() {

			}

			@Override
			public boolean isCommitted() {
				return false;
			}

			@Override
			public PrintWriter getWriter() throws IOException {
				// servlet返回信息，就是通过outputStream
				return new PrintWriter(getOutputStream());
			}

			@Override
			public ServletOutputStream getOutputStream() throws IOException {
				// 自己实现一个ServletOutputStream
				return new ServletOutputStream() {

					@Override
					public void write(int b) throws IOException {
						// TODO 实际用的就是那个socket连接对象
						socket.getOutputStream().write(b);
					}

					@Override
					public void write(byte[] b) throws IOException {
						socket.getOutputStream().write(b);
					}

					@Override
					public void write(byte[] b, int off, int len) throws IOException {
						socket.getOutputStream().write(b, off, len);
					}

					@Override
					public void flush() throws IOException {
						socket.getOutputStream().flush();
					}

					@Override
					public boolean isReady() {
						return false;
					}

					@Override
					public void setWriteListener(WriteListener writeListener) {
					}

				};
			}

			@Override
			public Locale getLocale() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getContentType() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getCharacterEncoding() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public int getBufferSize() {
				// Auto-generated method stub
				return 0;
			}

			@Override
			public void flushBuffer() throws IOException {
				// Auto-generated method stub

			}

			@Override
			public void setStatus(int sc, String sm) {
				// Auto-generated method stub

			}

			@Override
			public void setStatus(int sc) {
				// Auto-generated method stub

			}

			@Override
			public void setIntHeader(String name, int value) {
				// Auto-generated method stub

			}

			@Override
			public void setHeader(String name, String value) {
				// Auto-generated method stub

			}

			@Override
			public void setDateHeader(String name, long date) {
				// Auto-generated method stub

			}

			@Override
			public void sendRedirect(String location) throws IOException {
				// Auto-generated method stub

			}

			@Override
			public void sendError(int sc, String msg) throws IOException {
				// Auto-generated method stub

			}

			@Override
			public void sendError(int sc) throws IOException {
				// Auto-generated method stub

			}

			@Override
			public int getStatus() {
				// Auto-generated method stub
				return 0;
			}

			@Override
			public Collection<String> getHeaders(String name) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public Collection<String> getHeaderNames() {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String getHeader(String name) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String encodeUrl(String url) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String encodeURL(String url) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String encodeRedirectUrl(String url) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public String encodeRedirectURL(String url) {
				// Auto-generated method stub
				return null;
			}

			@Override
			public boolean containsHeader(String name) {
				// Auto-generated method stub
				return false;
			}

			@Override
			public void addIntHeader(String name, int value) {
				// Auto-generated method stub

			}

			@Override
			public void addHeader(String name, String value) {
				// Auto-generated method stub

			}

			@Override
			public void addDateHeader(String name, long date) {
				// Auto-generated method stub

			}

			@Override
			public void addCookie(Cookie cookie) {
				// Auto-generated method stub

			}

			@Override
			public void setContentLengthLong(long len) {

			}
		};
	}

}
