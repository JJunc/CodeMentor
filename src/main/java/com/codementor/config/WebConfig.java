package com.codementor.config;

import com.codementor.intetceptor.LoginCheckInterceptor;
import com.codementor.post.enums.PostCategoryConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

//    @Value("${FILE_UPLOAD_DIR}")
    private String fileUploadDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns(
                        "/post/create",
                        "/post/edit/**",
                        "/post/delete/**",
                        "/comment/create",
                        "/comment/api/edit/**",
                        "/comment/api/delete/**",
                        "/my/**",
                        "/my"
                )
                .excludePathPatterns(
                        "/css/**", "/js/**", "/images/**", "/favicon.ico"
                );
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter((Converter<?, ?>) new PostCategoryConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + fileUploadDir + "/");
    }

}