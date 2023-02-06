package com.matrix.photogram.cofig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer { // web 설정 파일
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			WebMvcConfigurer.super.addResourceHandlers(registry);
			
//			file:///D:/workspace/springboot/upload/ -> application.yml파일에서 file: path: 에 설정한 경로 
			registry
			.addResourceHandler("/upload/**") // jsp페이지에서 /upload/** 이런 주소 패턴이 나오면 발동
			.addResourceLocations("file:///" + uploadFolder) // yml파일에 설정해 놓은 경로
			.setCachePeriod(60*10*6) // 60초*10=10분*6=1시간 / 1시간 동안 이미지 캐싱
			.resourceChain(true)
			.addResolver(new PathResourceResolver());
		}

}
