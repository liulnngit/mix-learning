package com.dongnaoedu.tony.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.tony.service.TicketService;

@RestController
@RequestMapping("/ticket")
public class TicketController {

	@Autowired
	TicketService ticketService;

	// 查询某一车次余票
	@RequestMapping("/queryStock")
	public Object getUserInfo(String ticketSeq) throws Exception {
		// 调用service方法获取
		return ticketService.queryTicketStock(ticketSeq);
	}
}
