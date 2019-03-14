package com.dongnaoedu.tony.test;

import com.dongnaoedu.tony.JwtTokenProvider;
import com.dongnaoedu.tony.UserClaims;

import io.jsonwebtoken.Claims;

/**
 * JWT-Token生成
 * 
 * @author Tony
 *
 */
public class JWTtests {
	public static void main(String[] args) {
		// md5("7981798723412342134"); > 签名

		// 密钥 12345678
		JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("12345678");
		UserClaims claims = new UserClaims();
		claims.setUserName("Tony");
		claims.setEmail("tony@qq.com");
		// token有效期
		
		String token = jwtTokenProvider.createToken(claims);
		System.out.println("生成的token：" + token);
		// 生成Token
		Claims userClaims = jwtTokenProvider.parseToken(token);
		System.out.println("解析出来的Toekn内容：" + userClaims);
	}
}
