package com.ibm.wfm.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty; 
 
/*
 * @Author : Ricardo Gellman Prado - rgellman@br.ibm.com
 * 
 * User model that match to Full Token provided by W3ID 
 * Including BlueGroups
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class W3UserDetail implements Serializable {

	private static final long serialVersionUID = 1L; 
  
	private String uniqueSecurityName;
    private String sub;
    private String name;
    @JsonProperty("preferred_username")
    private String preferredUsername;
    private String nonce;
    private String acr;
    @JsonProperty("rt_hash")
    private String rtHash;
    @JsonProperty("at_hash")
    private String atHash;
    private String realmName;
    private String displayName;
    private String userType;
    private String iss;
    private String aud;
    private String exp; 
    private String iat; 
    private String emailAddress;
    private String lastName;  
    private List<String> blueGroups;
    private String cn; 
    private String dn; 
    private String uid;
    private String firstName; 
    private String jti;
    
    public W3UserDetail() {}

	public String getUniqueSecurityName() {
		return uniqueSecurityName;
	}

	public void setUniqueSecurityName(String uniqueSecurityName) {
		this.uniqueSecurityName = uniqueSecurityName;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPreferredUsername() {
		return preferredUsername;
	}

	public void setPreferredUsername(String preferredUsername) {
		this.preferredUsername = preferredUsername;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getAcr() {
		return acr;
	}

	public void setAcr(String acr) {
		this.acr = acr;
	}

	public String getRtHash() {
		return rtHash;
	}

	public void setRtHash(String rtHash) {
		this.rtHash = rtHash;
	}

	public String getAtHash() {
		return atHash;
	}

	public void setAtHash(String atHash) {
		this.atHash = atHash;
	}

	public String getRealmName() {
		return realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getIss() {
		return iss;
	}

	public void setIss(String iss) {
		this.iss = iss;
	}

	public String getAud() {
		return aud;
	}

	public void setAud(String aud) {
		this.aud = aud;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getIat() {
		return iat;
	}

	public void setIat(String iat) {
		this.iat = iat;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<String> getBlueGroups() {
		return blueGroups;
	}

	public void setBlueGroups(List<String> blueGroups) {
		this.blueGroups = blueGroups;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public boolean hasBluegroup(String blueGroupNm) {
		return this.blueGroups.contains(blueGroupNm);
	}

	@Override
	public String toString() {
		return "W3UserIdDetails [uniqueSecurityName=" + uniqueSecurityName + ", sub=" + sub + ", name=" + name
				+ ", preferredUsername=" + preferredUsername + ", nonce=" + nonce + ", acr=" + acr + ", rtHash="
				+ rtHash + ", atHash=" + atHash + ", realmName=" + realmName + ", displayName=" + displayName
				+ ", userType=" + userType + ", iss=" + iss + ", aud=" + aud + ", exp=" + exp + ", iat=" + iat
				+ ", emailAddress=" + emailAddress + ", lastName=" + lastName + ", blueGroups=" + blueGroups + ", cn="
				+ cn + ", dn=" + dn + ", uid=" + uid + ", firstName=" + firstName + ", jti=" + jti + "]";
	}

}
