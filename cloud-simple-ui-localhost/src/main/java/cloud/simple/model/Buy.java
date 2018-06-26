package cloud.simple.model;

import java.util.List;

public class Buy {
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Long> getProductidList() {
		return productidList;
	}
	public void setProductidList(List<Long> productidList) {
		this.productidList = productidList;
	}
	private User user;
	private List<Long> productidList;
}
