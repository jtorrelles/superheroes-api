package com.mindata.es.superheroeschallenge.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class ApiKeyHandlerFilter extends GenericFilterBean {

	@Value("${api.security.api-key}")
	private String apiKey;
	
	private static final String MESSAGE = "Invalid API KEY";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		var req = (HttpServletRequest) request;
		var path = req.getRequestURI();

		if (path.startsWith("/superheroes") == false) {
			chain.doFilter(request, response);
			return;
		}

		String key = req.getHeader(HttpHeaders.AUTHORIZATION) == null ? "" : req.getHeader(HttpHeaders.AUTHORIZATION);

		if (key.equals(apiKey)) {
			chain.doFilter(request, response);
		} else {
			var httpResponse = (HttpServletResponse) response;

			httpResponse.reset();
			httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentLength(MESSAGE.length());
			response.getWriter().write(MESSAGE);
		}
	}

}
