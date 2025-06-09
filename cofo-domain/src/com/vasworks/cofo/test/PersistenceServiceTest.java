package com.vasworks.cofo.test;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vasworks.cofo.service.AgentServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class PersistenceServiceTest {
	
	public static final Log LOG = LogFactory.getLog(PersistenceServiceTest.class);
	
	@Autowired
	private AgentServiceImpl agentService;
	
	@Test
	public void doNothing() {
	}
	
	@Test
	public void testSaveWinner() {
		File[] uploads = new File[3];
//		agentService.register("niyid@yahoo.com", "Niyi Dada", "passw0rd123");
//		agentService.authenticate("niyid@yahoo.com", "passw0rd123");
		agentService.savePolygonSurvey(33l, 2l, "Test polygon mapped from http://www.the-di-lab.com/polygon/", uploads, "(6.611304343011702, 3.3204808831214905)(6.610587626286675, 3.320840299129486)(6.6112856924037775, 3.3221572637557983)(6.612154277112049, 3.321293592453003)(6.611304343011702, 3.3204808831214905)", "fieldagent1@gtsng.com");
	}
}
