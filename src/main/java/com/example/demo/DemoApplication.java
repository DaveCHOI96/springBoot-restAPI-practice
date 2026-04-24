package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

//@EnableCaching // 캐시 기능을 켜는 어노테이션 / CacheService로 직접 Redis 다루고 있어 필요없음
@EnableJpaAuditing // 해당 어노테이션이 있어야 @CreatedDate가 작동
@SpringBootApplication
//훨씬 더 정돈된 표준 규격(Spring Data의 PagedModel)으로 변환
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
