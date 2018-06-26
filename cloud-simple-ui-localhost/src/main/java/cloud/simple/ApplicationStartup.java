package cloud.simple;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cloud.simple.quartz.BuyJob;
import cloud.simple.quartz.LoginJob;
import cloud.simple.quartz.QuartzManager;
import cloud.simple.quartz.SearchAllJob;
import cloud.simple.quartz.SearchJob;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		QuartzManager.addJob("searchAll", "searchAll", "searchAll", "searchAll", SearchAllJob.class, "0/2 * * * * ?");
		QuartzManager.addJob("buy", "buy", "buy", "buy", BuyJob.class, "0/12 * * * * ?");
		QuartzManager.addJob("login", "login", "login", "login", LoginJob.class, "0/20 * * * * ?");
	}

}
