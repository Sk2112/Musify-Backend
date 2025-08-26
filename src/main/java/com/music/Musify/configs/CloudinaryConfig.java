package com.music.Musify.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {


    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzzmthiif",
                "api_key", "861171651553514",
                "api_secret", "W8wB9ic-Ffqg0jsKHK0YwMpmP4A"));
    }
}


