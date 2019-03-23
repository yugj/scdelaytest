package cn.yugj.test.config;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * http client configuration
 *
 * @author yugj
 * @date 2019/3/14 上午11:09.
 */
@Configuration
@ConditionalOnProperty(value = "sys.httpclient.enabled", havingValue = "true", matchIfMissing = false)
public class BasicApacheHttpClientConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BasicApacheHttpClientConfiguration.class);

    @Value("${sys.httpclient.MaxConnectionsPerHost:1000}")
    private Integer maxConnectionPerHost;
    @Value("${sys.httpclient.MaxTotalConnections:6000}")
    private Integer maxTotalConnections;

    @Value("${sys.httpclient.ReadTimeout:10000}")
    private Integer readTimeout;
    @Value("${sys.httpclient.ConnectTimeout:3000}")
    private Integer connectTimeout;

    private static final int TIMER_DELAY_SECOND = 20;
    private static final int CLOSE_EXPIRE_PERIOD_SECOND = 20;
    private static final int CLOSE_IDOL_PERIOD_SECOND = 40;
    private static final int TIME_TO_LIVE_SECOND = 40;
    private static final int EXECUTOR_CORE_SIZE = 3;

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(EXECUTOR_CORE_SIZE,
            new BasicThreadFactory.Builder().namingPattern("apache-connection-manager-timer-%d").daemon(true).build());

    /**
     * 连接池管理配置
     * 替换HttpClientFeignLoadBalancedConfiguration HttpClientConnectionManager 默认配置
     *
     * @return manager
     */
    @Bean
    public HttpClientConnectionManager httpClientConnectionManager() {

        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(TIME_TO_LIVE_SECOND, TimeUnit.SECONDS);

        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionPerHost);

        //release connections
        scheduledExecutorService.scheduleAtFixedRate(() -> {

            log.info("apache connection stats: {}", connectionManager.getTotalStats());
            connectionManager.closeExpiredConnections();
            connectionManager.closeIdleConnections(CLOSE_IDOL_PERIOD_SECOND, TimeUnit.SECONDS);
        }, TIMER_DELAY_SECOND, CLOSE_EXPIRE_PERIOD_SECOND, TimeUnit.SECONDS);

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
