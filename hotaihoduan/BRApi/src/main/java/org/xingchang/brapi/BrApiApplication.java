package org.xingchang.brapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrApiApplication.class, args);
		System.out.println("\n========================================");
		System.out.println("BRApi 启动成功！");
		System.out.println("访问地址：http://localhost:8080/api");
		System.out.println("测试接口：http://localhost:8080/api/test/hello");
		System.out.println("Swagger文档：http://localhost:8080/api/swagger-ui/index.html");
		System.out.println("登录接口：POST http://localhost:8080/api/user/login");
		System.out.println("========================================\n");
	}

}
