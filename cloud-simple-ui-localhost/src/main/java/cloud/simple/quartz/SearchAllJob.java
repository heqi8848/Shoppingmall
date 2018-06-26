package cloud.simple.quartz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cloud.simple.UserAgentInterceptor;
import cloud.simple.model.Item;
import cloud.simple.model.Product;

@Component
@ConfigurationProperties
public class SearchAllJob implements Job{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		RestTemplate restTemplate = new RestTemplate();
		UserAgentInterceptor userAgentInterceptor = new UserAgentInterceptor();
		userAgentInterceptor.headerValue = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
		restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(userAgentInterceptor));
		String url = "http://" + "localhost" + ":" + "8080"  + "/product/product/searchAll";
		Item[] itemlist = restTemplate.getForObject(url, Item[].class);
		logger.info("excute searchAll job");
	}

}
