package org.sharenice.web;

import org.eclipse.jetty.alpn.ALPN;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class FileServer {

    private final static int HTTP_PORT = 8080;
    private final static int HTTPS_PORT = 8443;

    public static void main(String[] args) throws Exception {
        QueuedThreadPool threadPool = new QueuedThreadPool(100);

        // The Jetty Server.
        Server server = new Server(threadPool);

        // Custome FileHandler
        FileHandler handler = new FileHandler("src/main/website");
        server.setHandler(handler);

        // HTTP Configuration
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(HTTPS_PORT);
        http_config.setSendXPoweredBy(true);
        http_config.setSendServerVersion(true);

        // HTTPS Configuration
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer(true));

        // HTTP Connector
        ServerConnector http = new ServerConnector(server,new HttpConnectionFactory(http_config));
        http.setPort(HTTP_PORT);
        server.addConnector(http);

        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath("/tmp/keystore/keystore.jks");
        sslContextFactory.setKeyStorePassword("storepwd");
        sslContextFactory.setTrustStorePath("/tmp/keystore/truststore.jks");
        sslContextFactory.setTrustStorePassword("storepwd");
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);
        sslContextFactory.setUseCipherSuitesOrder(true);
        sslContextFactory.setExcludeCipherSuites(
                "SSL_RSA_WITH_DES_CBC_SHA",
                "SSL_DHE_RSA_WITH_DES_CBC_SHA",
                "SSL_DHE_DSS_WITH_DES_CBC_SHA",
                "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
                "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
                "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
                "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(https_config);

        NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory("h2","h2-17","h2-16","h2-15","h2-14","http/1.1");
        alpn.setDefaultProtocol(http.getDefaultProtocol());

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory,alpn.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector =
                new ServerConnector(server,ssl,alpn,h2,new HttpConnectionFactory(https_config));
        http2Connector.setPort(HTTPS_PORT);
        server.addConnector(http2Connector);

        ALPN.debug=true;

        server.start();
        server.setDumpAfterStart(true);
        server.dumpStdErr();
        server.join();

    }

}
/* vi:set expandtab sts=4 sw=4: */
