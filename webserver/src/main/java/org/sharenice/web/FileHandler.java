package org.sharenice.web;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.resource.Resource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by mischat on 05/11/2015.
 */
public class FileHandler extends AbstractHandler {
    private final MimeTypes mimeTypes = new MimeTypes();

    private Cache<String, byte[]> cache = CacheBuilder.newBuilder().maximumSize(1000).build();

    public FileHandler( String resource ) throws IOException {
        File dir = new File(resource);
        List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file : files) {
            String relative = new File(resource).toURI().relativize(file.toURI()).getPath();
            System.out.println("file: " + file.getCanonicalPath() + " resource = "+ resource + " file" + file.getPath() + " relativose " + relative);
            byte[] encoded = Files.readAllBytes(Paths.get(file.getCanonicalPath()));
            cache.put("/"+relative, encoded);
        }
    }

    @Override
    public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
        byte[] fileContents = cache.getIfPresent(target);

        if (fileContents == null || fileContents.length < 1) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String extension = FilenameUtils.getExtension(target);

        if (Strings.isNullOrEmpty(extension)) {
            response.setContentType("text/html");
        } else {
            response.setContentType(mimeTypes.getMimeByExtension("."+extension));
        }

        response.setContentLength(fileContents.length);

        baseRequest.setHandled(true);
        response.getOutputStream().write(fileContents);
        response.getOutputStream().flush();

    }
}
