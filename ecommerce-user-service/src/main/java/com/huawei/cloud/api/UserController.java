package com.huawei.cloud.api;

import com.huawei.cloud.common.Workload;
import com.huawei.cloud.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/user")
@ConfigurationProperties
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${persistence-server.host:localhost}")
	public String persistenceIp;
	
	@Value("${persistence-server.port:8080}")
	public String persistencePort;
	
	@Value("${persistence-server.mapping:/persistence}")
	public String persistenceMapping;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Workload works;
	
	@PostMapping("/login")
	@ResponseBody
	public User Login(@RequestBody User user, @RequestParam(value="workload", required=false) Integer workload){
		if (null ==workload){
			works.doSomeWork();
		}else{
			works.doSomeWork(workload);
		}
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		HttpEntity<User> req = new HttpEntity<User>(user, headers);
		
		String url = "http://" + persistenceIp + ":" + persistencePort + persistenceMapping + "/user";
		User result = restTemplate.postForObject(url, req, User.class);
		System.out.println(result);
		return result;
	}	
	
    @PostMapping("/validate")
    @ResponseBody
    public User Validate(@RequestBody User user) {
    	return Login(user, 1);
    }
	
//	@GetMapping("/update")
//	public User getUserById(@PathVariable Long id){
//		return restTemplate.getForObject("http://localhost:8080/simple/" + id, User.class);
//	}
}
