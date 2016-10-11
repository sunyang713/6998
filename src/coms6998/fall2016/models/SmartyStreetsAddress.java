package coms6998.fall2016.models;

public class SmartyStreetsAddress {
	
	private int candidate_index;
	private String delivery_point_barcode;
	private String delivery_line_1;
	private String last_line;
	
	private SmartyStreetsAddress(int candidate_index, String delivery_point_barcode, String delivery_line_1, String last_line){
		this.candidate_index = candidate_index;
		this.delivery_point_barcode = delivery_point_barcode;
		this.delivery_line_1 = delivery_line_1;
		this.last_line = last_line;
	}
	
	public String getDPBarcode(){
		return this.delivery_point_barcode;
	}

}
