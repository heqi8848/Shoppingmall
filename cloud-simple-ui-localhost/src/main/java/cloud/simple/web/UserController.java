/*
 * Copyright 2012-2020 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * @author lzhoumail@126.com/zhouli
 * Git http://git.oschina.net/zhou666/spring-cloud-7simple
 */

package cloud.simple.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import cloud.simple.model.AutoTestInfo;
import cloud.simple.model.AutotestConf;
import cloud.simple.model.Buy;
import cloud.simple.model.Item;
import cloud.simple.model.Product;
import cloud.simple.model.User;
import cloud.simple.model.UserAutoTestConf;
import cloud.simple.quartz.BuyJob;
import cloud.simple.quartz.LoginJob;
import cloud.simple.quartz.QuartzManager;
import cloud.simple.quartz.SearchAllJob;
import cloud.simple.quartz.SearchJob;
import cloud.simple.service.MyService;

@RestController
@ConfigurationProperties
public class UserController {
//	static public List<Product> lst = new ArrayList<>();
//	static {
//		lst.add(new Product(34211223411L, "product1", 2246, 400, 0));
//		lst.add(new Product(34211223412L, "product2", 3488, 5, 0));
//		lst.add(new Product(34211223413L, "product3", 1345, 6, 1));
//		lst.add(new Product(34211223414L, "product4", 1345, 7, 1));
//	}
	
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${apigateway.host:localhost}")
	public String apigatewayIp;

	@Value("${apigateway.port:8080}")
	public String apigatewayPort;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MyService MyService;

	@RequestMapping(value = "/product/searchAll")
	public List<Product> searchAll() {
		return MyService.searchAll();
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public User login(@RequestBody User u, @RequestParam(value="workload", required=false) Integer workload) {
		return MyService.login(u, workload);
	}

	@RequestMapping(value = "/product/buy/{id}", method = RequestMethod.POST)
	public Boolean buy(@PathVariable int id, @RequestBody Buy b) {
		return MyService.buy(id, b);
	}

	@RequestMapping(value = "/product/search", method = RequestMethod.GET)
	public List<Product> searchByName(@RequestParam(value = "productName", required = true) String productName) {
		return MyService.searchByName(productName);
	}
	
	@RequestMapping(value="/autotest/set", method=RequestMethod.POST)
	public Boolean setAutoTest( @RequestBody AutotestConf autotestConf)
	{
		System.out.println(autotestConf);
		UserAutoTestConf.instance().setUrlSwitch(autotestConf);
		
		QuartzManager.shutdownJobs();
		Map<String,AutoTestInfo> map = UserAutoTestConf.instance().getUrlSwitch();
		
		AutoTestInfo loginInfo = map.get("/user/login");
		AutoTestInfo BuyInfo = map.get("/product/buy");
		AutoTestInfo searchInfo = map.get("/product/search");
		AutoTestInfo searchAllInfo = map.get("/product/searchAll");
		
		if(1 == loginInfo.getTestSwitch()){
			QuartzManager.addJob("login", "login", "login", "login", LoginJob.class, "0/" + loginInfo.getPeriod() + " * * * * ?");
		}
		if(1 == BuyInfo.getTestSwitch()){
			QuartzManager.addJob("buy", "buy", "buy", "buy", BuyJob.class, "0/" + BuyInfo.getPeriod() + " * * * * ?");
		}
		if(1 == searchInfo.getTestSwitch()){
			QuartzManager.addJob("search", "search", "search", "search", SearchJob.class, "0/" + searchInfo.getPeriod() + " * * * * ?");
		}
		if(1 == searchAllInfo.getTestSwitch()){
			QuartzManager.addJob("searchAll", "searchAll", "searchAll", "searchAll", SearchAllJob.class, "0/" + searchAllInfo.getPeriod() + " * * * * ?");
		}
		return true;
	}
	
	@RequestMapping(value="/autotest/get")
	public Map<String,AutoTestInfo> getAutoTest(){
		System.out.println(UserAutoTestConf.instance().getUrlSwitch());
		return UserAutoTestConf.instance().getUrlSwitch();
	}
}

