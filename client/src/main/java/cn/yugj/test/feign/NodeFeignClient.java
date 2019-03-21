package cn.yugj.test.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yugj
 * @date 2019/3/21 上午11:46.
 */
@Service
@FeignClient(name = "nodeClient", url = "http://localhost:3000")
public interface NodeFeignClient {

    /**
     * test do noting
     * @return
     */
    @RequestMapping("/")
    String doNothing();

}
