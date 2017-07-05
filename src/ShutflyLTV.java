import java.util.*;
import java.text.*;

 
/**
 * @author Jinglun Zhang
 * 
 * Core class to store event data and get LTV
 * 
 */

public class ShutflyLTV {

	private static boolean validCustomerID(String customerID){
		//TODO, validate customerID length and if it consists of letters "0-9" and "a-f"
		return true;
	}
	
	private static double convertCurrency(double damt, String sCurrency){
		//TODO: based on currency type sCurrency, to convert it to USD
		return damt;
	}
	
	private static double getAmt(String s){
		double damt = 0.0;
		String[] str = s.split(" ");
		try {
			damt = Double.parseDouble(str[0]);
		}
		catch (NumberFormatException err){
			return sConstants.WRONG_TOTAL_AMOUNT;
		}
		
		if (str.length>1 && !str[1].equalsIgnoreCase(sConstants.USD))
			damt = convertCurrency(damt,str[1]); 		//TODO: convert currency
		return damt;
	}

	
	/**
	 * To store an event data e into DataPool D. When change Customer information, load an image, or make an order, it is also a site_visit.
	 * 
	 * @param e a map of event data. In Map, there is a key "Type" which determines the event and its data. The Type and its associated keys as below:
				type *
					CUSTOMER
				Additional Data
					key(customer_id) *
					event_time *
					last_name
					adr_city
					adr_state
				type *
					SITE_VISIT
				Additional Data
					key(page_id) *
					event_time *
					customer_id *
					tags (array of name/value properties)
				type *
					IMAGE
				Additional Data
					key(image_id) *
					event_time *
					customer_id *
					camera_make
					camera_model
				type *
					ORDER
				Additional Data
					key(order_id) *
					event_time *
					customer_id *
					total_amount *
				* represents required data
	 * @param D DataPool, an object of all Data
	 * @return    0: ingest performed successful
	 * 			 -1: customerID missing
	 * 			 -2: event_time missing
	 * 			 -3: event_time not correctly formatted
	 * 			 -4: customer with customerID existing
	 * 
	 * 			-98: no map data
	 * 			-99: type is not recognized
	 * 
	 */


	public static int ingest(Map<String,Object> e, DataPool D){
		
		if (e==null ||e.size()==0)
			return sConstants.NO_MAP_DATA; 
		
		String type = (String) e.get(sConstants.TYPE);
		if(type == null)
			return sConstants.WRONG_TYPE;
		String customerID;
		if (sConstants.CUSTOMER.equalsIgnoreCase(type))
			customerID= (String)e.get(sConstants.KEY);
		else
			customerID= (String)e.get(sConstants.CUSTOMER_ID);
			
		if (customerID==null ||!validCustomerID(customerID))
			return sConstants.WRONG_CUSTOMERID;
		String event_time = (String)e.get(sConstants.EVENT_TIME);
		if (event_time==null)
			return sConstants.NO_EVENT_TIME;
		Date eventTime;
	    SimpleDateFormat format = new SimpleDateFormat(sConstants.DATE_PATTERN);
	    try {
	    	eventTime = format.parse(event_time);
	    } catch (ParseException err) {
	    	return sConstants.WRONG_DATE_FORMAT;
	    }
		
		String id;
		String tmpS;
		String verb;
		Map<String, String> map = new HashMap<String,String>();
		
		if (type.equalsIgnoreCase(sConstants.CUSTOMER)){
			id = customerID;
			tmpS = (String)e.get(sConstants.ADR_CITY);
			if (tmpS!=null && tmpS.length()>0) 
				map.put(sConstants.ADR_CITY, tmpS);
			tmpS = (String)e.get(sConstants.ADR_STATE);
			if (tmpS!=null && tmpS.length()>0) 
				map.put(sConstants.ADR_STATE, tmpS);
			tmpS = (String)e.get(sConstants.LAST_NAME);
			if (tmpS!=null && tmpS.length()>0) 
				map.put(sConstants.LAST_NAME, tmpS);
			tmpS = (String)e.get(sConstants.VERB);
			if (tmpS==null || tmpS.length()==0 ||!(tmpS.equalsIgnoreCase(sConstants.NEW) ||tmpS.equalsIgnoreCase(sConstants.UPDATE))) 
				return sConstants.WRONG_VERB;
			verb = tmpS;
		}
		else if (type.equalsIgnoreCase(sConstants.ORDER)){
			id = (String)e.get(sConstants.KEY);
			if (id==null || id.length()==0)
				return sConstants.WRONG_KEY;
			tmpS = (String)e.get(sConstants.TOTAL_AMOUNT);
			if (tmpS==null || tmpS.length()==0) 
				return sConstants.NO_TOTAL_AMOUNT;
			double amt = getAmt(tmpS);
			if (amt < 0.0)
				return sConstants.WRONG_TOTAL_AMOUNT;

			map.put(sConstants.TOTAL_AMOUNT, String.valueOf(amt));			
			tmpS = (String)e.get(sConstants.VERB);
			if (tmpS==null || tmpS.length()==0 || !(tmpS.equalsIgnoreCase(sConstants.NEW) ||tmpS.equalsIgnoreCase(sConstants.UPDATE))) 
				return sConstants.WRONG_VERB;
			verb = tmpS;
		}
		else if (type.equalsIgnoreCase(sConstants.IMAGE)){
			id = (String)e.get(sConstants.KEY);
			if (id==null || id.length()==0)
				return sConstants.WRONG_KEY;
			tmpS = (String)e.get(sConstants.CAMERA_MAKE);
			if (tmpS!=null && tmpS.length()>0) 
				map.put(sConstants.CAMERA_MAKE, tmpS);
			tmpS = (String)e.get(sConstants.CAMERA_MODEL);
			if (tmpS!=null && tmpS.length()>0) 
				map.put(sConstants.CAMERA_MODEL, tmpS);
			tmpS = (String)e.get(sConstants.VERB);
			if (tmpS==null || tmpS.length()==0 || !tmpS.equalsIgnoreCase(sConstants.UPLOAD))
				return sConstants.WRONG_VERB;
			verb = tmpS;
		}
		else if (type.equalsIgnoreCase(sConstants.SITE_VISIT)){
			id = (String)e.get(sConstants.KEY);
			if (id==null || id.length()==0)
				return sConstants.WRONG_KEY;
			tmpS = (String)e.get(sConstants.TAGS);
			if (tmpS!=null && tmpS.length()>0) 
				map.put(sConstants.TAGS, tmpS);   //TODO: we may parse tags to form a Map			
			tmpS = (String)e.get(sConstants.VERB);
			if (tmpS==null || tmpS.length()==0 || !tmpS.equalsIgnoreCase(sConstants.NEW))
				return sConstants.WRONG_VERB;
			verb = tmpS;
		}
		else 
			return sConstants.WRONG_TYPE;
		
		int ret;
		if (verb.equalsIgnoreCase(sConstants.UPDATE))
			ret=D.updateEntry(customerID, eventTime, id, type, map);
		else 
			ret=D.addEntry(customerID, eventTime, id, type, map);
		
		return ret;
	}
	
	
	/**
	 * 
	 * the top x customers with the highest Simple Lifetime Value from data D
	 * 
	 * @param x int cusotmer number
	 * @param D DataPool Data Pool ingested from DW
	 * @return an array of  Customer objects whose number is less equal to x
	 * 
	 * 	Please note that the timeframe for this calculation should come from D. That is, use the data that was ingested into D to
	calculate the LTV to frame the start and end dates of your LTV calculation. You should not be using external data (in particular
	"now") for this calculation.

	 */
	public  static Customer[] topXSimpleLTVCustomers(int x, DataPool D) {
		
		if (x<=0)
			return null; //error
		
		TreeMap<Double,Customer> topLtv = new TreeMap<Double,Customer> ((Collections.reverseOrder()));
		Date start = D.getStart();
		Date end = D.getEnd();
	    for(Map.Entry<String,Customer> entry : D.getMap().entrySet()) {
	    	Double ltv = entry.getValue().getLTV(start, end);
	    	topLtv.put(ltv, entry.getValue());
	    }
	    
	    if (topLtv.size()<x)
	    	x = topLtv.size();
	    if (x==0)
	    	return null;	    
		
		Customer[] customers = new MyCustomer[x];
	    //topLtv.descendingKeySet();
	    int i=0;
	    /*
	    for (i=0;i<x;i++)
	    {
    		customers[i++] = topLtv.firstEntry().getValue();
    		topLtv.remove(topLtv.firstEntry().getKey());    	
	    }
	    */
	    for(Map.Entry<Double,Customer> entry : topLtv.entrySet()) {
	    	if (i<x)
	    		customers[i++] = entry.getValue();
	    }
	    
		
		return customers;
	}

	
	/**
	 * 
	 * Innitial DataPool Object
	 * 
	 */
	public  static DataPool createDataPool() {
		return new MyDataPool();
	}

		


}