package test;
	
public class Item {
	public String upc;
	public String name;
	
	public Item(String upc, String name) {
		this.upc = upc;
		this.name = name;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
