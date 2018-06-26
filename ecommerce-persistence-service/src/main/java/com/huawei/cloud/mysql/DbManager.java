package com.huawei.cloud.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.huawei.cloud.entity.Item;
import com.huawei.cloud.entity.User;


@Service
public class DbManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${mysql.host}")
    String dbhost;
    @Value("${mysql.port}")
    String dbport;
    @Value("${mysql.db}")
    String dbname;
    @Value("${mysql.user}")
    String dbuser;
    @Value("${mysql.pass}")
    String dbpwd;
    @Value("${mysql.periodicTaskInterval:25000}")
    long periodicTaskInterval;
    
    @Autowired
    private RedisTemplate redisTemplate;

    final String query = "SELECT * FROM product_table";
    final String insertProduct = " insert into product_table (id,name, price)  values (?, ?, ?)";
    final String insertProductWithError = " insert into product_table1 (id,name, price)  values (?, ?, ?)";
    final String insertUser = " insert into user_table (id,name)  values (?, ?)";
    final String insertUserWithEror = " insert into user_table1 (id,name)  values (?, ?)";
    final String select = "SELECT * FROM product_table WHERE id=?";
    final String findProducts = "SELECT * FROM product_table WHERE name like ?";
    final String findProductById = "SELECT * FROM product_table WHERE id= ?";
    final String findAllProducts = "SELECT * FROM product_table";
    final String findUsers = "SELECT * FROM user_table WHERE name=? and password=?";
    final String selectWithError = "SELECT * FROM product_table1 WHERE id=?";
    final String selectCount = "SELECT count(*) FROM product_table";
    final String insertPayment = "INSERT INTO `payment_table` (`userid`, `productid`) VALUES (?, ?)";
    final String insertPaymentWrong = "INSERT INTO `payment_table` （`userid`, `productid`） VALUES [?, ?]";
    boolean isPersistentceError = false;
    private AtomicInteger counter = new AtomicInteger(0);
    private Timer timer = new Timer();

    public static int errorNum = 0;
    
    public DbManager() throws InstantiationException, IllegalAccessException, ClassNotFoundException {


        logger.info("DbManager created");

        // And From your main() method or any other method

    }

    @PostConstruct
    public void schedulePeriodicTask() {

//        timer.schedule(new ExecuteSqlCount(dbhost, dbuser, dbpwd), 0, 1000);
//        logger.info("scheduled PeriodicTask every 25 sec");
    }

    public void printTable() {
        logger.info("printTable");
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            // create our mysql database connection

            String myUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;

            conn = DriverManager.getConnection(myUrl, dbuser, dbpwd);

            // create the java statement
            st = conn.createStatement();

            // execute the query, and get a java resultset
            rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                // print the results
                System.out.format("%s, %s\n", id, name);
            }

        } catch (Exception e) {
            logger.error("Got an exception! ");
            logger.error(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (st != null)
                    st.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public boolean addItem(Item item) {
        Connection conn = null;
        PreparedStatement preparedStmt = null;
        boolean answer = true;
        try {
            String myUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;
            conn = DriverManager.getConnection(myUrl, dbuser, dbpwd);
            preparedStmt = (isPersistentceError == true) ? conn.prepareStatement(insertProductWithError)
                    : conn.prepareStatement(insertProduct);
            preparedStmt.setLong(1, item.getId());
            preparedStmt.setString(2, item.getName());
            preparedStmt.setDouble(3, item.getPrice());

            preparedStmt.execute();

        } catch (Exception e) {
            logger.error("Got an exception! --- addItem");
            logger.error(e.getMessage());
            answer = false;
        } finally {
            try {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                if (conn != null)
                    conn.close();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return answer;
    }

    public Item searchItem(long id) throws SQLException {
        Item toReturn = null;
        Connection conn = null;
        PreparedStatement preparedStmt = null;
        try {
            String myUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;

            conn = DriverManager.getConnection(myUrl, dbuser, dbpwd);

            preparedStmt = (isPersistentceError == true) ? conn.prepareStatement(selectWithError)
                    : conn.prepareStatement(select);

            preparedStmt.setLong(1, id);

            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                Long longId = rs.getLong("id");
                Double price = rs.getDouble("price");

                toReturn = new Item(longId, name, price);

            }

        } catch (Exception e) {
            logger.error("Got an exception! ", e);
            logger.error(e.getMessage());
        } finally {
            if (preparedStmt != null)
                preparedStmt.close();

            if (conn != null)
                conn.close();
        }

        return toReturn;

    }

    public void addUser(User newUser) throws Exception {
        Connection conn = null;
        PreparedStatement preparedStmt = null;
        try {
            String myUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;

            conn = DriverManager.getConnection(myUrl, dbuser, dbpwd);
            preparedStmt = (isPersistentceError == true) ? conn.prepareStatement(insertUserWithEror)
                    : conn.prepareStatement(insertUser);
            preparedStmt.setString(1, newUser.getName());
            preparedStmt.setString(2, newUser.getPassword());

            preparedStmt.execute();


        } catch (Exception e) {
            logger.error("Got an exception! --- addItem");
            logger.error(e.getMessage());
            throw e;
        } finally {
            if (preparedStmt != null)
                preparedStmt.close();

            if (conn != null)
                conn.close();
        }
    }

    public void setPersistenceError(Boolean isSet) {

        isPersistentceError = isSet;
    }


    @Async
    public List<Item> findProductsByName(String productName) throws SQLException {
        List<Item> toReturn = new LinkedList<>();
        Connection conn = null;
        PreparedStatement preparedStmt = null;
        try {
            String myUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;
            conn = DriverManager.getConnection(myUrl, dbuser, dbpwd);
            preparedStmt = conn.prepareStatement(findProducts);

            preparedStmt.setString(1, "%" + productName + "%");
            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                Long longId = rs.getLong("id");
                Double price = rs.getDouble("price");

                toReturn.add(new Item(longId, name, price));
            }

        } catch (Exception e) {
            logger.error("Got an exception! ");
            logger.error(e.getMessage());
        } finally {
            if (preparedStmt != null)
                preparedStmt.close();
            if (conn != null)
                conn.close();
        }

        return toReturn;
    }


    @Async
    public List<User> findUsers(User user) throws SQLException {
        List<User> toReturn = new LinkedList<>();
        Connection conn = null;
        PreparedStatement preparedStmt = null;
        try {
            String myUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;
            conn = DriverManager.getConnection(myUrl, dbuser, dbpwd);
            preparedStmt = conn.prepareStatement(findUsers);

            preparedStmt.setString(1, user.getName());
            preparedStmt.setString(2, user.getPassword());
            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");
                toReturn.add(new User(name, password));
            }

        } catch (Exception e) {
            logger.error("Got an exception! ");
            logger.error(e.getMessage());
        } finally {
            if (preparedStmt != null)
                preparedStmt.close();
            if (conn != null)
                conn.close();
        }
        return toReturn;
    }

	public List<Item> findAllProducts() throws SQLException {
        // 从缓存中获取城市信息
        String key = "products";
        ValueOperations<String, List<Item>> operations = redisTemplate.opsForValue();

        // 缓存存在
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
        	List<Item> items = operations.get(key);
        	logger.info("find products from cache!");
            return items;
        }
		
		List<Item> toReturn = new ArrayList<Item>();
        Connection conn = null;
        PreparedStatement preparedStmt = null;
        try {
            String myUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;
            conn = DriverManager.getConnection(myUrl, dbuser, dbpwd);
            preparedStmt = conn.prepareStatement(findAllProducts);

            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
            	Long id = rs.getLong("id");
                String name = rs.getString("name");
                Double password = rs.getDouble("price");
                toReturn.add(new Item(id, name, password));
            }
            logger.info("find products from db!");
            
            // add to cache
            operations.set(key, toReturn, 10, TimeUnit.SECONDS);
            logger.info("add products to cache!");

        } catch (Exception e) {
            logger.error("Got an exception! ");
            logger.error(e.getMessage());
        } finally {
            if (preparedStmt != null)
                preparedStmt.close();
            if (conn != null)
                conn.close();
        }
        return toReturn;
	}

	public List<Item> findProductsById(long id) throws SQLException {
        List<Item> toReturn = new LinkedList<>();
        Connection conn = null;
        PreparedStatement preparedStmt = null;
        try {
            String myUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;
            conn = DriverManager.getConnection(myUrl, dbuser, dbpwd);
            preparedStmt = conn.prepareStatement(findProducts);

            preparedStmt.setLong(1, id);
            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                Long longId = rs.getLong("id");
                Double price = rs.getDouble("price");

                toReturn.add(new Item(longId, name, price));
            }

        } catch (Exception e) {
            logger.error("Got an exception! ");
            logger.error(e.getMessage());
        } finally {
            if (preparedStmt != null)
                preparedStmt.close();
            if (conn != null)
                conn.close();
        }

        return toReturn;
	}

	
	
	public void addPayment(long userId, long productId) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStmt = null;
        try {
            String myUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname;

            conn = DriverManager.getConnection(myUrl, dbuser, dbpwd);
            String sqlstr = "";
            
            logger.info("num is :" + errorNum);
            if(errorNum < 8){
            	sqlstr = insertPayment;
            	errorNum++;
            }else if(errorNum < 10){
				sqlstr = insertPaymentWrong;
				errorNum++;
			}else {
				errorNum = 0;
			}
            preparedStmt = conn.prepareStatement(sqlstr);
            preparedStmt.setLong(1, userId);
            preparedStmt.setLong(2, productId);

            preparedStmt.execute();


        } catch (Exception e) {
            logger.error("Got an exception! --- add payment");
            logger.error(e.getMessage());
            throw e;
        } finally {
            if (preparedStmt != null)
                preparedStmt.close();

            if (conn != null)
                conn.close();
        }
		
	}
}
