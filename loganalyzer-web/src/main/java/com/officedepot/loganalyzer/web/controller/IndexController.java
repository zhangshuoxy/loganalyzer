package com.officedepot.loganalyzer.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.mutable.MutableLong;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.officedepot.loganalyzer.LogAnalyzerConstants;
import com.officedepot.loganalyzer.dao.ExceptionCountDao;
import com.officedepot.loganalyzer.dao.ExceptionEntryDao;
import com.officedepot.loganalyzer.domain.ExceptionCount;
import com.officedepot.loganalyzer.domain.ExceptionEntry;
import com.officedepot.loganalyzer.utils.MapUtils;

/**
 * Created by CH on 4/23/14.
 */
@Controller
public class IndexController {
	
    @Resource
    private ExceptionCountDao exceptionCountDao;

    @Resource
    private ExceptionEntryDao exceptionEntryDao;

    @RequestMapping("/")
    public String dashboard(Model model) {
    	DateTime today = new DateTime().withTimeAtStartOfDay();
    	Date startDate = today.minusDays(1).toDate();
    	Date endDate = today.toDate();
    	model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        Iterable<ExceptionCount> counts = exceptionCountDao.findByTimestampBetween(startDate.getTime(), endDate.getTime());
        Map<String, MultiValueMap<String, ExceptionCount>> serverExceptionCounts = new TreeMap<>();
        for (ExceptionCount ec : counts) {
        	String serverName = ec.getServer().split("\\-")[0];
        	MultiValueMap<String, ExceptionCount> serverExceptionCount = serverExceptionCounts.get(serverName);
        	if (null == serverExceptionCount) {
        		serverExceptionCount = new LinkedMultiValueMap<>();
        		serverExceptionCounts.put(serverName, serverExceptionCount);
        	}
        	serverExceptionCount.add(ec.getServer(), ec);
        }
        model.addAttribute("serverExceptionCounts", serverExceptionCounts);
    	
        return "dashboard";
    }
    
    @RequestMapping("/dashboard/{server}")
    public String dashboard(@PathVariable String server, Model model) {
    	DateTime today = new DateTime().withTimeAtStartOfDay();
    	Date startDate = today.minusDays(1).toDate();
    	Date endDate = today.toDate();
        
        renderDashboard(model, server, startDate, endDate);
    	
        return "dashboard-server";
    }
    
    @RequestMapping("/dashboard/{server}/{fromDate}/to/{toDate}")
    public String dashboard(@PathVariable String server, @PathVariable String fromDate, @PathVariable String toDate, Model model) {
    	Date startDate = DateTime.parse(fromDate, LogAnalyzerConstants.FORMATTER_DATE).toDate();
        Date endDate = DateTime.parse(toDate, LogAnalyzerConstants.FORMATTER_DATE).toDate();
        
        renderDashboard(model, server, startDate, endDate);
    	
        return "dashboard-server";
    }
    

    @RequestMapping("/dashboard/{server}/{fromDate}/to/{toDate}/{exception}")
    public String getException(@PathVariable String server, @PathVariable String exception, @PathVariable String fromDate, @PathVariable String toDate, Model model) {
    	model.addAttribute("server", server);
    	model.addAttribute("exception", exception);
    	model.addAttribute("fromDate", fromDate);
    	model.addAttribute("toDate", toDate);
    	
    	Date startDate = DateTime.parse(fromDate, LogAnalyzerConstants.FORMATTER_DATE).toDate();
        Date endDate = DateTime.parse(toDate, LogAnalyzerConstants.FORMATTER_DATE).toDate();
        
		List<ExceptionEntry> exceptions = exceptionEntryDao.findByServerAndException(server, exception, startDate.getTime(), endDate.getTime());
    	
    	Map<String, MutableLong> locations = new HashMap<>();
    	for (ExceptionEntry entry : exceptions) {
    		MutableLong count = new MutableLong(1);
            MutableLong oldCount = locations.put(entry.getLocation(), count);
            if (null != oldCount) {
                count.add(oldCount.intValue());
            }
    	}
    	locations = MapUtils.sortByValue(locations);
    	model.addAttribute("locations", locations);
    	
    	Iterable<ExceptionCount> counts = exceptionCountDao.findByServerAndExceptionAndTimestampBetweenOrderByTimestampAsc(server, exception, startDate.getTime(), endDate.getTime());
    	model.addAttribute("counts", counts);
    	
    	return "dashboard-server-exception";
    }
    
    private void renderDashboard(Model model, String server, Date startDate, Date endDate) {
    	model.addAttribute("server", server);
    	model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        Iterable<ExceptionCount> counts = exceptionCountDao.findByServerAndTimestampBetweenOrderByTimestampAsc(server, startDate.getTime(), endDate.getTime());
    	
    	Map<String, MutableLong> exceptions = new HashMap<>();
    	for (ExceptionCount ec : counts) {
    		MutableLong count = new MutableLong(ec.getCount());
            MutableLong oldCount = exceptions.put(ec.getException(), count);
            if (null != oldCount) {
                count.add(oldCount.intValue());
            }
    	}
    	
    	exceptions = MapUtils.sortByValue(exceptions, 30);
    	model.addAttribute("exceptions", exceptions);

        MultiValueMap<String, ExceptionCount> exceptionCounts = new LinkedMultiValueMap<>();
        for (ExceptionCount ec : counts) {
        	if (exceptions.containsKey(ec.getException())) {
        		exceptionCounts.add(ec.getException(), ec);
        	}
        }
        model.addAttribute("exceptionCounts", exceptionCounts);
    }
}
