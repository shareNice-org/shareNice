package org.sharenice.web;

import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

public class FileServer {

    public static void main(String[] args) throws Exception {

        Server server = new Server();



        // HTTP Configuration
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        http_config.setSendXPoweredBy(true);
        http_config.setSendServerVersion(true);

        // HTTP Connector
        ServerConnector http = new ServerConnector(server,new HttpConnectionFactory(http_config), new HTTP2CServerConnectionFactory(http_config));
        http.setPort(8080);
        server.addConnector(http);

        // SSL Context Factory for HTTPS and HTTP/2
//        String jetty_distro = System.getProperty("jetty.distro","../../jetty-distribution/target/distribution");
        SslContextFactory sslContextFactory = new SslContextFactory(true);
        sslContextFactory.setKeyStorePath("/tmp/keystore/keystore.jks");
        sslContextFactory.setKeyStorePassword("localhost");
        sslContextFactory.setKeyManagerPassword("localhost");
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

        // HTTPS Configuration
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(https_config);

        NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol(http.getDefaultProtocol());

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory,alpn.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector =
                new ServerConnector(server,ssl,alpn,h2,new HttpConnectionFactory(https_config));
        http2Connector.setPort(8443);
        server.addConnector(http2Connector);

        FileHandler handler = new FileHandler("src/main/webapp");

    //    GzipHandler gzip = new GzipHandler();
      //  server.setHandler(gzip);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { handler });
        server.setHandler(handlers);

        ALPN.debug=true;

        server.start();
        server.dumpStdErr();
        server.join();



/**
        QueuedThreadPool threadPool = new QueuedThreadPool(100);

        // The Jetty Server.
        Server server = new Server(threadPool);

        server.addBean(new ScheduledExecutorScheduler());
        // Common HTTP configuration.
        HttpConfiguration config = new HttpConfiguration();

        // HTTP/1.1 support.
        HttpConnectionFactory http1 = new HttpConnectionFactory(config);

        // HTTP/2 cleartext support.
        HTTP2CServerConnectionFactory http2c = new HTTP2CServerConnectionFactory(config);

        ServerConnector connector = new ServerConnector(server, http1, http2c);
        connector.setPort(8080);
        server.addConnector(connector);

        // Here configure contexts / servlets / etc.

        FileHandler handler = new FileHandler("src/main/webapp");

        GzipHandler gzip = new GzipHandler();
        server.setHandler(gzip);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { handler });
        gzip.setHandler(handlers);

        server.start();
        server.dumpStdErr();
        server.join();
*/
    }

}
/* vi:set expandtab sts=4 sw=4: */
