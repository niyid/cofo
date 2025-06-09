package com.vasworks.cofo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class VitalInformation implements Serializable {
	
	public static enum Gender {MALE, FEMALE};
	
	public static enum MaritalStatus {SINGLE, MARRIED, SEPARATED, DIVORCED, WIDOWED};

	/**
	 * 
	 */
	private static final long serialVersionUID = -3258263802927665498L;

	private VitalInformationId id;
	
	private String lastName;
	
	private String firstName;
	
	private String otherNames;
	
	private String maidenName;
	
	private Gender gender;
	
	private MaritalStatus maritalStatus;
	
	private Date birthDate;
	
	private CountryState stateOfOrigin;
	
	private UserTitle userTitle;
	
	private HomeAddress geoAddress;
	
	private ContactInfo contactInfo;
	
	private Country nationality;
	
	private Ethnicity ethicity;
	
	private Occupation occupation;
	
	private String pinHash;
	
	private byte[] hashSalt;
	
	private double accountBalance;
	
	private List<AccountTransaction> accountTransactions;
	
	@EmbeddedId
	public VitalInformationId getId() {
		return id;
	}

	public void setId(VitalInformationId id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(String otherNames) {
		this.otherNames = otherNames;
	}

	public String getMaidenName() {
		return maidenName;
	}

	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@ManyToOne
	public CountryState getStateOfOrigin() {
		return stateOfOrigin;
	}

	public void setStateOfOrigin(CountryState stateOfOrigin) {
		this.stateOfOrigin = stateOfOrigin;
	}

	@ManyToOne
	public UserTitle getUserTitle() {
		return userTitle;
	}

	public void setUserTitle(UserTitle userTitle) {
		this.userTitle = userTitle;
	}

	@ManyToOne
	public HomeAddress getGeoAddress() {
		return geoAddress;
	}

	public void setGeoAddress(HomeAddress geoAddress) {
		this.geoAddress = geoAddress;
	}

	@ManyToOne
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	@ManyToOne
	public Country getNationality() {
		return nationality;
	}

	public void setNationality(Country nationality) {
		this.nationality = nationality;
	}

	@ManyToOne
	public Ethnicity getEthicity() {
		return ethicity;
	}

	public void setEthicity(Ethnicity ethicity) {
		this.ethicity = ethicity;
	}

	@ManyToOne
	public Occupation getOccupation() {
		return occupation;
	}

	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}

	@Lob
	public String getPinHash() {
		return pinHash;
	}

	public void setPinHash(String passwordHash) {
		this.pinHash = passwordHash;
	}

	@Lob
	public byte[] getHashSalt() {
		return hashSalt;
	}

	public void setHashSalt(byte[] hashSalt) {
		this.hashSalt = hashSalt;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	@OneToMany(mappedBy = "accountOwner")
	public List<AccountTransaction> getAccountTransactions() {
		return accountTransactions;
	}

	public void setAccountTransactions(List<AccountTransaction> accountTransactions) {
		this.accountTransactions = accountTransactions;
	}
}
