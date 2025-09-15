package com.ibm.wfm.beans.tram;

import java.util.ArrayList;

public class TramProjectFinancialsByQuarter {

	String projectCountryCd;
	String projectNumber;
	String projectDescription;
	String startDate;
	String endDate;
	String customerName;
	double averageRevenueAmt=0.0;
	double averageCostAmt=0.0;
	double averageGpAmt=0.0;
	double averageGpMarginAmt=0.0;
	int status=0;
	ArrayList<TramProjectFinancialsByQuarterDetailItem> items;
	
	public TramProjectFinancialsByQuarter(String projectCountryCd, String projectNumber, String projectDescription,
			String startDate, String endDate, String customerName) {
		super();
		this.projectCountryCd = projectCountryCd;
		this.projectNumber = projectNumber;
		this.projectDescription = projectDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.customerName = customerName;
	}
	
	public String getProjectCountryCd() {
		return projectCountryCd;
	}
	public void setProjectCountryCd(String projectCountryCd) {
		this.projectCountryCd = projectCountryCd;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public ArrayList<TramProjectFinancialsByQuarterDetailItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<TramProjectFinancialsByQuarterDetailItem> items) {
		this.items = items;
	}

	public void addDetailItem(TramProjectFinancialsByQuarterDetailItem detailItem) {
		if (items==null) items = new ArrayList<TramProjectFinancialsByQuarterDetailItem>();
		items.add(detailItem);
	}

	public double getAverageRevenueAmt() {
		return averageRevenueAmt;
	}

	public void setAverageRevenueAmt(double averageRevenueAmt) {
		this.averageRevenueAmt = averageRevenueAmt;
	}

	public double getAverageCostAmt() {
		return averageCostAmt;
	}

	public void setAverageCostAmt(double averageCostAmt) {
		this.averageCostAmt = averageCostAmt;
	}

	public double getAverageGpAmt() {
		return averageGpAmt;
	}

	public void setAverageGpAmt(double averageGpAmt) {
		this.averageGpAmt = averageGpAmt;
	}

	public double getAverageGpMarginAmt() {
		return averageGpMarginAmt;
	}

	public void setAverageGpMarginAmt(double averageGpMarginAmt) {
		this.averageGpMarginAmt = averageGpMarginAmt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TramProjectFinancialsByQuarter [projectCountryCd=" + projectCountryCd + ", projectNumber="
				+ projectNumber + ", projectDescription=" + projectDescription + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", customerName=" + customerName + ", averageRevenueAmt=" + averageRevenueAmt
				+ ", averageCostAmt=" + averageCostAmt + ", averageGpAmt=" + averageGpAmt + ", averageGpMarginAmt="
				+ averageGpMarginAmt + ", status=" + status + ", items=" + items + "]";
	}
	


}
