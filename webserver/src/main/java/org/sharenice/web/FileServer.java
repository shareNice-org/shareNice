package org.sharenice.web;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.resource.Resource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class FileServer {

    public static void main(String[] args) throws Exception {

        // The Jetty Server.
        Server server = new Server();

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

//        MimeTypes mimeTypes = resource_handler.getMimeTypes();
//        mimeTypes.addMimeMapping(, "text/html");
//        resource_handler.setMimeTypes(mimeTypes);
        FileHandler handler = new FileHandler("src/main/webapp");

        // Add the ResourceHandler to the server.
        GzipHandler gzip = new GzipHandler();
        server.setHandler(gzip);
        HandlerList handlers = new HandlerList();
//        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        handlers.setHandlers(new Handler[] { handler });
        gzip.setHandler(handlers);

        server.start();
        server.join();

    }





}
/* vi:set expandtab sts=2 sw=2: */
