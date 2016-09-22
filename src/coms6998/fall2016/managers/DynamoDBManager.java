package coms6998.fall2016.managers;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.Customer;

public class DynamoDBManager {
	
	private AmazonDynamoDBClient dynamoDBClient;
	private DynamoDBMapper mapper;
	
	public DynamoDBManager() {
		dynamoDBClient = new AmazonDynamoDBClient();
		dynamoDBClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		mapper = new DynamoDBMapper(dynamoDBClient);
	}
	
	public boolean deleteCustomer(Customer emailKey){
		Customer customerToDelete = mapper.load(emailKey);
		if(customerToDelete != null) {
			customerToDelete.setDeleted(true);
			mapper.save(customerToDelete);
			return true;
		}
		return false;
	}
	
	public boolean deleteAddress(Address address){
		Address addressToDelete = mapper.load(address);
		if(addressToDelete != null) {
			addressToDelete.setDeleted(true);
			mapper.save(addressToDelete);
			return true;
		}
		return false;
	}

}
