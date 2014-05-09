package com.officedepot.loganalyzer.dao;

import com.officedepot.loganalyzer.domain.ExceptionEntry;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by CH on 4/17/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/applicationContext-loganalyzer-dao-mysql.xml")
@Transactional
public class ExceptionEntryDaoTest {

    @Resource
    private ExceptionEntryDao dao;

    @Test
    @Repeat(1)
    @Rollback(false)
    public void testInsert() {
        String id = "bsd-label" + RandomUtils.nextInt(12);
        ExceptionEntry entry = new ExceptionEntry(id, new Date(), "NullPointerException", "Some Action", "msg", "exception details" + RandomStringUtils.randomAlphanumeric(10));
        dao.save(entry);
        ExceptionEntry persisted = dao.findOne(entry.getId());
        System.out.println(persisted);
        Assert.assertNotNull(persisted);
        Assert.assertTrue(dao.exists(entry.getId()));

        id = "www-label" + RandomUtils.nextInt(12);
        entry = new ExceptionEntry(id, new Date(), "NumberFormatException", "Some Action", "msg", "exception details" + RandomStringUtils.randomAlphanumeric(10));
        dao.save(entry);
    }

    @Test
    public void testQuery() {
        StopWatch sw = new StopWatch("query");
        sw.start();
        Pageable pageable = new PageRequest(0, 40, Sort.Direction.DESC, "id.timestamp");
        Page<ExceptionEntry> entries = dao.findByServerAndException("www-label0", "RuntimeException", pageable);
        sw.stop();
        System.out.println("=======================");
        System.out.println(sw.prettyPrint());
        System.out.println("logEntries:" + entries.getSize());
        Assert.assertNotNull(entries);
        for (ExceptionEntry e : entries) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testTimestampOfLastEntry() {
        StopWatch sw = new StopWatch("query");
        sw.start();
        Pageable pageable = new PageRequest(0, 1, Sort.Direction.DESC, "id.timestamp");
        List<ExceptionEntry> entries = dao.findByServer("www-label0", pageable);
        sw.stop();
        System.out.println("=======================");
        System.out.println(sw.prettyPrint());
        Assert.assertNotNull(entries);
        for (ExceptionEntry entry : entries) {
            System.out.println(entry);
        }
    }

    @Test
    public void testGetExceptionCount() {
        StopWatch sw = new StopWatch("query");
        sw.start();
        DateTime dt = new DateTime(2014, 4, 22, 15, 10, 23, 0);//2014-04-23 12:16:23
        Date date = dt.toDate();
        Long count = dao.getExceptionCountSince("www-label0", "NullPointerException", date);
        sw.stop();
        System.out.println("=======================");
        System.out.println(sw.prettyPrint());
        Assert.assertNotNull(count);
        System.out.println("count: " + count);
    }
}
