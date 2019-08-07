package com.xpq.cs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xpq.cs.util.MailUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CsApplicationTests {

	@Test
	public void contextLoads() {
		 boolean isSend = MailUtil.sendEmail("这是一封测试邮件", new String[]{"pq_xie@163.com","xie_pq@163.com"}, null, "<h3><a href='http://www.baidu.com'>百度一下，你就知道</a></h3>", null);
		 System.out.println(isSend);
	}

}
