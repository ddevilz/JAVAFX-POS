package application;

public class Service {

    private String serviceId;
    private String name;
    private String type;
    private double rate1;
    private double rate2;
    private double rate3;
    private double rate4;
    private double rate5;
    
	public Service(String serviceId, String name, String type, double rate1, double rate2, double rate3, double rate4,
			double rate5) {
		this.serviceId = serviceId;
		this.name = name;
		this.type = type;
		this.rate1 = rate1;
		this.rate2 = rate2;
		this.rate3 = rate3;
		this.rate4 = rate4;
		this.rate5 = rate5;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getRate1() {
		return rate1;
	}

	public void setRate1(double rate1) {
		this.rate1 = rate1;
	}

	public double getRate2() {
		return rate2;
	}

	public void setRate2(double rate2) {
		this.rate2 = rate2;
	}

	public double getRate3() {
		return rate3;
	}

	public void setRate3(double rate3) {
		this.rate3 = rate3;
	}

	public double getRate4() {
		return rate4;
	}

	public void setRate4(double rate4) {
		this.rate4 = rate4;
	}

	public double getRate5() {
		return rate5;
	}

	public void setRate5(double rate5) {
		this.rate5 = rate5;
	}
	
    
    

}
