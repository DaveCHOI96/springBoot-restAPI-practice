plugins {
	java
	id("org.springframework.boot") version "3.5.13"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	// 1. Redis 연동을 위한 핵심 라이브러리 (이게 있어야 RedisCacheConfiguration이 포함됩니다)
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	// 2. 스프링의 캐시 추상화 기능을 쓰기 위한 라이브러리
	implementation ("org.springframework.boot:spring-boot-starter-cache")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
