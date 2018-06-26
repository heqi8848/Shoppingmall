package com.huawei.cloud.api;

import com.huawei.cloud.entity.Item;
import com.huawei.cloud.entity.ServiceTestStatus;
import com.huawei.cloud.entity.User;
import com.huawei.cloud.mysql.DbManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/persistence")
public class PersistanceRestController{
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	public DbManager manager;
    
	
	 @RequestMapping(value = "persistenceError", method = RequestMethod.PUT)
	 public ResponseEntity  setPersistanceError( @RequestParam(value = "isSet") Boolean isSet) {
	    	 try {
	    		 manager.setPersistenceError(isSet);
	    	 }
	    	 catch (Exception e) {
			 	   return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				   }
	          return new ResponseEntity<>(HttpStatus.OK);
	}
	
//	@RequestMapping(value = "user/", method = RequestMethod.POST)
//	public ResponseEntity persistUser(@RequestBody User input) {
//		try {
//        
//			 logger.info(input.toString());
//			manager.addUser(input);
//			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(input.getId())
//					.toUri();
//
//			return ResponseEntity.created(location).build();
//		} catch (Exception e) {
//			 logger.error("persistUser " + e.getMessage());
//		}
//		return ResponseEntity.status(500).build();
//	}

	@RequestMapping(value = "product/{id}", method = RequestMethod.GET)
	public ResponseEntity buyProduct(@PathVariable long id) {
		try {
			 logger.info("buyProduct " + id);
			Item item = manager.searchItem(id);
			logger.info("item is:" + item);
            if(item==null){
            	// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            	   throw  new ResourceNotFoundException();
            }
			return ResponseEntity.ok(item);
		} catch (SQLException e) {
			 logger.error("persistUser " + e.getMessage());
		}
		return ResponseEntity.status(500).build();
	}


	@RequestMapping(value = "products_by_name", method = RequestMethod.GET)
	public ResponseEntity findProductsByName(@RequestParam(value="name") String text ) {
		try {
			logger.info("trying to find product:  " + text);
			List<Item> items = manager.findProductsByName(text);
			if(items==null || items.isEmpty()){
				throw  new ResourceNotFoundException();
			}
			return ResponseEntity.ok(items);
		} catch (SQLException e) {
			logger.error("persistUser " + e.getMessage());
		}
		return ResponseEntity.status(500).build();
	}
	
	@RequestMapping(value = "products", method = RequestMethod.GET)
	public ResponseEntity findProducts() {
		try {
			logger.info("trying to find all products");
			List<Item> items = manager.findAllProducts();
			if(items==null || items.isEmpty()){
				throw  new ResourceNotFoundException();
			}
			return ResponseEntity.ok(items);
		} catch (SQLException e) {
			logger.error("persistUser " + e.getMessage());
		}
		return ResponseEntity.status(500).build();
	}
	
	@RequestMapping(value = "user", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity findUser(@RequestBody User user) {
		try {
			logger.info("trying to find user:  " + user.getName());
			List<User> users = manager.findUsers(user);
			if(users==null || users.isEmpty()){
				logger.info("didn't find user:  " + user.getName());
				throw  new ResourceNotFoundException();
			}
			User rsUser = users.get(0);
			logger.info("find user:  " + user.getName());
			return ResponseEntity.ok(rsUser);
		} catch (SQLException e) {
			logger.error("persistUser " + e.getMessage());
		}
		return ResponseEntity.status(500).build();
	}

	@RequestMapping(value = "payment/{userId}/{productId}", method = RequestMethod.GET)
	public Boolean addCart(@PathVariable("userId") long userId, @PathVariable("productId") long productId) throws SQLException {
		try {
			logger.info(userId + " add " + productId + " to cart.");
			manager.addPayment(userId, productId);
			return true;
		} catch (SQLException e) {
			logger.error("addCart error:" + e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping(value = "status", method = RequestMethod.GET)
	@ResponseBody
	public ServiceTestStatus testService() {
		return new ServiceTestStatus(true, Collections.singletonMap("ProcessIsUp", Boolean.TRUE));
	}
}