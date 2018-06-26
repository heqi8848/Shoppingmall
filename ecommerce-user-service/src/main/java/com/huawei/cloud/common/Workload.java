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
	
	@Value("${workload:10}")
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
			   logger.info("Throttler ->The work takes "+delay+"  millisec");
		  }	
		  Thread.sleep(delay);
		} catch (Exception e) {
			 logger.info(e.getMessage());
		}
	}
	public void doSomeWork(Integer num)
	{
		try {
		  if(num > 0){
			   logger.info("Throttler ->The work takes "+num+"  millisec");
		  }	
		  Thread.sleep(num);
		} catch (Exception e) {
			 logger.info(e.getMessage());
		}
	}
}

