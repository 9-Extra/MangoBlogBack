package cn.mango.mangoblog.demo;

import cn.mango.mangoblog.application.DemoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest(classes = DemoApplication.class)
class DemoApplicationTests {

	@Test
	@Rollback
	void createUser() {
		//User user = new User("Amy");
	}

}
