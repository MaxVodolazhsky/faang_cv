package org.faang.cv.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CvBotRunner {

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(CvBotRunner.class);
    }
}