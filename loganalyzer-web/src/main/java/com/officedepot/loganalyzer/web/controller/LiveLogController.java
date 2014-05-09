/**
 * 
 */
package com.officedepot.loganalyzer.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.officedepot.loganalyzer.processor.live.LiveLog;

/**
 * @author hao
 */
@Controller
public class LiveLogController {
	
	@Resource
	private LiveLog liveLog;

	@RequestMapping("/live/{server}")
	public String helloWorld(@PathVariable String server, Model model) {
		model.addAttribute("log", liveLog.getStackedLiveException(server));
		return "live";
	}
	
	@RequestMapping(value = "/live/{server}/pull", method = RequestMethod.GET)
	@ResponseBody
	public DeferredResult<String> pull(@PathVariable String server) {
		DeferredResult<String> result = new DeferredResult<String>();
		result.setResult(liveLog.getLiveException(server));
		return result;
	}
}
