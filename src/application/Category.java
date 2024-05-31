package application;

public class Category {
	
	 private  String catid;
     private  String categoryName;
     
     
     
     
	public Category(String catid, String categoryName) {
		this.catid = catid;
		this.categoryName = categoryName;
	}
	
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
     

}
