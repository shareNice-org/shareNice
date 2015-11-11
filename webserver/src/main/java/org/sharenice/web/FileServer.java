package org.sharenice.web;

import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

public class FileServer {

    public static void main(String[] args) throws Exception {

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

//        server.setHandler(new FileHandler("src/main/webapp"));

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

    }

}
/* vi:set expandtab sts=2 sw=2: */
