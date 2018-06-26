package cloud.simple.model;

import java.util.HashMap;
import java.util.Map;


public class UserAutoTestConf {
	private static class ConfHolder{
		private static final UserAutoTestConf INSTANCE = new UserAutoTestConf();
	}
	
	private UserAutoTestConf(){
		mp.put("/user/login", new AutoTestInfo(1,20));
		mp.put("/product/buy", new AutoTestInfo(1,12));
		mp.put("/product/searchAll", new AutoTestInfo(1,2));
		mp.put("/product/search", new AutoTestInfo(0,60));
	}
	
	public static final UserAutoTestConf instance()
	{
		return ConfHolder.INSTANCE;
	}
	
	
	private Map<String, AutoTestInfo> mp = new HashMap();
	
	
	public void setUrlSwitch(AutotestConf conf)
	{
		mp = conf.getConfMap();
	}
	
	public Map<String, AutoTestInfo> getUrlSwitch()
	{
		return mp;
	}
}
