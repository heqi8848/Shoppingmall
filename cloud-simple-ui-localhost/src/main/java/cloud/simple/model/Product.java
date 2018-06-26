package cloud.simple.model;

public class Product {
	private long id;
	private String name;
	private double price;
	private double discount;
	private int isOffer;
	


	public Product(long id, String name, double price, double discount,
			int isOffer) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.discount = discount;
		this.isOffer = isOffer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getIsOffer() {
		return isOffer;
	}

	public void setIsOffer(int isOffer) {
		this.isOffer = isOffer;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	
}
