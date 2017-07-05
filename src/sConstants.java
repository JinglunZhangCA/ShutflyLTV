/**
 * @author Jinglun Zhang
 * 
 * Define constants to make globalization 
 * 
 * Some of them needs to change to make the system localization without rebuilding 
 * 
 */


public class sConstants {
	//error codes
	public static final int SUCCESS = 0;
	public static final int WRONG_CUSTOMERID = 1;
	public static final int NO_EVENT_TIME = 2;
	public static final int WRONG_DATE_FORMAT = 3;
	public static final int NO_TOTAL_AMOUNT=4;
	public static final int WRONG_TOTAL_AMOUNT=5;
	public static final int WRONG_KEY=6;
	public static final int WRONG_VERB=7;
	public static final int NOT_EXIST=8;
	public static final int ALREADY_EXIST=9;
	public static final int OUTPUT_ERROR = 10;
	public static final int NO_MAP_DATA = 11;
	public static final int WRONG_TYPE = 12;
	public static final String[] ERROR_MSG = {"OTHER_ERRORS","WRONG_CUSTOMERID","NO_EVENT_TIME","WRONG_DATE_FORMAT","NO_TOTAL_AMOUNT","WRONG_KEY","WRONG_KEY","WRONG_VERB","NOT_EXIST","ALREADY_EXIST",
		"OUTPUT_ERROR","NO_MAP_DATA","WRONG_TYPE"};
	
	public static final int YEARS = 10;
	//TODO: from system control late
	public static final boolean DO_LOGS = true;  //true: making a log for the processing steps and error messages, fales: no logs 
	
	//literals
	//TODO: get these literals from properties so that no needs for re-build L18N
	public static final String INPUT = "input";
	public static final String OUTPUT = "output";
	public static final String DOTLOG = ".log";
	public static final String DOTTXT = ".txt";
	public static final String TYPE = "type";
	public static final String CUSTOMER_ID = "customer_id";	
	public static final String EVENT_TIME = "event_time";	
	public static final String KEY = "key";	
//	public static final String ORDER_ID = "order_id";	
//	public static final String IMAGE_ID = "image_id";	
//	public static final String PAGE_ID = "page_id";	
	public static final String CUSTOMER = "CUSTOMER"; 
	public static final String SITE_VISIT = "SITE_VISIT"; 
	public static final String IMAGE = "IMAGE"; 
	public static final String ORDER = "ORDER"; 
	public static final String LAST_NAME = "last_name"; 
	public static final String ADR_CITY = "adr_city"; 
	public static final String ADR_STATE = "adr_state"; 
	public static final String TAGS = "tags"; 
	public static final String CAMERA_MAKE = "camera_make"; 
	public static final String CAMERA_MODEL = "camera_model"; 
	public static final String TOTAL_AMOUNT = "total_amount"; 
  	public static final String USD = "USD"; 
  	public static final String NEW = "NEW"; 
  	public static final String UPDATE = "UPDATE"; 
  	public static final String UPLOAD = "UPLOAD"; 
  	public static final String VERB = "verb"; 
  	public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  	public static final String NO_INPUT = "No Input File! Usage: Java ShutflyTest input1 input2 ...";
  	public static final String START_PROCESSING = " Start processing ";
  	public static final String SUCCESS_PROCESSING = " successful processed ";
  	public static final String ERROR = " ERROR ";
  	public static final String LIFE_TIME_VALUE = "Life_Time_Value";
  	public static final String WEEK_SPENDING = "Weekly_Average_Spending";
  	public static final String WEEK_VISITS = "Weekly_Average_Visits";
  	public static final String FIRST_VISIT = "Visit_First_Time";
  	public static final String LAST_VISIT = "Visit_Last_Time";
  	public static final String NEW_LINE = "line.separator";
  	
}