package com.diy.app;

import com.diy.framework.web.HandlerMapping;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.mvc.view.ViewResolver;
import com.diy.framework.web.mvc.view.ViewResolverRegistry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class AppInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BeanFactory beanFactory = new BeanFactory("com.diy.app");

        HandlerMapping mapping = new HandlerMapping();
        mapping.put("/lectures", (LectureController) beanFactory.getBean(LectureController.class));

        ViewResolverRegistry viewResolvers = new ViewResolverRegistry(
                List.of(
                        new ViewResolver("/", ".jsp"),
                        new ViewResolver("/", ".html")
                ));

        sce.getServletContext().setAttribute("handlerMapping", mapping);
        sce.getServletContext().setAttribute("viewResolvers", viewResolvers);
    }
}
