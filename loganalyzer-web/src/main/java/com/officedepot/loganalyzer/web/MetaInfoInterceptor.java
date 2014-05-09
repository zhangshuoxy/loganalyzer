/**
 * 
 */
package com.officedepot.loganalyzer.web;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.officedepot.loganalyzer.domain.Server;
import com.officedepot.loganalyzer.meta.LogAnalyzerMeta;

/**
 * @author hao
 */
public class MetaInfoInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (null != modelAndView) {
			Set<Server> servers = LogAnalyzerMeta.getInstance().getLogSources();
			MultiValueMap<String, Server> serverMap = new LinkedMultiValueMap<String, Server>();
	    	for (Server server : servers) {
	    		serverMap.add(server.getName(), server);
	    	}
	    	
	    	modelAndView.addObject("servers", servers);
	    	modelAndView.addObject("serverMap", serverMap);
			modelAndView.addObject("ignoreList", LogAnalyzerMeta.getInstance().getIgnoreList());
		}
	}
}
