package cloud.simple.service;

import cloud.simple.WebApplication;
import cloud.simple.model.Buy;
import cloud.simple.model.Item;
import cloud.simple.model.Product;
import cloud.simple.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@ConfigurationProperties
public class MyService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${apigateway.host:localhost}")
    public String apigatewayIp;

    @Value("${apigateway.port:8080}")
    public String apigatewayPort;
    
    @Value("${product.discount:200}")
    public double discount;

    @Autowired
    private HttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Product> searchAll() {
        WebApplication.userAgentInterceptor.headerValue = request.getHeader("user-agent");
        String url = "http://" + apigatewayIp + ":" + apigatewayPort + "/product/product/searchAll";
        logger.info(url);
        Item[] itemlist = restTemplate.getForObject(url, Item[].class);
        logger.info("itemlist is: " + itemlist);
        List<Product> list = new ArrayList<>();
        for (Item item : itemlist) {
            Product product = new Product(item.getId(), item.getName(), item.getPrice(), 200, 0);
            list.add(product);
        }
        return list;
    }

    public User login(User u, Integer workload) {
        System.out.println(u.getName());
        System.out.println(u.getId());
        System.out.println(u.getPassword());
        WebApplication.userAgentInterceptor.headerValue = request.getHeader("user-agent");
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<User> req = new HttpEntity<User>(u, headers);
        String url = "";
        if (null != workload) {
            url = "http://" + apigatewayIp + ":" + apigatewayPort + "/user/user/login?workload=" + workload.toString();
        } else {
            url = "http://" + apigatewayIp + ":" + apigatewayPort + "/user/user/login";
        }

        User result = restTemplate.postForObject(url, req, User.class);
        System.out.println(result);
        return result;
    }

    public Boolean buy(int id, Buy b) {
    	WebApplication.userAgentInterceptor.headerValue = request.getHeader("user-agent");
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<User> req = new HttpEntity<User>(b.getUser(), headers);
        Boolean result = true;
        for (long productid : b.getProductidList()) {
            String url = "http://" + apigatewayIp + ":" + apigatewayPort + "/product/product/buy/" + productid;
            logger.info("buy such url:" + url);
            result &= restTemplate.postForObject(url, req, Boolean.class);
        }

        return result;
    }

    public List<Product> searchByName(String productName) {
        System.out.println(productName);
        WebApplication.userAgentInterceptor.headerValue = request.getHeader("user-agent");
        String url = "http://" + apigatewayIp + ":" + apigatewayPort + "/product/product/search?productName=" + productName;
        Item[] itemlist = restTemplate.getForObject(url, Item[].class);

        List<Product> list = new ArrayList<>();
        for (Item item : itemlist) {
            Product product = new Product(item.getId(), item.getName(), item.getPrice(), 200, 0);
            list.add(product);
        }
        return list;
    }
}
