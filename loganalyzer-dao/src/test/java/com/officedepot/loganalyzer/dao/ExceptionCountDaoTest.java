package com.officedepot.loganalyzer.dao;

import java.util.Date;

import com.officedepot.loganalyzer.domain.ExceptionCount;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Created by CH on 4/17/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/applicationContext-loganalyzer-dao-mysql.xml")
@Transactional
public class ExceptionCountDaoTest {
	
	private static final DateTimeFormatter FORMATTER_TIME = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Resource
    private ExceptionCountDao dao;

    @Test
    public void testInsert() {
        ExceptionCount ec = new ExceptionCount("www-01", "NullPointerException", 1L, new Date().getTime());
        dao.save(ec);
        ExceptionCount persisted = dao.findOne(ec.getId());
        System.out.println(persisted.toString());
        Assert.assertNotNull(persisted);
        Assert.assertTrue(dao.exists(ec.getId()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        persisted.increaseCount();
        dao.save(persisted);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        persisted.increaseCount(5);
        dao.save(persisted);
    }

    @Test
    public void testList() {
        Date startDate = DateTime.parse("2014-05-01", FORMATTER_TIME).toDate();
        Date endDate = DateTime.parse("2014-05-02", FORMATTER_TIME).toDate();
        Iterable<ExceptionCount> ecs = dao.findByServerAndTimestampBetweenOrderByTimestampAsc("www-01", startDate.getTime(), endDate.getTime());
        Assert.assertNotNull(ecs);
        for (ExceptionCount ec : ecs) {
            System.out.println(ec);
        }
    }
    
    @Test
    public void testCount() {
    	Date startDate = DateTime.parse("2014-05-01", FORMATTER_TIME).toDate();
    	Date endDate = DateTime.parse("2014-05-12", FORMATTER_TIME).toDate();
    	Iterable<ExceptionCount> ecs = dao.findByTimestampBetween(startDate.getTime(), endDate.getTime());
    	Assert.assertNotNull(ecs);
    	for (ExceptionCount ec : ecs) {
    		System.out.println(ec);
    	}
    }

}
