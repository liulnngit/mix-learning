<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<title>JAVA秒杀系统</title>
<script type="text/javascript" src="resources/jquery-3.2.1.js"></script>
<script type="text/javascript">
	$(function() {
		//获取token
		$("#test").click(function() {
// // 前端控制访问频率
// // 1. disabled 禁用按钮
// $("#test").attr('disabled', true);

// // 2. 5秒后自动启用
// setTimeout(function() {
// 	$("#test").removeAttr("disabled");
// }, 5000);

			$.ajax({
				type : "get",
				url : "miaosha?userId=tony&goodsCode=bike",
				complete : function(XMLHttpRequest, textStatus) {
					if ('true' == XMLHttpRequest.responseText) {
						alert("秒杀成功");
					} else {
						alert("#######################################秒杀失败");
					}
				}
			});
		});
	});
</script>
</head>
<body>
	<center>
		<h1>当前服务器端口：<%=request.getLocalAddr() + ":" + request.getLocalPort()%></h1>
		<h1><button id="test" style="width:100px;height:50px;font-size:30px">抢购</button></h1>
		<img src="resources/miaosha.jpg" width="1100px" />
	</center>
</body>
</html>