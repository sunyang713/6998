package coms6998.fall2016.managers;

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
			//save it to the db
			mapper.save(customer);
			return DBReturnCode.Success;
		}
		System.err.println("Customer already exists.");
		return DBReturnCode.AlreadyExists;
	}

}
