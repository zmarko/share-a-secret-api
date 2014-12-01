/*
 * The MIT License
 *
 * Copyright 2014 Marko Zivanovic.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rs.in.zivanovic.share.a.secret.api;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

    @Bean
    public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
        return (ConfigurableEmbeddedServletContainer servletContainer) -> {
            ((TomcatEmbeddedServletContainerFactory) servletContainer).addConnectorCustomizers(
                    (TomcatConnectorCustomizer) (Connector connector) -> {
                        AbstractHttp11Protocol httpProtocol = (AbstractHttp11Protocol) connector.getProtocolHandler();
                        httpProtocol.setCompression("on");
                        httpProtocol.setCompressionMinSize(256);
                        String mimeTypes = httpProtocol.getCompressableMimeTypes();
                        String mimeTypesWithJson = mimeTypes + "," + MediaType.APPLICATION_JSON_VALUE;
                        httpProtocol.setCompressableMimeTypes(mimeTypesWithJson);
                    });
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
