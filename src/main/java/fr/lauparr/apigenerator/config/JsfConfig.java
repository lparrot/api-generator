package fr.lauparr.apigenerator.config;

import fr.lauparr.apigenerator.listeners.CustomSessionListener;
import org.primefaces.webapp.filter.FileUploadFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JsfConfig {

	@Autowired private CustomSessionListener sessionListener;

	@Bean
	@RequestScope
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@Bean
	public ServletRegistrationBean<FacesServlet> facesServletRegistration() {
		final ServletRegistrationBean<FacesServlet> registration = new ServletRegistrationBean<>(new FacesServlet(), "*.xhtml");
		registration.setName("Faces Servlet");
		registration.setLoadOnStartup(1);

		return registration;
	}

	@Bean
	public FilterRegistrationBean<FileUploadFilter> facesUploadFilterRegistration(Environment environment) {
		final Map<String, String> param = new HashMap<>();
		param.put("thresholdSize", "102400000");
		param.put("uploadDirectory", environment.getProperty("java.io.tmpdir"));

		final FilterRegistrationBean<FileUploadFilter> registrationBean = new FilterRegistrationBean<>(new FileUploadFilter(), this.facesServletRegistration());
		registrationBean.setName("PrimeFaces FileUpload Filter");
		registrationBean.setInitParameters(param);
		registrationBean.addUrlPatterns("/*");
		registrationBean.setDispatcherTypes(DispatcherType.FORWARD, DispatcherType.REQUEST);

		return registrationBean;
	}

	@Bean
	public ServletContextInitializer servletContextInitializer() {
		return servletContext -> {
			servletContext.createListener(ContextLoaderListener.class);
			servletContext.createListener(RequestContextListener.class);
			servletContext.createListener(HttpSessionEventPublisher.class);
			servletContext.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain")).addMappingForUrlPatterns(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST), false, "/*");
		};
	}

	@Bean
	public WebMvcConfigurer forwardToIndex() {
		return new WebMvcConfigurer() {
			@Override
			public void addViewControllers(final @NonNull ViewControllerRegistry registry) {
				registry.addViewController("/").setViewName("forward:/index.xhtml");
				registry.addViewController("/error").setViewName("forward:/errors/500.xhtml");
				registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
			}
		};
	}

	@Bean
	public ErrorPageRegistrar errorPageRegistrar() {
		return registry -> {
			registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/errors/404.xhtml"));
			registry.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/errors/403.xhtml"));
			registry.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/errors/403.xhtml"));
		};
	}

}
