package cn.yugj.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author yugj
 * @date 2019/3/15 下午12:43.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServerStart {

    public static void main(String[] args) {
        SpringApplication.run(ServerStart.class, args);
    }


}
