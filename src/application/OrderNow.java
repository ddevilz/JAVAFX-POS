package application;


import java.time.LocalDate;
import java.util.Date;

public class OrderNow {
    private int sno;
    private int onum;
    private LocalDate odate;
    private String otime;
    private String rtype;
    private int cid;
    private String cname;
    private String mobile;
    private int items;
    private double quans;
    private double sub;
    private double disp;
    private double disamt;
    private double tax;
    private double gross;
    private double rof;
    private double net;
    private double ad;
    private String pby;
    private double bal;
    private String dtype;
    private double nop;
    private Date ddate;
    private Date dtime1;
    private String dtime2;
    private String dtime3;
    private String remarks;
    private String status;
    private String company;
    private double cash;
    private double card;
    private double netb;
    private double others;
    private double taxp;
    private String address;
    
    
    public OrderNow() {
    	
	}

    

	@Override
	public String toString() {
		return "OrderNow [sno=" + sno + ", onum=" + onum + ", odate=" + odate + ", otime=" + otime + ", rtype=" + rtype
				+ ", cid=" + cid + ", cname=" + cname + ", mobile=" + mobile + ", items=" + items + ", quans=" + quans
				+ ", sub=" + sub + ", disp=" + disp + ", disamt=" + disamt + ", tax=" + tax + ", gross=" + gross
				+ ", rof=" + rof + ", net=" + net + ", ad=" + ad + ", pby=" + pby + ", bal=" + bal + ", dtype=" + dtype
				+ ", nop=" + nop + ", ddate=" + ddate + ", dtime1=" + dtime1 + ", dtime2=" + dtime2 + ", dtime3="
				+ dtime3 + ", remarks=" + remarks + ", status=" + status + ", company=" + company + ", cash=" + cash
				+ ", card=" + card + ", netb=" + netb + ", others=" + others + ", taxp=" + taxp + "]";
	}



	public OrderNow(int sno, int onum, LocalDate odate, String otime, String rtype, int cid, String cname, String mobile,
			int items, double quans, double sub, double disp, double disamt, double tax, double gross, double rof,
			double net, double ad, String pby, double bal, String dtype, double nop, Date ddate, Date dtime1,
			String dtime2, String dtime3, String remarks, String status, String company, double cash, double card,
			double netb, double others, double taxp, String address) {
		this.sno = sno;
		this.onum = onum;
		this.odate = odate;
		this.otime = otime;
		this.rtype = rtype;
		this.cid = cid;
		this.cname = cname;
		this.mobile = mobile;
		this.items = items;
		this.quans = quans;
		this.sub = sub;
		this.disp = disp;
		this.disamt = disamt;
		this.tax = tax;
		this.gross = gross;
		this.rof = rof;
		this.net = net;
		this.ad = ad;
		this.pby = pby;
		this.bal = bal;
		this.dtype = dtype;
		this.nop = nop;
		this.ddate = ddate;
		this.dtime1 = dtime1;
		this.dtime2 = dtime2;
		this.dtime3 = dtime3;
		this.remarks = remarks;
		this.status = status;
		this.company = company;
		this.cash = cash;
		this.card = card;
		this.netb = netb;
		this.others = others;
		this.taxp = taxp;
		this.address = address;
	}



	public int getSno() {
		return sno;
	}



	public void setSno(int sno) {
		this.sno = sno;
	}



	public int getOnum() {
		return onum;
	}



	public void setOnum(int onum) {
		this.onum = onum;
	}



	public LocalDate getOdate() {
		return odate;
	}



	public void setOdate(LocalDate odate) {
		this.odate = odate;
	}



	public String getOtime() {
		return otime;
	}



	public void setOtime(String otime) {
		this.otime = otime;
	}



	public String getRtype() {
		return rtype;
	}



	public void setRtype(String rtype) {
		this.rtype = rtype;
	}



	public int getCid() {
		return cid;
	}



	public void setCid(int cid) {
		this.cid = cid;
	}



	public String getCname() {
		return cname;
	}



	public void setCname(String cname) {
		this.cname = cname;
	}



	public String getMobile() {
		return mobile;
	}



	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public int getItems() {
		return items;
	}



	public void setItems(int items) {
		this.items = items;
	}



	public double getQuans() {
		return quans;
	}



	public void setQuans(double quans) {
		this.quans = quans;
	}



	public double getSub() {
		return sub;
	}



	public void setSub(double sub) {
		this.sub = sub;
	}



	public double getDisp() {
		return disp;
	}



	public void setDisp(double disp) {
		this.disp = disp;
	}



	public double getDisamt() {
		return disamt;
	}



	public void setDisamt(double disamt) {
		this.disamt = disamt;
	}



	public double getTax() {
		return tax;
	}



	public void setTax(double tax) {
		this.tax = tax;
	}



	public double getGross() {
		return gross;
	}



	public void setGross(double gross) {
		this.gross = gross;
	}



	public double getRof() {
		return rof;
	}



	public void setRof(double rof) {
		this.rof = rof;
	}



	public double getNet() {
		return net;
	}



	public void setNet(double net) {
		this.net = net;
	}



	public double getAd() {
		return ad;
	}



	public void setAd(double ad) {
		this.ad = ad;
	}



	public String getPby() {
		return pby;
	}



	public void setPby(String pby) {
		this.pby = pby;
	}



	public double getBal() {
		return bal;
	}



	public void setBal(double bal) {
		this.bal = bal;
	}



	public String getDtype() {
		return dtype;
	}



	public void setDtype(String dtype) {
		this.dtype = dtype;
	}



	public double getNop() {
		return nop;
	}



	public void setNop(double nop) {
		this.nop = nop;
	}



	public Date getDdate() {
		return ddate;
	}



	public void setDdate(Date ddate) {
		this.ddate = ddate;
	}

	public Date getDtime1() {
		return dtime1;
	}



	public void setDtime1(Date dtime1) {
		this.dtime1 = dtime1;
	}



	public String getDtime2() {
		return dtime2;
	}



	public void setDtime2(String dtime2) {
		this.dtime2 = dtime2;
	}



	public String getDtime3() {
		return dtime3;
	}



	public void setDtime3(String dtime3) {
		this.dtime3 = dtime3;
	}



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getCompany() {
		return company;
	}



	public void setCompany(String company) {
		this.company = company;
	}



	public double getCash() {
		return cash;
	}



	public void setCash(double cash) {
		this.cash = cash;
	}



	public double getCard() {
		return card;
	}



	public void setCard(double card) {
		this.card = card;
	}



	public double getNetb() {
		return netb;
	}



	public void setNetb(double netb) {
		this.netb = netb;
	}



	public double getOthers() {
		return others;
	}



	public void setOthers(double others) {
		this.others = others;
	}



	public double getTaxp() {
		return taxp;
	}



	public void setTaxp(double taxp) {
		this.taxp = taxp;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}
	
    
    
	
    
}