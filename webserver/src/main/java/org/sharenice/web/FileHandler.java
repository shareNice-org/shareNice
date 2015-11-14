package org.sharenice.web;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.Hashing;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.sharenice.web.model.WebResourceCache;

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

    private Cache<String, WebResourceCache> cache = CacheBuilder.newBuilder().maximumSize(1000).build();

    public FileHandler( String resource ) throws IOException {
        File dir = new File(resource);
        List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file : files) {
            String relative = new File(resource).toURI().relativize(file.toURI()).getPath();
            System.out.println("file: " + file.getCanonicalPath() + " resource = "+ resource + " file" + file.getPath() + " relativose " + relative);
            byte[] encoded = Files.readAllBytes(Paths.get(file.getCanonicalPath()));
            cache.put("/"+relative, new WebResourceCache(encoded, Hashing.sha1().hashBytes(encoded).toString()));
        }
    }

    @Override
    public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {

        if (((Request) request).getHttpURI().getHost().startsWith("www.")) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", ((Request) request).getRootURL().toString().replace("www.","")+target);
            return;
        }

        if (target.equals("/")) {
            target = "/index";
        }

        WebResourceCache webResourceCache = cache.getIfPresent(target);

        if (webResourceCache == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String extension = FilenameUtils.getExtension(target);

        if (Strings.isNullOrEmpty(extension)) {
            response.setContentType("text/html; charset=utf-8");
        } else {
            response.setContentType(mimeTypes.getMimeByExtension("."+extension));
        }

        response.setHeader("ETag","\""+ webResourceCache.getEtag()+"\"");
        response.setHeader("Cache-Control", "public, max-age=30");
        response.setHeader("X-Clacks-Overhead", "GNU Terry Pratchett");
        response.setContentLength(webResourceCache.getContent().length);

        baseRequest.setHandled(true);
        response.getOutputStream().write(webResourceCache.getContent());
        response.getOutputStream().flush();
    }
}
