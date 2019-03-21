package cn.yugj.test.config;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Decoder is ok,not the delay reason
 * avg <1 ms, max 16ms
 * @author yugj
 * @date 2019/3/16 下午1:54.
 */
public class TestDecoder extends ResponseEntityDecoder {

    private static final Logger log = LoggerFactory.getLogger(TestDecoder.class);

    public TestDecoder(Decoder decoder) {
        super(decoder);
    }


    @Override
    public Object decode(final Response response, Type type) throws IOException,
            FeignException {

        long start = System.currentTimeMillis();
        Object rs = super.decode(response, type);

        long end = System.currentTimeMillis();

        log.info("decode:" + (end - start));

        return rs;

    }

}
