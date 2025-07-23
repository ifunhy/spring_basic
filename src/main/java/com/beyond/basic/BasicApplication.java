package com.beyond.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

// ComponentScan은 Application파일을 포함한 경로 하위의 요소들만 scan 가능
@SpringBootApplication
// 주로 web서블릿 기반의 구성요소(@WebServlet)를 스캔, 자동으로 빈(싱글톤객체)으로 등록
@ServletComponentScan
public class BasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicApplication.class, args);
	}

}
