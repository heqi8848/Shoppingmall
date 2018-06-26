package cloud.simple.quartz;

import cloud.simple.UserAgentInterceptor;
import cloud.simple.model.Buy;
import cloud.simple.model.User;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@ConfigurationProperties
public class BuyJob implements Job{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		RestTemplate restTemplate = new RestTemplate();
		UserAgentInterceptor userAgentInterceptor = new UserAgentInterceptor();
		userAgentInterceptor.headerValue = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
		restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(userAgentInterceptor));
		User user = new User(0, "apm", "123456");
		Buy buy = new Buy();
		buy.setUser(user);
		List<Long> productidList = new ArrayList<>();
		productidList.add(34211223411L);
		buy.setProductidList(productidList);
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		HttpEntity<User> req = new HttpEntity<User>(buy.getUser(), headers);
		Boolean result = true;
		for (long productid : buy.getProductidList()) {
			String url = "http://" + "localhost" + ":" + "8080"  + "/product/product/buy/" + productid;
			result &= restTemplate.postForObject(url, req, Boolean.class);
		}
		
		
		logger.info("excute Buy job");
	}

}
