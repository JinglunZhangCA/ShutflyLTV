import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author Jinglun Zhang
 * 
 * myCustomer implements interface Customer, is to handle Customer object.
 *
 */
public class MyCustomer implements Customer{
	
	private double ltv =0.0;
	private double wkAvgSpending =0.0;
	private double wkAvgVisits = 0;
	
	private String customer_id;
	private Date eventTime;
	private String event_time;
	private String last_name = null;
	private String adr_city = null;
	private String adr_state = null;
	
	Date first = new Date(System.currentTimeMillis());
	Date last = new Date(0);
	TreeMap<Date,Map<String, String>> verbMap;
	

	/**
	 * Construct a new Customer
	 * 
	 * @param cid String customer_id
	 * @param eventTime Date event time
	 * @param map Map<String,String> other data
	 * @param DataPool D store all data
	 */
	 MyCustomer(String cid, Date eventTime, Map<String,String>map) {
		
			customer_id  = cid;
		    //SimpleDateFormat format = new SimpleDateFormat(sConstants.DATE_PATTERN);
		    if (eventTime.before(first))
		    	first = eventTime;
		    if (eventTime.after(last))
		    	last = eventTime;
			
			verbMap = new TreeMap<Date,Map<String,String>>();
			if (map != null){
				adr_city = map.get(sConstants.ADR_CITY);
				adr_state = map.get(sConstants.ADR_STATE);
				last_name = map.get(sConstants.LAST_NAME);
				
				Map<String,String> tmap = new HashMap<String,String>();
				tmap.put(sConstants.TYPE, sConstants.CUSTOMER);
				verbMap.put(eventTime, tmap);	
			}
	}
	
	/**
	 * Construct a new Customer
	 * 
	 * @param eventTime Date event time
	 * @param type String verb type
	 * @param map Map<String,String> other data
	 */
	public void update(Date eventTime, String key, String type,  Map<String,String>map) {
		
		    if (eventTime.compareTo(first)<0)
		    	first = eventTime;
		    if (eventTime.compareTo(last)>0)
		    	last = eventTime;
			
			if (map==null)
				map = new HashMap<String, String>();
			if(sConstants.CUSTOMER.equalsIgnoreCase(type)) {
				adr_city = map.get(sConstants.ADR_CITY);
				adr_state = map.get(sConstants.ADR_STATE);
				last_name = map.get(sConstants.LAST_NAME);
			}
			if (verbMap == null)
				verbMap = new TreeMap<Date,Map<String,String>>();
			map.put(sConstants.TYPE, type);
			verbMap.put(eventTime, map);	
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
	 * 
	 */
	public int newCustomer(Map<String, String> customerMap) {
		
		customer_id  = customerMap.get(sConstants.CUSTOMER_ID);
		if (customer_id==null || customer_id.length() == 0)
			return sConstants.WRONG_CUSTOMERID;
		event_time = customerMap.get(sConstants.CUSTOMER_ID);
		if (event_time==null)
			return sConstants.NO_EVENT_TIME;
	    SimpleDateFormat format = new SimpleDateFormat(sConstants.DATE_PATTERN);
	    try {
	    	eventTime = format.parse(event_time);
	    } catch (ParseException err) {
	    	return sConstants.WRONG_DATE_FORMAT;
	    }
	    if (eventTime.compareTo(first)<0)
	    	first = eventTime;
	    if (eventTime.compareTo(last)>0)
	    	last = eventTime;
		
		adr_city = customerMap.get(sConstants.ADR_CITY);
		adr_state = customerMap.get(sConstants.ADR_STATE);
		last_name = customerMap.get(sConstants.LAST_NAME);
		
		return sConstants.SUCCESS;
	}
	

	
	
	/**
	 * 
	 * @param ltv double life time value
	 */
	public void setLTV(double ltv){
		this.ltv = ltv;
	}


	/**
	 * 
	 * @param start Date start time
	 * 
	 * @param end Date end time
	 * @return ltv double
	 */
	public double getLTV(Date start, Date end){
		int weeks =0;
		this.wkAvgVisits = 0;
		
	    Date start1 = resetTime(start);
	    Date end1 = resetTime(end);

	    Calendar cal = new GregorianCalendar();
	    cal.setTime(start1);
	    while (cal.getTime().before(end)) {
	        // add another week
	        cal.add(Calendar.WEEK_OF_YEAR, 1);
	        weeks++;
	    }
		
	    if (weeks == 0) return 0;
	    
	    double t_ord = 0;
	    for(Map.Entry<Date,Map<String,String>> entry : verbMap.entrySet()) {
	    	Map<String,String> m = entry.getValue();
	    	if (sConstants.ORDER.equalsIgnoreCase(m.get(sConstants.TYPE))){
	    		String ord = m.get(sConstants.TOTAL_AMOUNT);
	    		if (ord != null)
	    			t_ord += Double.parseDouble(ord);
	    	}
	    }
	    
		ltv =0.0;
		wkAvgSpending = (double) t_ord/weeks;
		wkAvgVisits = (double) verbMap.size()/weeks;
		ltv = 52*wkAvgSpending*sConstants.YEARS;
		return ltv;
	}

	public static Date resetTime (Date d) {
	    Calendar cal = new GregorianCalendar();
	    cal.setTime(d);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
	
	
	/**
	 * 
	 * @return String last_name 
	 */
	public String getLastName(){
		return last_name;
	}
	
	/**
	 * 
	 * @return String adr_city
	 */
	public String getCity(){
		return adr_city;
	}
	
	/**
	 * 
	 * @return String adr_state
	 */
	public String getState(){
		return adr_state;
	}
	
	/**
	 * 
	 * @return int Weekly average visits
	 */
	public double getWKAvgVisits(){
		return wkAvgVisits;
	}



	
	/**
	 * 
	 * @return double wkAvgSpending
	 */
	public double getWKAvgSpending(){
		return wkAvgSpending;
	}
	
	
	/**
	 * 
	 * @return String customer ID
	 */
	public String getID(){
		return customer_id;
	}

	/**
	 * 
	 * @return double LTV
	 */
	public double getLTV(){
		return ltv;
	}
	
	/**
	 * 
	 * @return Date first time visit
	 */
	public Date getFirst(){
		return first;
	}

	/**
	 * 
	 * @return Date last time visit
	 */
	public Date getLast(){
		return last;
	}
}