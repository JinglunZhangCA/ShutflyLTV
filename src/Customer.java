import java.util.*;

/**
 * 
 * @author Jinglun Zhang
 * 
 * This interface is to handle Customer object.
 *
 */
public interface Customer {
	
	/**
	 * set a new Customer data customerMap
	 * 
	 * @param customerMap a map of customer data with keys as below:
	 * 						customer_id *
							event_time *
							last_name
							adr_city
							adr_state					
	 * @return    0: set performed 
	 * 			 -1: customerID missing
	 * 			 -2: event_time missing
	 * 			 -3: event_time not correctly formatted
	 * 			 -4: customer with customerID existing
	 * 
	 */
	public int newCustomer(Map<String, String> customerMap);
	
	
	/**
	 * get a map with customer data
	 * 
	 * @return a map for customerData, please see method setCustomer. null if no data
	 * 
	public Map getCustomer();
	 */
	

	/**
	 * Construct a new Customer
	 * 
	 * @param eventTime Date event time
	 * @param type String verb type
	 * @param map Map<String,String> other data
	 */
	public void update(Date eventTime, String key, String type,  Map<String,String>map);

	/**
	 * 
	 * @param ltv double LTV 
	 */
	
	
	public void setLTV(double ltv);
	
	/**
	 * 
	 * @param start Date start time
	 * 
	 * @param end Date end time
	 * @return ltv double
	 */
	public double getLTV(Date start, Date end);
	
	
	/**
	 * 
	 * @return String last_name 
	 */
	public String getLastName();
	
	/**
	 * 
	 * @return String adr_city
	 */
	public String getCity();
	
	/**
	 * 
	 * @return String adr_state
	 */
	public String getState();
	
	/**
	 * 
	 * @return double Weekly average visits
	 */
	public double getWKAvgVisits();



	
	/**
	 * 
	 * @return double wkAvgSpending
	 */
	public double getWKAvgSpending();
	
	
	/**
	 * 
	 * @return String customer ID
	 */
	public String getID();

	/**
	 * 
	 * @return double LTV
	 */
	public double getLTV();
	
	/**
	 * 
	 * @return Date first time visit
	 */
	public Date getFirst();

	/**
	 * 
	 * @return Date last time visit
	 */
	public Date getLast();


}