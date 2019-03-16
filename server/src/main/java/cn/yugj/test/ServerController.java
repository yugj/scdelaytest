package cn.yugj.test;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yugj
 * @date 2019/3/15 下午12:39.
 */
@RestController
@RequestMapping("/server")
public class ServerController {

    /**
     * post only
     * @param req
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "/testPost")
    public Resp testPost(@RequestBody Req req,HttpServletRequest request) throws InterruptedException {



        long start = System.currentTimeMillis();

        long sleep = 10L;
        Thread.sleep(sleep);

        Resp resp = new Resp();
        resp.setHell("return");

        long end = System.currentTimeMillis();
        System.out.println("testPost cost :" + (end - start));
        return resp;
    }

    /**
     * get only
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "/testGet", method = RequestMethod.GET)
    public String testGet() throws InterruptedException {

        long start = System.currentTimeMillis();
        long sleep = 10L;
        Thread.sleep(sleep);

        long end = System.currentTimeMillis();
        long cost = end - start;

        System.out.println("testGet cost :" + cost);
        return "testGet cost:" + cost;
    }

    /**
     * post and get
     * @param request
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/test")
    public String test(HttpServletRequest request) throws InterruptedException {
        String method = request.getMethod();
        System.out.println(method);

        long start = System.currentTimeMillis();

        long sleep = 10L;
        Thread.sleep(sleep);
        long end = System.currentTimeMillis();

        long cost = end - start;

        System.out.println("test cost :" + cost);
        return "test get or post cost:" + cost;
    }

    @RequestMapping("/testVoid")
    public void testVoid() throws InterruptedException {
        long start = System.currentTimeMillis();

        long sleep = 10L;
        Thread.sleep(sleep);
        long end = System.currentTimeMillis();
        long cost = end - start;
        System.out.println("void cost :" + cost);
    }
}
