package cn.yugj.test;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author yugj
 * @date 2019/3/15 下午12:52.
 */
@Controller
public class OpController {

    @Autowired
    private SeverFeignClient client;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * post 2 server via feign hystrix ribbon HttpURLConnection
     * @return
     */
    @RequestMapping("/client/test")
    @ResponseBody
    public String test() {

        long s1 = System.currentTimeMillis();
        Req req = new Req();
        req.setHell("test");
        Resp hell = client.testPost(req);
        long s2 = System.currentTimeMillis();

        long cost = s2 - s1;
        System.out.println( " test1 cost = " +  cost);

        return "t1:" + cost;

    }

    /**
     * post 2 server via restTemplate
     * performance worst
     * @return
     */
    @RequestMapping("/client/test2")
    @ResponseBody
    public String test2() {

        Map<String,Object> param = Maps.newHashMap();
        param.put("hell", "test2");

        long s1 = System.currentTimeMillis();
        String hell = restTemplate.postForObject("http://localhost:9001/server/testPost",param,String.class,new Object());
        long s2 = System.currentTimeMillis();

        long cost = s2 - s1;
        System.out.println(hell + " test2 cost = " + cost);

        return "t2:" + cost;



    }

    /**
     * post 2 server via feign hystrix ribbon HttpURLConnection
     * @return
     */
    @RequestMapping("/client/test3")
    @ResponseBody
    public String test3() {

        long s1 = System.currentTimeMillis();
        Req req = new Req();
        req.setHell("test");

        String hell  = client.testGet();
        long s2 = System.currentTimeMillis();
        long cost = s2 - s1;
        System.out.println(hell + " test3 cost = " +  cost);

        return "t3:" + cost;

    }


    /**
     * post 2 server via feign hystrix ribbon HttpURLConnection
     * @return
     */
    @RequestMapping("/client/test4")
    @ResponseBody
    public String test4() {

        long s1 = System.currentTimeMillis();
        Req req = new Req();
        req.setHell("test");

        String hell  = client.testBothGet();

        long s2 = System.currentTimeMillis();
        long cost = s2 - s1;
        System.out.println(hell + " test4 cost = " +  cost);

        return "t4:" + cost;

    }

    /**
     * post 2 server via feign hystrix ribbon HttpURLConnection
     * @return
     */
    @RequestMapping("/client/test5")
    @ResponseBody
    public String test5() {

        long s1 = System.currentTimeMillis();
        Req req = new Req();
        req.setHell("test");

        String hell  = client.testBothPost();

        long s2 = System.currentTimeMillis();
        long cost = s2 - s1;
        System.out.println(hell + " test5 cost = " +  cost);

        return "t5:" + cost;

    }

    @RequestMapping("/client/test6")
    @ResponseBody
    public String test6() {

        long s1 = System.currentTimeMillis();
        client.testVoid();
        long s2 = System.currentTimeMillis();
        long cost = s2 - s1;
        System.out.println("test6 cost = " +  cost);

        return "t6:" + cost;

    }

}
