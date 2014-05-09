package com.officedepot.loganalyzer.web.controller;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.officedepot.loganalyzer.dao.MessageEntryDao;
import com.officedepot.loganalyzer.domain.MessageEntry;

@Controller
public class MessageEntryController {

    @Resource
    private MessageEntryDao messageEntryDao;

    @RequestMapping("/message/{server}")
    public String getMessageByServer(@PathVariable String server, Model model) {
    	 Pageable pageable = new PageRequest(0, 40, Sort.Direction.DESC, "id.timestamp");
         Page<MessageEntry> messageEntries= messageEntryDao.findByServer(server,pageable);
        model.addAttribute("messageEntries", messageEntries);
        System.out.println("messageEntries");
        return "messageEntries";
    }
    
    @RequestMapping("/message")
    public String getMessageList(@PathVariable String server, Model model) {
    	Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC, "id.timestamp");
    	 Iterable<MessageEntry> messageEntries = messageEntryDao.findAll(pageable);
    	model.addAttribute("messageEntries", messageEntries);
    	System.out.println("messageEntries");
    	return "messageEntries";
    }
    
}
