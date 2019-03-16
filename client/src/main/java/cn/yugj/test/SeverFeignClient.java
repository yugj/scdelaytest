package cn.yugj.test;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yugj
 * @date 2019/3/15 下午12:50.
 */
@Service
@FeignClient(name = "serverClient", url = "http://localhost:9001")
public interface SeverFeignClient {

    /**
     * post test
     * @return hell
     */
    @RequestMapping(value = "/server/testPost")
    Resp testPost(@RequestBody Req req);

    /**
     * get test
     * @return string
     */
    @RequestMapping(value = "/server/testGet",method = RequestMethod.GET)
    String testGet();


    /**
     * test same api with get method
     * @return
     */
    @RequestMapping(value = "/server/test",method = RequestMethod.GET)
    String testBothGet();

    /**
     * test same api with post method
     * @return
     */
    @RequestMapping(value = "/server/test",method = RequestMethod.POST)
    String testBothPost();

    /**
     * test void return type
     */
    @RequestMapping(value = "/server/testVoid")
    void testVoid();


}
