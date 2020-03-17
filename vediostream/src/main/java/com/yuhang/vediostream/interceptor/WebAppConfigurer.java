package com.yuhang.vediostream.interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器解决全局跨域问题
 *
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer{


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("*")
		.allowCredentials(true)
		.allowedMethods("GET", "POST", "DELETE", "PUT")
		.maxAge(3600 * 24);
	}
}
