package coms6998.fall2016.managers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.lambda.runtime.Context;

import coms6998.fall2016.models.Customer;

public class ValidationManager {
	private Context context;
	
	public ValidationManager(Context context){
		this.setContext(context);
	}
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public boolean isValidEmail(String email) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
		boolean isValid = matcher.find();
		if (!isValid) {
			context.getLogger().log("Invalid email!");
		}
        return isValid;
	}
	
	public boolean isValidName(String name){
		boolean isValid = name.chars().allMatch(x -> Character.isLetter(x));
		if (!isValid) {
			context.getLogger().log("Invalid first or last name!");
		}
		return isValid;
	}
	
	public boolean isValidPhoneNumber(String pn){
		boolean isDigits = pn.chars().allMatch(x -> Character.isDigit(x));
		boolean isValid = isDigits && (pn.length() == 10);
		if (!isValid){
			context.getLogger().log("Invalid phone number!");
		}
		return isValid;
	}
	
	public boolean isValidCustomer(Customer customer){
        return isValidEmail(customer.getEmail()) && 
        		isValidName(customer.getFirstName()) &&
        		isValidName(customer.getLastName()) &&
        		isValidPhoneNumber(customer.getPhoneNumber());
	}


}
