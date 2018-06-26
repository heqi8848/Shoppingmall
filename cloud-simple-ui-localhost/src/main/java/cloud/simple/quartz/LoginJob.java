package cloud.simple.quartz;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cloud.simple.UserAgentInterceptor;
import cloud.simple.model.User;
import cloud.simple.service.MyService;

@Component
@ConfigurationProperties
public class LoginJob implements Job{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		RestTemplate restTemplate = new RestTemplate();
		UserAgentInterceptor userAgentInterceptor = new UserAgentInterceptor();
		userAgentInterceptor.headerValue = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
		restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(userAgentInterceptor));
		User user = new User(0, "apm", "123456");
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		HttpEntity<User> req = new HttpEntity<User>(user, headers);
		String url = "http://" + "localhost" + ":" + "8080"  + "/user/user/login?workload=1000";
		
		User result = restTemplate.postForObject(url, req, User.class);
		
		logger.info("excute Login job");
	}

}
