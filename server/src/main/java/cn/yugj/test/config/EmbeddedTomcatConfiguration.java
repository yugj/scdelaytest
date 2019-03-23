package cn.yugj.test.config;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * affected by operate system
 * sysctl config
 * @author yugj
 * @date 2019/3/23 上午9:13.
 */
@Configuration
public class EmbeddedTomcatConfiguration {

    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();

        factory.addConnectorCustomizers(connector ->
                ((AbstractHttp11Protocol) connector.getProtocolHandler()).setMaxKeepAliveRequests(1000));

        factory.addConnectorCustomizers(connector ->
                ((AbstractHttp11Protocol) connector.getProtocolHandler()).setKeepAliveTimeout(60000));

        return factory;
    }
}
