import java.util.*;

/**
 * 
 * @author Jinglun Zhang
 * 
 * DataPool class  is to store all customer, image, site_visit, order data. We view new/update customer, upload an image, make an order, all are a site visiting, too. 
 * 
 * DataPool is a map with key:<customer_id, event_time>, and value:<type, key, Map> where Map is a String key and a String value pair, it's formal definition:
 * 
 * 		Map<<String,Date>,<String,String,Map<String,String>>>
 * 
 * and type with values:
				type 
					CUSTOMER
				Additional Data
					last_name
					adr_city
					adr_state
				type 
					SITE_VISIT
				Additional Data
					tags (array of name/value properties)
				type 
					IMAGE
				Additional Data
					camera_make
					camera_model
				type 
					ORDER
				Additional Data
					total_amount *
				* represents required data *
 */
public class MyDataPool implements DataPool{
	//TreeMap<<String,Date>,<String,String, Map<String,String>>> dataPool;
	HashMap<String, Customer> treeData; //key customer_id
	Date first = new Date(System.currentTimeMillis());
	Date last = new Date(0);
	
	
	/**
	 * Construct
	 */
	MyDataPool(){
		treeData = new HashMap<String, Customer>();
	}

	/**
	 * @return HashMap data map
	 */
	public HashMap<String, Customer> getMap (){
		return treeData ;
	}


	/**
	 * @return Date start date
	 */
	public Date getStart (){
		return first ;
	}


	/**
	 * @return Date end date
	 */
	public Date getEnd (){
		return last ;
	}


	/**
	 * Add an entry to the data pool
	 * 
	 * @param cid String customer ID
	 * @param eventTime Date event time
	 * @param key String key (if type is CUSTOMER, it's customer ID
	 * @param type String type
	 * @param map Map<String,String> other data
	 * 
	 */
	public int addEntry(String cid, Date eventTime, String key, String type, Map<String,String> map){
		Customer cust;
		if (!type.equalsIgnoreCase(sConstants.CUSTOMER)){
			if (treeData.get(cid) == null){
				//return sConstants.NOT_EXIST;
				cust = new MyCustomer(cid, eventTime, map);
				treeData.put(cid, cust);
			}
			else {
				cust = treeData.get(cid);
				cust.update(eventTime, key, type, map);
			}
		}
		else {
			if (treeData.get(cid) != null){
				return sConstants.ALREADY_EXIST;
			}
			cust = new MyCustomer(cid, eventTime, map);
			treeData.put(cid, cust);
		}
	    if (eventTime.before(first))
	    	first = eventTime;
	    if (eventTime.after(last))
	    	last = eventTime;

		return sConstants.SUCCESS;
	}

	/**
	 * update an entry to the data pool. the old entry becomes a record of site visit 
	 * 
	 * @param cid String customer ID
	 * @param eventTime Date event time
	 * @param key String key (if type is CUSTOMER, it's customer ID
	 * @param type String type
	 * @param map Map<String,String> other data
	 * 
	 */
	public int updateEntry(String cid, Date eventTime, String key, String type, Map<String,String> map){
		//modify previous entry first to a site_visit
		Customer cust = treeData.get(cid);
		if (cust == null){
		//	if (type.equalsIgnoreCase(sConstants.CUSTOMER))
		//		return sConstants.NOT_EXIST;
			cust = new MyCustomer(cid, eventTime, null); 
		}
		cust.update(eventTime, key, type, map);
	    if (eventTime.before(first))
	    	first = eventTime;
	    if (eventTime.after(last))
	    	last = eventTime;

		return sConstants.SUCCESS;
	}
	/** 
	 * set a new Customer data customerMap
	 * 
	 * @param customerMap a map of customer data with keys as below:
	 * 						customer_id *
							event_time *
							last_name
							adr_city
							adr_state					
	 * @param allData a map of allData
	 * @return    0: set performed 
	 * 			 -1: customerID missing
	 * 			 -2: event_time missing
	 * 			 -3: event_time not correctly formatted
	 * 			 -4: customer with customerID existing
	 * 
	 */
	public int newCustomer(Map customerMap, Map allData) {
		
		return 0;
	}
	
	/**
	 * update a existing Customer data customerMap
	 * 
	 * @param customerMap a map of customer data with keys as below:
	 * 						customer_id *
							event_time *
							last_name
							adr_city
							adr_state
	 * @param allData a map of allData
	 * @return    0: update performed 
	 * 			 -1: customerID missing
	 * 			 -2: event_time missing
	 * 			 -3: event_time not correctly formatted
	 * 			 -4: customer with customerID not existing
	 * 
	 */
	public int updateCustomer(Map customerMap, Map allData){
		
		return 0;
	}
	
	/**
	 * 
	 * get a map with customers from this data pool
	 * 
	 * @return a map for customerData, please see method setCustomer. null if no data
	 * 
	 */
	public Map<String, Customer> getCustomer(){
		return treeData;
	}
	
	/**
	 * get Customer object with the customerID from Data Pool
	 * 
	 * @param customerID a string of customer ID
	 * @param allData DataPool
	 * @return an object for customer data, please see method setCustomer. null if no data
	 * 
	 */	
	
	public Customer getCustomer(String customerID, DataPool allData){
		
		if (allData == null || customerID== null) 
			return null;
		return treeData.get(customerID);
		
	}

}