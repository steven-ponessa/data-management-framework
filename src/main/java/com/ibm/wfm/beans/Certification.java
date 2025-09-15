package com.ibm.wfm.beans;

import java.sql.Date;
import java.sql.Timestamp;

import com.ibm.wfm.annotations.DbColumn;

public class Certification {
	@DbColumn(columnName = "CERT_ID",isId=true)
	private int certId;
	@DbColumn(columnName = "VENDOR")
	private String vendor;
	@DbColumn(columnName = "THEATRE")
	private String theatre;
	@DbColumn(columnName = "CERTIFICATION_CODE")
	private String certificationCode;
	@DbColumn(columnName = "CERTIFICATE_DESCRIPTION")
	private String certificateDescription;
	@DbColumn(columnName = "CERT_DATE")
	private Date certDate;
	@DbColumn(columnName = "SK_CERT_DATE")
	private int skCertDate;
	@DbColumn(columnName = "EXPIRY_DATE")
	private Date expiryDate;
	@DbColumn(columnName = "SK_EXPIRY_DATE")
	private int skExpiryDate;
	@DbColumn(columnName = "RECERT_DATE")
	private Date recertDate;
	@DbColumn(columnName = "SK_RCERT_DATE")
	private int skRcertDate;
	@DbColumn(columnName = "EXAM_CERT")
	private String examCert;
	@DbColumn(columnName = "QUALIFICATION_TYPE")
	private String qualificationType;
	@DbColumn(columnName = "CERTIFICATION_STATUS")
	private String certificationStatus;
	@DbColumn(columnName = "CERTIFICATION_PROGRAM_STATUS")
	private String certificationProgramStatus;
	@DbColumn(columnName = "DATA_SOURCE")
	private String dataSource;
	@DbColumn(columnName = "DATE_OF_UPLOAD")
	private Date dateOfUpload;
	@DbColumn(columnName = "SK_DATE_OF_UPLOAD")
	private int skDateOfUpload;
	@DbColumn(columnName = "EVIDENCE_UPLOADED")
	private String evidenceUploaded;
	@DbColumn(columnName = "VALIDATE")
	private String validate;
	@DbColumn(columnName = "VALIDATED_BY")
	private String validatedBy;
	@DbColumn(columnName = "DELETE")
	private String delete;
	@DbColumn(columnName = "CURRENT_INDICATOR")
	private int currentIndicator;
	@DbColumn(columnName = "EFFECTIVE_DATE")
	private Date effectiveDate;
	@DbColumn(columnName = "SK_EFFECTIVE_DATE")
	private int skEffectiveDate;
	@DbColumn(columnName = "EXPIRATION_DATE")
	private Date expirationDate;
	@DbColumn(columnName = "SK_EXPIRATION_DATE")
	private int skExpirationDate;
	@DbColumn(columnName = "ETL_TIMESTAMP")
	private Timestamp etlTimestamp;
	@DbColumn(columnName = "AUDIT_TIMESTAMP")
	private Timestamp auditTimestamp;
	@DbColumn(columnName = "VORTEX_INTERNET_EMAIL")
	private String vortexInternetEmail;
	@DbColumn(columnName = "CNUM")
	private String cnum;
	@DbColumn(columnName = "SK_EMPLOYEE")
	private int skEmployee;

	public Certification() {}

	public Certification(int certId, String vendor, String theatre, String certificationCode,
			String certificateDescription, Date certDate, int skCertDate, Date expiryDate, int skExpiryDate,
			Date recertDate, int skRcertDate, String examCert, String qualificationType, String certificationStatus,
			String certificationProgramStatus, String dataSource, Date dateOfUpload, int skDateOfUpload,
			String evidenceUploaded, String validate, String validatedBy, String delete, int currentIndicator,
			Date effectiveDate, int skEffectiveDate, Date expirationDate, int skExpirationDate, Timestamp etlTimestamp,
			Timestamp auditTimestamp, String vortexInternetEmail, String cnum, int skEmployee) {
		super();
		this.certId = certId;
		this.vendor = vendor;
		this.theatre = theatre;
		this.certificationCode = certificationCode;
		this.certificateDescription = certificateDescription;
		this.certDate = certDate;
		this.skCertDate = skCertDate;
		this.expiryDate = expiryDate;
		this.skExpiryDate = skExpiryDate;
		this.recertDate = recertDate;
		this.skRcertDate = skRcertDate;
		this.examCert = examCert;
		this.qualificationType = qualificationType;
		this.certificationStatus = certificationStatus;
		this.certificationProgramStatus = certificationProgramStatus;
		this.dataSource = dataSource;
		this.dateOfUpload = dateOfUpload;
		this.skDateOfUpload = skDateOfUpload;
		this.evidenceUploaded = evidenceUploaded;
		this.validate = validate;
		this.validatedBy = validatedBy;
		this.delete = delete;
		this.currentIndicator = currentIndicator;
		this.effectiveDate = effectiveDate;
		this.skEffectiveDate = skEffectiveDate;
		this.expirationDate = expirationDate;
		this.skExpirationDate = skExpirationDate;
		this.etlTimestamp = etlTimestamp;
		this.auditTimestamp = auditTimestamp;
		this.vortexInternetEmail = vortexInternetEmail;
		this.cnum = cnum;
		this.skEmployee = skEmployee;
	}

	public int getCertId() {
		return certId;
	}

	public void setCertId(int certId) {
		this.certId = certId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getTheatre() {
		return theatre;
	}

	public void setTheatre(String theatre) {
		this.theatre = theatre;
	}

	public String getCertificationCode() {
		return certificationCode;
	}

	public void setCertificationCode(String certificationCode) {
		this.certificationCode = certificationCode;
	}

	public String getCertificateDescription() {
		return certificateDescription;
	}

	public void setCertificateDescription(String certificateDescription) {
		this.certificateDescription = certificateDescription;
	}

	public Date getCertDate() {
		return certDate;
	}

	public void setCertDate(Date certDate) {
		this.certDate = certDate;
	}

	public int getSkCertDate() {
		return skCertDate;
	}

	public void setSkCertDate(int skCertDate) {
		this.skCertDate = skCertDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getSkExpiryDate() {
		return skExpiryDate;
	}

	public void setSkExpiryDate(int skExpiryDate) {
		this.skExpiryDate = skExpiryDate;
	}

	public Date getRecertDate() {
		return recertDate;
	}

	public void setRecertDate(Date recertDate) {
		this.recertDate = recertDate;
	}

	public int getSkRcertDate() {
		return skRcertDate;
	}

	public void setSkRcertDate(int skRcertDate) {
		this.skRcertDate = skRcertDate;
	}

	public String getExamCert() {
		return examCert;
	}

	public void setExamCert(String examCert) {
		this.examCert = examCert;
	}

	public String getQualificationType() {
		return qualificationType;
	}

	public void setQualificationType(String qualificationType) {
		this.qualificationType = qualificationType;
	}

	public String getCertificationStatus() {
		return certificationStatus;
	}

	public void setCertificationStatus(String certificationStatus) {
		this.certificationStatus = certificationStatus;
	}

	public String getCertificationProgramStatus() {
		return certificationProgramStatus;
	}

	public void setCertificationProgramStatus(String certificationProgramStatus) {
		this.certificationProgramStatus = certificationProgramStatus;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public Date getDateOfUpload() {
		return dateOfUpload;
	}

	public void setDateOfUpload(Date dateOfUpload) {
		this.dateOfUpload = dateOfUpload;
	}

	public int getSkDateOfUpload() {
		return skDateOfUpload;
	}

	public void setSkDateOfUpload(int skDateOfUpload) {
		this.skDateOfUpload = skDateOfUpload;
	}

	public String getEvidenceUploaded() {
		return evidenceUploaded;
	}

	public void setEvidenceUploaded(String evidenceUploaded) {
		this.evidenceUploaded = evidenceUploaded;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getValidatedBy() {
		return validatedBy;
	}

	public void setValidatedBy(String validatedBy) {
		this.validatedBy = validatedBy;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public int getCurrentIndicator() {
		return currentIndicator;
	}

	public void setCurrentIndicator(int currentIndicator) {
		this.currentIndicator = currentIndicator;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public int getSkEffectiveDate() {
		return skEffectiveDate;
	}

	public void setSkEffectiveDate(int skEffectiveDate) {
		this.skEffectiveDate = skEffectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getSkExpirationDate() {
		return skExpirationDate;
	}

	public void setSkExpirationDate(int skExpirationDate) {
		this.skExpirationDate = skExpirationDate;
	}

	public Timestamp getEtlTimestamp() {
		return etlTimestamp;
	}

	public void setEtlTimestamp(Timestamp etlTimestamp) {
		this.etlTimestamp = etlTimestamp;
	}

	public Timestamp getAuditTimestamp() {
		return auditTimestamp;
	}

	public void setAuditTimestamp(Timestamp auditTimestamp) {
		this.auditTimestamp = auditTimestamp;
	}

	public String getVortexInternetEmail() {
		return vortexInternetEmail;
	}

	public void setVortexInternetEmail(String vortexInternetEmail) {
		this.vortexInternetEmail = vortexInternetEmail;
	}

	public String getCnum() {
		return cnum;
	}

	public void setCnum(String cnum) {
		this.cnum = cnum;
	}

	public int getSkEmployee() {
		return skEmployee;
	}

	public void setSkEmployee(int skEmployee) {
		this.skEmployee = skEmployee;
	}

}
