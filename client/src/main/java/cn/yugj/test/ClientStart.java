package cn.yugj.test;

import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * @author yugj
 * @date 2019/3/15 下午12:42.
 */
@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableHystrix
@EnableAsync
@EnableAspectJAutoProxy
public class ClientStart {

    @Autowired
    private RestTemplateBuilder builder;
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }


    public static void main(String[] args) {
        SpringApplication.run(ClientStart.class, args);
    }

    /**
     * just extends the default Decoder,do some logs
     * @return
     */
    @Bean
    public Decoder feignDecoder() {
        return new TestDecoder(new SpringDecoder(this.messageConverters));
    }

}