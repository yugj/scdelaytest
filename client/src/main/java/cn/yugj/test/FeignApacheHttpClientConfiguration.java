package cn.yugj.test;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * feign http client configuration
 *
 * @author yugj
 * @date 2019/3/14 上午11:09.
 */
@Configuration
@ConditionalOnProperty(value = "feign.httpclient.enabled", havingValue = "true", matchIfMissing = false)
public class FeignApacheHttpClientConfiguration {

    @Value("${ribbon.MaxConnectionsPerHost}")
    private Integer maxConnectionPerHost;
    @Value("${ribbon.MaxTotalConnections}")
    private Integer maxTotalConnections;

    @Value("${ribbon.ReadTimeout}")
    private Integer readTimeout;
    @Value("${ribbon.ConnectTimeout}")
    private Integer connectTimeout;

    private static final int TIMER_DELAY_SECOND = 20;
    private static final int CLOSE_EXPIRE_PERIOD_SECOND = 5;
    private static final int CLOSE_IDOL_PERIOD_SECOND = 30;
    private static final int TIME_TOP_LIVE_SECOND = 30;

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2,
            new BasicThreadFactory.Builder().namingPattern("apache-connection-manager-timer-%d").daemon(true).build());

    /**
     * 连接池管理配置
     * 替换HttpClientFeignLoadBalancedConfiguration HttpClientConnectionManager 默认配置
     *
     * @return manager
     */
    @Bean
    public HttpClientConnectionManager httpClientConnectionManager() {

        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(TIME_TOP_LIVE_SECOND, TimeUnit.SECONDS);

        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionPerHost);

        //释放expired conn
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                connectionManager.closeExpiredConnections();
            }
        }, TIMER_DELAY_SECOND, CLOSE_EXPIRE_PERIOD_SECOND, TimeUnit.SECONDS);

        //释放idle conn
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                connectionManager.closeIdleConnections(CLOSE_EXPIRE_PERIOD_SECOND, TimeUnit.SECONDS);
            }
        }, TIMER_DELAY_SECOND, CLOSE_IDOL_PERIOD_SECOND, TimeUnit.SECONDS);


        return connectionManager;

    }

    /**
     * http client bean
     *
     * @param httpClientConnectionManager connectionManager
     * @return client
     */
    @Bean
    public CloseableHttpClient httpClient(HttpClientConnectionManager httpClientConnectionManager) {

        HttpClientBuilder builder = HttpClientBuilder.create().disableCookieManagement().useSystemProperties();

        //默认请求配置 一般不需配置，对应client在包装使用时会重写对应值
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(readTimeout)
                .build();

        CloseableHttpClient httpClient = builder.
                setDefaultRequestConfig(defaultRequestConfig).
                setConnectionManager(httpClientConnectionManager).
                setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).build();

        return httpClient;

    }
}
