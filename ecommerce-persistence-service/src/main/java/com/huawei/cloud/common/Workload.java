package com.huawei.cloud.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class Workload {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${workload:200}")
	private long delay;
	
	public void updateDelay(long delay)
	{
		this.delay=delay;
		 logger.info("delay changed to "+delay);
	}
	public void doSomeWork()
	{
		try {
		  if(delay!=0){
			   logger.info("Throttler ->going to sleep for  "+delay+"  millisec");
		  }	
		  Thread.sleep(delay);
		} catch (Exception e) {
			 logger.info(e.getMessage());
		}
	}

}

