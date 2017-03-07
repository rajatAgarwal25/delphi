package com.proptiger.delphi.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class DelphiInitializer implements WebApplicationInitializer {

    private static final String CONFIG_LOCATION = "com.proptiger.delphi.config";
    private static final String MAPPING_URL     = "/*";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext context = getContext();
        servletContext.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(
                context));
        dispatcher.setMultipartConfig(getMultipartConfigElement());
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(MAPPING_URL);
        dispatcher.setAsyncSupported(true);
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        return context;
    }

    private MultipartConfigElement getMultipartConfigElement() {
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                LOCATION,
                MAX_FILE_SIZE,
                MAX_REQUEST_SIZE,
                FILE_SIZE_THRESHOLD);
        return multipartConfigElement;
    }

    private static final String LOCATION            = "/temp/"; // Temporary
                                                                // location
                                                                // where files
                                                                // will be
                                                                // stored

    private static final long   MAX_FILE_SIZE       = 5242880; // 5MB : Max
                                                                // file
                                                                // size.
                                                                // Beyond that
                                                                // size
                                                                // spring will
                                                                // throw
                                                                // exception.
    private static final long   MAX_REQUEST_SIZE    = 20971520; // 20MB : Total
                                                                // request size
                                                                // containing
                                                                // Multi
                                                                // part.

    private static final int    FILE_SIZE_THRESHOLD = 0;       // Size
                                                                // threshold
                                                                // after
                                                                // which files
                                                                // will be
                                                                // written to
                                                                // disk

}
