package com.officedepot.loganalyzer.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.officedepot.loganalyzer.domain.Server;

/**
 * Created by CH on 4/17/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/applicationContext-loganalyzer-dao-mysql.xml")
@Transactional
public class ServerDaoTest {

    @Resource
    private ServerDao dao;

    @Test
    public void testInsert() {
        Server server = new Server("www", "01", "http://205.157.102.187/logs/205.157.102.141/logs/SystemOut.log");
        dao.save(server);
        Server persisted = dao.findOne(server.getId());
        System.out.println(persisted.toString());
        Assert.assertNotNull(persisted);
        Assert.assertTrue(dao.exists(server.getId()));
    }
    
    @Test
    public void iniDB() {
    	List<Server> servers = new ArrayList<Server>();
        servers.add(new Server("www", "01", "http://205.157.102.187/logs/205.157.102.141/logs/SystemOut.log"));
    	servers.add(new Server("www", "02", "http://205.157.102.187/logs/205.157.102.142/logs/SystemOut.log"));
    	servers.add(new Server("www", "03", "http://205.157.102.187/logs/205.157.102.143/logs/SystemOut.log"));
    	servers.add(new Server("www", "04", "http://205.157.102.187/logs/205.157.102.144/logs/SystemOut.log"));
    	servers.add(new Server("www", "05", "http://205.157.102.187/logs/205.157.102.145/logs/SystemOut.log"));
    	servers.add(new Server("www", "06", "http://205.157.102.187/logs/205.157.102.146/logs/SystemOut.log"));
    	servers.add(new Server("www", "07", "http://205.157.102.187/logs/205.157.102.147/logs/SystemOut.log"));
    	servers.add(new Server("www", "08", "http://205.157.102.187/logs/205.157.102.148/logs/SystemOut.log"));
    	servers.add(new Server("www", "09", "http://205.157.102.187/logs/205.157.102.149/logs/SystemOut.log"));
    	servers.add(new Server("www", "10", "http://205.157.102.187/logs/205.157.102.150/logs/SystemOut.log"));
    	servers.add(new Server("www", "11", "http://205.157.102.187/logs/205.157.102.223/logs/SystemOut.log"));
    	servers.add(new Server("www", "12", "http://205.157.102.187/logs/205.157.102.225/logs/SystemOut.log"));

        servers.add(new Server("bsd", "01", "http://bsdpreview.na.odcorp.net/app/bsdprd01/logs/logs/SystemOut.log"));
        servers.add(new Server("bsd", "02", "http://205.157.103.202/app/bsdprd02/logs/logs/SystemOut.log"));
        servers.add(new Server("bsd", "03", "http://205.157.103.202/app/bsdprd03/logs/logs/SystemOut.log"));
        servers.add(new Server("bsd", "04", "http://205.157.103.202/app/bsdprd04/logs/logs/SystemOut.log"));
        servers.add(new Server("bsd", "05", "http://205.157.103.202/app/bsdprd05/logs/logs/SystemOut.log"));
        servers.add(new Server("bsd", "06", "http://205.157.103.202/app/bsdprd06/logs/logs/SystemOut.log"));
        servers.add(new Server("bsd", "07", "http://205.157.103.202/app/bsdprd07/logs/logs/SystemOut.log"));
        servers.add(new Server("bsd", "08", "http://205.157.103.202/app/bsdprd08/logs/logs/SystemOut.log"));
        servers.add(new Server("bsd", "09", "http://205.157.103.202/app/bsdprd09/logs/logs/SystemOut.log"));
        servers.add(new Server("bsd", "10", "http://205.157.103.202/app/bsdprd10/logs/logs/SystemOut.log"));

        servers.add(new Server("gmil", "01", "http://10.95.145.40/app1/SystemOut.log"));
        servers.add(new Server("gmil", "02", "http://10.95.145.40/app2/SystemOut.log"));
        servers.add(new Server("gmil", "03", "http://10.95.145.40/app3/SystemOut.log"));
        servers.add(new Server("gmil", "04", "http://10.95.145.40/app4/SystemOut.log"));
        servers.add(new Server("gmil", "05", "http://10.95.145.40/app5/SystemOut.log"));
        servers.add(new Server("gmil", "06", "http://10.95.145.40/app6/SystemOut.log"));

        servers.add(new Server("oden", "01", "http://10.95.66.95/oden/logs/SystemOut.log"));
        servers.add(new Server("oden", "02", "http://10.95.66.96/oden/logs/SystemOut.log"));

        servers.add(new Server("odws", "01", "http://205.157.103.160/chiowsprdcmb01/odwebservices/logs/SystemOut.log"));
        servers.add(new Server("odws", "02", "http://205.157.103.160/chiowsprdcmb02/odwebservices/logs/SystemOut.log"));

        servers.add(new Server("gtools", "01", "http://10.95.66.39:8080/gtn/logs/SystemOut.log"));
        servers.add(new Server("gtools", "02", "http://10.95.66.46:8080/gtn/logs/SystemOut.log"));
        

        servers.add(new Server("cpd", "01", "http://205.157.102.187/logs/205.157.102.141.cpd/logs/SystemOut.log"));
        servers.add(new Server("cpd", "02", "http://205.157.102.187/logs/205.157.102.142.cpd/logs/SystemOut.log"));
        servers.add(new Server("cpd", "03", "http://205.157.102.187/logs/205.157.102.143.cpd/logs/SystemOut.log"));
        servers.add(new Server("cpd", "04", "http://205.157.102.187/logs/205.157.102.144.cpd/logs/SystemOut.log"));

        servers.add(new Server("bsdupload", "", "http://10.95.66.84/bsdupload/logs/SystemOut.log"));
    	dao.save(servers);
    }

    @Test
    public void testList() {
        Iterable<Server> servers = dao.findAll();
        Assert.assertNotNull(servers);
        for (Server server : servers) {
            System.out.println(server);
        }
    }

}
