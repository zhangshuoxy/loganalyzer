package com.officedepot.loganalyzer.web.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.officedepot.loganalyzer.LogAnalyzerConstants;
import com.officedepot.loganalyzer.dao.ExceptionEntryDao;
import com.officedepot.loganalyzer.domain.ExceptionEntry;

@Controller
public class ExceptionEntryController {

    @Resource
    private ExceptionEntryDao exceptionEntryDao;

    @RequestMapping("/exception/{server}/{fromDate}/to/{toDate}/{exception}")
    public String getException(@PathVariable String server, @PathVariable String fromDate, @PathVariable String toDate, @PathVariable String exception, 
    		@RequestParam(value = "lastRecordTime", defaultValue = "0", required = false) long lastRecordTime, Model model) {
    	model.addAttribute("server", server);
    	model.addAttribute("exception", exception);
    	model.addAttribute("startDate", fromDate);
    	model.addAttribute("endDate", toDate);
    	
    	Date startDate = DateTime.parse(fromDate, LogAnalyzerConstants.FORMATTER_DATE).toDate();
        Date endDate = DateTime.parse(toDate, LogAnalyzerConstants.FORMATTER_DATE).toDate();
    	
    	boolean isShowMore = false;
    	if (0 != lastRecordTime) {
    		Date lastRecordTimestamp = new Date(lastRecordTime - 1);
    		if (lastRecordTimestamp.after(startDate) && lastRecordTimestamp.before(endDate)) {
    			endDate = lastRecordTimestamp;
    			isShowMore = true;
    		}
    	}
    	
    	Pageable pageable = new PageRequest(0, 20, Sort.Direction.DESC, "id.timestamp");
    	Page<ExceptionEntry> exceptions = exceptionEntryDao.findByServerAndException(server, exception, startDate.getTime(), endDate.getTime(), pageable);
    	model.addAttribute("exceptions", exceptions);
    	
    	return isShowMore ? "exceptions-data" : "exceptions";
    }
    
    @RequestMapping("/exception/{server}/{fromDate}/to/{toDate}/{exception}/{location}")
    public String getException(@PathVariable String server, @PathVariable String fromDate, @PathVariable String toDate, @PathVariable String exception, 
    		@PathVariable String location, @RequestParam(value = "lastRecordTime", defaultValue = "0", required = false) long lastRecordTime, Model model) {
    	model.addAttribute("server", server);
    	model.addAttribute("exception", exception);
    	model.addAttribute("location", location);
    	model.addAttribute("startDate", fromDate);
    	model.addAttribute("endDate", toDate);
    	
    	Date startDate = DateTime.parse(fromDate, LogAnalyzerConstants.FORMATTER_DATE).toDate();
        Date endDate = DateTime.parse(toDate, LogAnalyzerConstants.FORMATTER_DATE).toDate();
    	
    	boolean isShowMore = false;
    	if (0 != lastRecordTime) {
    		Date lastRecordTimestamp = new Date(lastRecordTime - 1);
    		if (lastRecordTimestamp.after(startDate) && lastRecordTimestamp.before(endDate)) {
    			endDate = lastRecordTimestamp;
    			isShowMore = true;
    		}
    	}
    	
    	Pageable pageable = new PageRequest(0, 20, Sort.Direction.DESC, "id.timestamp");
    	Page<ExceptionEntry> exceptions = exceptionEntryDao.findByServerAndExceptionAndLocation(server, exception, location, startDate.getTime(), endDate.getTime(), pageable);
    	model.addAttribute("exceptions", exceptions);
    	
    	return isShowMore ? "exceptions-data" : "exceptions";
    }
}
