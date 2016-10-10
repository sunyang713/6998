package coms6998.fall2016.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBNativeBoolean;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Addresses")
public class Address {
	
	private String uuid;
	private String deliveryPointBarcode;
	private String number;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private boolean isDeleted;
	
	
	@DynamoDBAttribute(attributeName="uuid")
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@DynamoDBHashKey(attributeName="dpBarcode")
	public String getDPBarcode() {
		return deliveryPointBarcode;
	}
	public void setDPBarcode(String barcode) {
		this.deliveryPointBarcode = barcode;
	}
	
	@DynamoDBAttribute(attributeName="number")
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@DynamoDBAttribute(attributeName="street")
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	@DynamoDBAttribute(attributeName="city")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	@DynamoDBAttribute(attributeName="state")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@DynamoDBAttribute(attributeName="zipCode")
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	@DynamoDBNativeBoolean
	@DynamoDBAttribute(attributeName="isDeleted")
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	


}
