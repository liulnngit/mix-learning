package com.dongnaoedu.tony;

import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;

@RestController
@CrossOrigin(origins="http://www.tony.com") //跨域
public class UserController {

	/**生成Token的工具*/
	private static JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("12345678");
	
	/**
	 * 获取token接口，返回token
	 */
	@RequestMapping("getToken")
	@ResponseBody
	public Object getToken(String userCode, String password) {
		// TODO 忽略校验用户名和密码
		if("tony".equals(userCode) || "123".equals(password)) {
			// 用户信息查出来，然后生成token字符串
			UserClaims userClaims = new UserClaims(); // 存在token字符串里面，可以被反解析
			userClaims.setUserName("tony");
			userClaims.setEmail("tony.com");
			userClaims.setId(UUID.randomUUID().toString()); // 每次生成新的Token
			// token保存 有效期，其他自定义的内容
			String tokenString = jwtTokenProvider.createToken(userClaims);
			
			return tokenString;
		}
		
		return "xxxxoooo";
	}
	
	//TODO 刷新token接口
	
	/**
	 * 获取用户相关的数据，并解析token
	 * 
	 * @return 返回结果
	 */
	@ResponseBody
	@RequestMapping("listData")
	public Object list(@RequestHeader("Authorization") String token) {
		try {
			// 验证一下token的正确性
			Claims claims = jwtTokenProvider.parseToken(token);
			// 解析出Toekn里面数据，成功解析就代表正确
			// 2. 检查token存储的其他信息， 比如：校验有效期
			System.out.println("用户请求了接口，用户名为："+claims.get("userName").toString());
			
			// TODO 忽略相关的调用相关的接口或者service进行数据查询
			// TODO xxService.dosomething()
			return "you can !" + System.currentTimeMillis();

		} catch (Exception e) {
			e.printStackTrace();
			return "error:" + e.getMessage();
		}
	}
}
