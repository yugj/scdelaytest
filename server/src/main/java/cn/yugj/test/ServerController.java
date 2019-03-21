package cn.yugj.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ServerController.class);

    /**
     * post only
     * @param req
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "/testPost")
    public Resp testPost(@RequestBody Req req,HttpServletRequest request) throws InterruptedException {

        log.info("req");

        long start = System.currentTimeMillis();

        long sleep = 10L;
        Thread.sleep(sleep);

        Resp resp = new Resp();
        resp.setHell("return");

        long end = System.currentTimeMillis();
        log.info("testPost cost :" + (end - start));
        return resp;
    }

    /**
     * get only
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "/testGet", method = RequestMethod.GET)
    public String testGet() throws InterruptedException {

        log.info("req");

        long start = System.currentTimeMillis();
        long sleep = 10L;
        Thread.sleep(sleep);

        long end = System.currentTimeMillis();
        long cost = end - start;

        log.info("testGet cost :" + cost);
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

        log.info("req");

        String method = request.getMethod();
        log.info(method);

        long start = System.currentTimeMillis();

        long sleep = 10L;
        Thread.sleep(sleep);
        long end = System.currentTimeMillis();

        long cost = end - start;

        log.info("test cost :" + cost);
        return "test get or post cost:" + cost;
    }

    @RequestMapping("/testVoid")
    public void testVoid() throws InterruptedException {
        log.info("req");

        long start = System.currentTimeMillis();

        long sleep = 10L;
        Thread.sleep(sleep);
        long end = System.currentTimeMillis();
        long cost = end - start;
        log.info("void cost :" + cost);
    }

    @RequestMapping("/doNothing")
    public String doNothing() {
        log.info("do noting");
        return "do nothing";
    }
}
