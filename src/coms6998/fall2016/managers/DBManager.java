package coms6998.fall2016.managers;

import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.DBReturnCode;

public interface DBManager {
	
	public DBReturnCode deleteCustomer(Customer emailKey);
	
	public DBReturnCode deleteAddress(Address address);
	
	public DBReturnCode create(Customer customer);
	
	public DBReturnCode create(Address address);

}
