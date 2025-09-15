package com.ibm.wfm.beans.tram;

public class TramProjectFinancialsByQuarterDetailItem {
	int year;
	int quarter;
	String mnemonicQuarterByMonth;
	String mnemonicYearByMonth;
	double revenuePlanAmount;
	double costPlanAmount;
	double gpPlanAmount;
	
	public TramProjectFinancialsByQuarterDetailItem(int year, int quarter, String mnemonicQuarterByMonth,
			String mnemonicYearByMonth, double revenuePlanAmount, double costPlanAmount, double gpPlanAmount) {
		super();
		this.year = year;
		this.quarter = quarter;
		this.mnemonicQuarterByMonth = mnemonicQuarterByMonth;
		this.mnemonicYearByMonth = mnemonicYearByMonth;
		this.revenuePlanAmount = revenuePlanAmount;
		this.costPlanAmount = costPlanAmount;
		this.gpPlanAmount = gpPlanAmount;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getQuarter() {
		return quarter;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	public String getMnemonicQuarterByMonth() {
		return mnemonicQuarterByMonth;
	}

	public void setMnemonicQuarterByMonth(String mnemonicQuarterByMonth) {
		this.mnemonicQuarterByMonth = mnemonicQuarterByMonth;
	}

	public String getMnemonicYearByMonth() {
		return mnemonicYearByMonth;
	}

	public void setMnemonicYearByMonth(String mnemonicYearByMonth) {
		this.mnemonicYearByMonth = mnemonicYearByMonth;
	}

	public double getRevenuePlanAmount() {
		return revenuePlanAmount;
	}

	public void setRevenuePlanAmount(double revenuePlanAmount) {
		this.revenuePlanAmount = revenuePlanAmount;
	}

	public double getCostPlanAmount() {
		return costPlanAmount;
	}

	public void setCostPlanAmount(double costPlanAmount) {
		this.costPlanAmount = costPlanAmount;
	}

	public double getGpPlanAmount() {
		return gpPlanAmount;
	}

	public void setGpPlanAmount(double gpPlanAmount) {
		this.gpPlanAmount = gpPlanAmount;
	}

	@Override
	public String toString() {
		return "TramProjectFinancialsByQuarterDetailItem [year=" + year + ", quarter=" + quarter
				+ ", mnemonicQuarterByMonth=" + mnemonicQuarterByMonth + ", mnemonicYearByMonth=" + mnemonicYearByMonth
				+ ", revenuePlanAmount=" + revenuePlanAmount + ", costPlanAmount=" + costPlanAmount + ", gpPlanAmount="
				+ gpPlanAmount + "]";
	}
	

}
