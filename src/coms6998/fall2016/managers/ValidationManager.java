package coms6998.fall2016.managers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.lambda.runtime.Context;

import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.Address;

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
	
	public boolean isLettersOnly(String input){
		String cleaned = input.replaceAll("\\s+","");
		boolean isValid = cleaned.chars().allMatch(x -> Character.isLetter(x));
		if (!isValid) {
			context.getLogger().log("Invalid letters-only entry!");
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
	
	public boolean isValidZipCode(String zc){
		boolean isDigits = zc.chars().allMatch(x -> Character.isDigit(x));
		boolean isValid = isDigits && (zc.length() == 5);
		if (!isValid){
			context.getLogger().log("Invalid zip code!");
		}
		return isValid;
	}
	
	public boolean isValidCustomer(Customer customer){
        return isValidEmail(customer.getEmail()) && 
        		isLettersOnly(customer.getFirstName()) &&
        		isLettersOnly(customer.getLastName()) &&
        		isValidPhoneNumber(customer.getPhoneNumber());
	}
	
	public boolean isValidAddress(Address address){
		return isValidZipCode(address.getZipCode()) && 
				isLettersOnly(address.getCity()) && 
				isLettersOnly(address.getState());
		
	}


}
