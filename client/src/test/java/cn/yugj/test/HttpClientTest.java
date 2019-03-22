package cn.yugj.test;

import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @author yugj
 * @date 2019/3/21 下午5:50.
 */
public class HttpClientTest {

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 100; i++) {
            HttpClients.createDefault().close();
        }

    }
}
