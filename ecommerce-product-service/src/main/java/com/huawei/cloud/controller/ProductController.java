package com.huawei.cloud.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.huawei.cloud.common.Workload;
import com.huawei.cloud.entity.Item;
import com.huawei.cloud.entity.User;

@Controller
@RequestMapping("/product")
@ConfigurationProperties
public class ProductController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${persistence-server.host:localhost}")
	public String persistenceIp;
	
	@Value("${persistence-server.port:8080}")
	public String persistencePort;
	
	@Value("${persistence-server.mapping:/persistence}")
	public String persistenceMapping;
	
	@Value("${users-server.host:localhost}")
	public String userIp;
	
	@Value("${users-server.port:8080}")
	public String userPort;	
	
	@Autowired
	private Workload workload;
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/search")
	@ResponseBody
	public Object getItemsByName(@RequestParam String productName){
		String url = "http://" + persistenceIp + ":" + persistencePort + persistenceMapping + "/products_by_name?name=" + productName;
		Object itemlist = restTemplate.getForObject(url, Object.class);
		logger.info("itemlist is: " + itemlist);
		return itemlist;		
	}
	
	@GetMapping("/searchAll")
	@ResponseBody
	public Object getItems(){
		String url = "http://" + persistenceIp + ":" + persistencePort + persistenceMapping + "/products";
		Object itemlist = restTemplate.getForObject(url, Object.class);
		logger.info("itemlist is: " + itemlist);
		return itemlist;
	}
	
	@PostMapping("/buy/{id}")
	@ResponseBody
	public Boolean buyItem(@PathVariable("id") Long id, @RequestBody User user) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		HttpEntity<User> req = new HttpEntity<User>(user, headers);
		
		String userUrl = "http://" + userIp + ":" + userPort + "/user/validate";
		logger.error(userUrl);
		User validateUser = restTemplate.postForObject(userUrl, req, User.class);
		System.out.println(validateUser.getName());
		if(null != validateUser){
			Long userId = validateUser.getId();
			String url = "http://" + persistenceIp + ":" + persistencePort + persistenceMapping + "/payment/" + userId + "/" + id;
			System.out.println(url);
			Boolean result = restTemplate.getForObject(url, Boolean.class);
			logger.info(user.getName() + "bought " + id);
			return result;			
		}else {
			throw new Exception("unvalidate user!!");
		}
	}
}

