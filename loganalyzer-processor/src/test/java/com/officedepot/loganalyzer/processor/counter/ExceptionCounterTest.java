package com.officedepot.loganalyzer.processor.counter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by CH on 4/23/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:META-INF/applicationContext-loganalyzer-dao-mysql.xml",
        "classpath:META-INF/applicationContext-loganalyzer-processor.xml"
})
public class ExceptionCounterTest {

    @Resource
    private ExceptionCounter counter;

    @Test
    public void testStart() {
        System.out.println("starting.............1..");
        counter.persistExceptionCounter();
        System.out.println("ended................1.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
