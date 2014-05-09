package com.officedepot.loganalyzer.dao;

import com.officedepot.loganalyzer.domain.MessageEntry;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by CH on 4/17/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/applicationContext-loganalyzer-dao-mysql.xml")
public class MessageEntryDaoTest {

    @Resource
    private MessageEntryDao dao;

    @Test
    @Repeat(1)
    public void testInsert() {
        MessageEntry entry = new MessageEntry("www-label-" + RandomUtils.nextInt(12), new Date());
        entry.setMessage("message-" + RandomStringUtils.randomAlphanumeric(10));
        dao.save(entry);
        MessageEntry persisted = dao.findOne(entry.getId());
        System.out.println(persisted.toString());
        Assert.assertNotNull(persisted);
        Assert.assertTrue(dao.exists(entry.getId()));
    }

    @Test
    public void testList() {
        Iterable<MessageEntry> messageEntries = dao.findAll();
        Assert.assertNotNull(messageEntries);
        for (MessageEntry messageEntry : messageEntries) {
            System.out.println(messageEntry.toString());
        }
    }

    @Test
    public void testQuery() {
        StopWatch sw = new StopWatch("query");
        sw.start();
        Pageable pageable = new PageRequest(0, 40, Sort.Direction.DESC, "id.timestamp");
        Page<MessageEntry> messageEntries = dao.findByServer("www-label-0", pageable);
        sw.stop();
        System.out.println("=======================");
        System.out.println(sw.prettyPrint());
        System.out.println("messageEntries:" + messageEntries.getSize());
        Assert.assertNotNull(messageEntries);
        for (MessageEntry entry : messageEntries) {
            System.out.println(entry.toString());
        }
    }
}
