//package com.jwt.config;
//
//import org.apache.ignite.configuration.CacheConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
//import com.cache.common.GlobalCacheConstant;
//import com.cache.config.CacheService;
//
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//
//@Configuration
//@ComponentScan(basePackages = { "com.jwt", "com.cache" })
//@Slf4j
//public class ApplicationConfig {
//
//	@Autowired
//	private CacheService cacheService;
//
//	@PostConstruct
//	public void createCache() {
//		log.info("Creating cache for JWT Token");
//		CacheConfiguration<String, String> cacheConfig = new CacheConfiguration<>();
//		cacheConfig.setName(GlobalCacheConstant.JWT_TOKEN_CACHE);
//		cacheService.addConfigs(cacheConfig);
//		log.info("Cache created successfully for JWT Token");
//	}
//}
