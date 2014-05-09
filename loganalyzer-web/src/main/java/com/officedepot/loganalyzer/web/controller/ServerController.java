package com.officedepot.loganalyzer.web.controller;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Sets;
import com.officedepot.loganalyzer.dao.ServerDao;
import com.officedepot.loganalyzer.domain.Server;
import com.officedepot.loganalyzer.meta.LogAnalyzerMeta;

@Controller
public class ServerController {

	@Resource
	private ServerDao serverDao;

	@RequestMapping("/servers")
	public Iterable<Server> getServerList(Model model) {
		return LogAnalyzerMeta.getInstance().getLogSources();
	}

	@RequestMapping(value = "/servers/add", method = RequestMethod.POST)
	public String addServer(@ModelAttribute("server") Server server) {
		serverDao.save(server);
		reloadServers();
		return "redirect:/servers";
	}
	
	@RequestMapping(value = "/servers/delete/{serverId}")
	public String deleteServer(@PathVariable String serverId) {
		serverDao.delete(serverId);
		reloadServers();
		return "redirect:/servers";
	}
	
	private void reloadServers() {
		Iterable<Server> servers = serverDao.findAll(new Sort("name", "nodeId"));
        if (null != servers && servers.iterator().hasNext()) {
            LogAnalyzerMeta.getInstance().setLogSources(Sets.newLinkedHashSet(servers));
        }
	}
	
}
