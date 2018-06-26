package cloud.simple.model;

public class Item {
	private long id; // required
	private String name; // required
	private double price; // required

	public Item(Long id, String name, Double price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public Item(String name, double price) {
		super();
		this.name = name;
		this.price = price;
	}

	public Item() {
		super();
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

}
