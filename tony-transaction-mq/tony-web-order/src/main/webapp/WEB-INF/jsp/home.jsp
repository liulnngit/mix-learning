<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<title>JAVA应用系统监控</title>
<script type="text/javascript" src="resources/jquery-3.2.1.js"></script>
<script type="text/javascript">
	var counter = 0;
	var time = 0;
	var t = null;
	function update(){
		time = time + 1;
		$("#test").html("请求中，耗时："+time+"秒");
	}
	$(function() {
		//获取token
		$("#test").click(function() {
			counter = counter + 1;
			// 请求开始，锁定按钮
			$("#test").html("请求中，耗时："+time+"秒");
			$("#test").attr({
				"disabled" : "disabled"
			});
			t = self.setInterval("update()",1000)
			
			
			$.ajax({
				type : "get",
				url : "user/getUserInfo?userId=tony",
				complete : function(XMLHttpRequest, textStatus) {
					if(counter == 10) {
						$("#content").html("");
						counter = 0;
					}
					$("#content").append("<br/>" + XMLHttpRequest.responseText);

					// 松开按钮
					$("#test").html("点击测试");
					$("#test").removeAttr("disabled");//将按钮可用
					time = 0;
					window.clearInterval(t);
					t = null;
					
				}
			});
		});
	});
</script>
</head>
<body>
	<h3>JAVA应用系统监控</h3>
	<h3>模拟 - 调用获取用户的接口</h3>
	<button id="test">点击测试</button>
	<br />
	<div id="content"></div>
</body>
</html>