import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Jinglun Zhang
 * 
 * DataPool is an interface to store all customer, image, site_visit, order data. We view new/update customer, upload an image, make an order, all are a site visiting, too. 
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
public interface DataPool {

	/**
	 * @return HashMap data map
	 */
	public HashMap<String, Customer> getMap ();


	/**
	 * @return Date start date
	 */
	public Date getStart ();
	

	/**
	 * @return Date end date
	 */
	public Date getEnd ();

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
	public int addEntry(String cid, Date eventTime, String key, String type, Map<String,String> map);

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
	public int updateEntry(String cid, Date eventTime, String key, String type, Map<String,String> map);
	
	
	/**
	 * 
	 * get a map with customers from this data pool
	 * 
	 * @return a map for customerData, please see method setCustomer. null if no data
	 * 
	 */
	public Map<String, Customer> getCustomer();
	
	/**
	 * get Customer object with the customerID from Data Pool
	 * 
	 * @param customerID a string of customer ID
	 * @param allData DataPool
	 * @return an object for customer data, please see method setCustomer. null if no data
	 * 
	 */
	public Customer getCustomer(String customerID, DataPool allData);
}