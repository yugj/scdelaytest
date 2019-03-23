package cn.yugj.test;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yugj
 * @date 2019/3/16 上午9:35.
 */
public class MultiThreadTest {


    private static final RestTemplate client = new RestTemplate();
    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(300);

    public static void main(String[] args) throws InterruptedException {

        /**
         * http://localhost:9002/client/test
         * feign post pojo and return pojo
         *
         * http://localhost:9002/client/test2
         * resttemplate post pojo and return pojo
         *
         * http://localhost:9002/client/test3
         * feign get no param and return string
         *
         * http://localhost:9002/client/test4
         * feign get no param,same server api return String
         * http://localhost:9002/client/test5
         * feign post no param,same server api return String
         *
         * http://localhost:9002/client/test6
         * feign post no param ,void return
         */
        String url = "http://localhost:9002/client/test";

        int threads = 200;
        int round = 1;
        int index = 0;

        AtomicInteger summaryCost = new AtomicInteger();

        System.out.println("-------------->>");

        while (index < round) {

            for (int i = 0; i < threads; i++) {

                fixedThreadPool.submit(() -> {

                    String resp = client.getForObject(url, String.class, new Object());

                    String[] costResp = resp.split(":");
                    int cost = Integer.valueOf(costResp[1]);
                    summaryCost.addAndGet(cost);
                    System.out.println(cost);
                });
            }

            index ++;
            Thread.sleep(1000L);
        }

        Thread.sleep(2000L);
        System.out.println("avg:" + (summaryCost.intValue() / (round * threads)));
        System.out.println("<<--------------");
        fixedThreadPool.shutdown();

    }


}
