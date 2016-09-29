package coms6998.fall2016.managers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.DBReturnCode;

public class DynamoDBManager implements DBManager{
	
	private AmazonDynamoDBClient dynamoDBClient;
	private DynamoDBMapper mapper;
	
	public DynamoDBManager() {
		dynamoDBClient = new AmazonDynamoDBClient();
		dynamoDBClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		mapper = new DynamoDBMapper(dynamoDBClient);
	}
	
	public DBReturnCode deleteCustomer(Customer emailKey){
		Customer customerToDelete = mapper.load(emailKey);
		if(customerToDelete != null) {
			customerToDelete.setDeleted(true);
			mapper.save(customerToDelete);
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}
	
	public DBReturnCode deleteAddress(Address address){
		Address addressToDelete = mapper.load(address);
		if(addressToDelete != null) {
			addressToDelete.setDeleted(true);
			mapper.save(addressToDelete);
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}
	
	public DBReturnCode create(Customer customer){
		if (mapper.load(customer) == null) {
			mapper.save(customer);
			return DBReturnCode.Success;
		}
		System.err.println("Customer already exists.");
		return DBReturnCode.AlreadyExists;
	}
	
	public DBReturnCode create(Address address){
		//check if an address exists first with that UUID and if it doesn't, generate it
		String uniqueAddr = address.getNumber() + address.getStreet() + address.getCity();
		String hash = md5(uniqueAddr);
		if (mapper.load(address.getClass(), hash) == null) {
			address.setUuid(hash); 
			mapper.save(address);
			return DBReturnCode.Success;
		}
		System.err.println("Address already exists.");
		return DBReturnCode.AlreadyExists;
	}
	
	//http://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash
	private static String md5(String input) {
	    try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(input.getBytes( "UTF-8" ));
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; i++) {
	            sb.append( String.format( "%02x", array[i]));
	        }
	        return sb.toString();
	    } catch ( NoSuchAlgorithmException | UnsupportedEncodingException e) {
	        return null;            
	    }

	}
	
	public DBReturnCode updateCustomer(Customer customerData) {
		Customer customer = mapper.load(customerData);
		if(customer == null || customer.isDeleted()) {
			return DBReturnCode.NotFound;
		} else {
			mapper.save(customerData);
			return DBReturnCode.Success;
		}
	}
	
	public DBReturnCode updateAddress(Address addressData) {
		String newUniqueAddr = addressData.getNumber() + addressData.getStreet() + addressData.getCity();
		String hash = md5(newUniqueAddr);
		
		Address oldAddr = mapper.load(Address.class, addressData.getUuid());

		if(oldAddr == null || oldAddr.isDeleted()) {
			return DBReturnCode.NotFound;
		} else {
			addressData.setUuid(hash);
			mapper.save(addressData);
			oldAddr.setDeleted(true);
			mapper.save(oldAddr);
			return DBReturnCode.Success;
		}
	}

	public Customer getCustomer(String emailKey) {
		return mapper.load(Customer.class, emailKey);
	}
	
	public DBReturnCode getCus(Customer customer) {
		if (mapper.load(customer) != null) {
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}
	
	public DBReturnCode getAdd(Address address) {
		if (mapper.load(address) != null) {
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}
	
	public Address getAddress(String uuidKey) {
		return mapper.load(Address.class, uuidKey);
	}

}
