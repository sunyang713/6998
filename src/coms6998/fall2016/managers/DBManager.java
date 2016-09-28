package coms6998.fall2016.managers;

import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.Customer;

public interface DBManager {
	
	public boolean deleteCustomer(Customer emailKey);
	
	public boolean deleteAddress(Address address);
	
	public boolean create(Customer customer);

}
