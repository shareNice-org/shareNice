package org.sharenice.web;

import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.HttpOutput;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.resource.Resource;

import javax.activation.MimeType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * Created by mischat on 05/11/2015.
 */
public class FileHandler extends AbstractHandler {
    private final MimeTypes mimeTypes = new MimeTypes();
    private final Resource baseResource;

    public FileHandler( String resource ) throws MalformedURLException {
        this.baseResource = Resource.newResource(resource);
    }

    @Override
    public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
        Resource requestedResource = getResource(request);

        final File file = requestedResource.getFile();

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String extension = FilenameUtils.getExtension(target);

        if (Strings.isNullOrEmpty(extension)) {
            response.setContentType("text/html");
        } else {
            response.setContentType(mimeTypes.getMimeByExtension("."+extension));
        }

        response.setContentLength((int) requestedResource.getFile().length());
        response.setDateHeader("Last-Modified", requestedResource.lastModified());

        ((HttpOutput) response.getOutputStream()).sendContent(FileChannel.open(file.toPath(), StandardOpenOption.READ));
    }

    protected Resource getResource(String path) throws IOException {
        if(path != null && path.startsWith("/")) {
            Resource base = this.baseResource;
            if(base == null) {
                throw new MalformedURLException(path);
            } else {
                try {
                    path = URIUtil.canonicalPath(path);
                    Resource e = base.addPath(path);
                    return e;
                } catch (IOException ex) {
                    throw ex;
                }
            }
        } else {
            throw new MalformedURLException(path);
        }
    }

    protected Resource getResource(HttpServletRequest request) throws IOException {
        Boolean included = Boolean.valueOf(request.getAttribute("javax.servlet.include.request_uri") != null);
        String servletPath;
        String pathInfo;
        if(included != null && included.booleanValue()) {
            servletPath = (String)request.getAttribute("javax.servlet.include.servlet_path");
            pathInfo = (String)request.getAttribute("javax.servlet.include.path_info");
            if(servletPath == null && pathInfo == null) {
                servletPath = request.getServletPath();
                pathInfo = request.getPathInfo();
            }
        } else {
            servletPath = request.getServletPath();
            pathInfo = request.getPathInfo();
        }

        String pathInContext = URIUtil.addPaths(servletPath, pathInfo);
        return this.getResource(pathInContext);
    }
}
