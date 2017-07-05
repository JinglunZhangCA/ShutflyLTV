import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
/**
 * @author Jinglun Zhang
 * 
 * ShutflyTest is a class to test ShutflyLTV class with the input files. The input file is a text file with a string steam in JSON format as shown in the events.txt 
 * in Sample_input fold.
 * 
 * The usage is:
 * 
 * Java ShutflyTest inputFileName
 * 
 * Where inputFileName is the input file name in the input fold, it is txt file type and the whole file name is inputFileName.txt, the test output will have the 
 * same file name in the output fold. For example, if you issue a command:
 * 
 * Java ShutflyTest test1
 * 
 * There is an input\test1.txt file before testing and it generates output\test1.txt. You can also issue a command with multiple inpurt file like:
 * 
 * Java ShutflyTest test1 test2 test3 test4
 * 
 * It generates output\test1.txt, too. 
 * 
 * Please use interfaces Customer and DataPool for some operations. You can initial a DataPool by calling ShutflyLTV.createDataPool()
 */

public class ShutflyTest {

	private static final MyDataPool D = null;

	public static void main(String[] arg){
	
		if (arg==null || arg.length==0){
			System.out.println(sConstants.NO_INPUT);
			return;
		}
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        
        JSONParser parser = new JSONParser();
        FileWriter logF = null, output ;
    	DataPool D = ShutflyLTV.createDataPool();
    	String nl = System.getProperty(sConstants.NEW_LINE);

    	
		for (int i=0; i<arg.length; i++) //handle multiple files one by one
		{
	        
	        try {
	        	if (i==0) {
	        		if (sConstants.DO_LOGS){
	        			logF = new FileWriter(".\\"+sConstants.OUTPUT+"\\"+arg[i]+sConstants.DOTLOG);
	        			logF.write(sdf.format(cal.getTime())+ ": " + sConstants.START_PROCESSING + arg[i]+nl);
	        		}
	        	}
	        	JSONArray objs = (JSONArray)parser.parse(new FileReader(".\\"+sConstants.INPUT+"\\"+arg[i]+sConstants.DOTTXT));
		        Iterator itr=  objs.iterator();

		        while(itr.hasNext()){
		        	JSONObject jsonObject = (JSONObject)itr.next();
		        	//handle tags, to make it as a String
		            JSONArray tags = (JSONArray) jsonObject.get(sConstants.TAGS);
		            if (tags != null){
		            	jsonObject.put(sConstants.TAGS, (Object) tags.toString()); //to unify the processing late, all in a String
		            }
		        	
		        	@SuppressWarnings("unchecked")
					Map<String,Object> map = (Map<String,Object>) jsonObject;		        	
		        	int ret = ShutflyLTV.ingest(map, D);
		        	if (ret!=sConstants.SUCCESS){
		        		//log error
		        		if (sConstants.DO_LOGS && logF != null){
		        			logF.write(sdf.format(cal.getTime())+ ": " + sConstants.ERROR + ret +", "+ sConstants.ERROR_MSG[ret]+" - " + map.toString()+nl);
		        		}
		        	} else
		        		if (sConstants.DO_LOGS && logF != null){
		        			logF.write(sdf.format(cal.getTime())+ ": " + sConstants.SUCCESS_PROCESSING + " " + map.toString()+nl);
		        		}
		        }

	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
				if (sConstants.DO_LOGS && logF != null){
					try {
						logF.write(sdf.format(cal.getTime()) + ": " + sConstants.ERROR + e.getErrorType() +", " + e.getMessage()+nl);
						//logF.close();
			        } catch (IOException ee) {
			            ee.printStackTrace();
			        }
				}
	        }

	    }
		
		try {
			if (sConstants.DO_LOGS && logF != null){
				logF.write(sdf.format(cal.getTime()) + ": " + sConstants.SUCCESS_PROCESSING+nl);
			}
        } catch (IOException e) {
            e.printStackTrace();

		}
		
		//testing  Customer[] topXSimpleLTVCustomers(int x, MyDataPool D) 
		int cNum = D.getCustomer().size();
		int n1 = cNum/3;
		if (n1==0)
			n1=1;
		int n2 = cNum+1;
		
		Customer[] c1 = ShutflyLTV.topXSimpleLTVCustomers(n1, D);
		String time = sdf.format(cal.getTime());
		int ret = printResult(time, c1, arg[0], n1, D, logF); 
		//since n2 is > total customer number, it will get all customers with LTV
		Customer[] c2 = ShutflyLTV.topXSimpleLTVCustomers(n2, D);
		time = sdf.format(cal.getTime());
		ret = printResult(time, c2, arg[0], n2, D, logF); 
		
		try {
			logF.flush();
			logF.close();
	    } catch (IOException e) {
	        e.printStackTrace();
		}
	       
	}
	
	public static int printResult(String time, Customer[] c, String filename, int num, DataPool D, FileWriter logF) {
    	String nl = System.getProperty(sConstants.NEW_LINE);
		try {
			if (sConstants.DO_LOGS && logF != null){
				if (c != null)
					logF.write(time + ": " + sConstants.SUCCESS_PROCESSING + " ShutflyLTV.topXSimpleLTVCustomers("+num+") "+nl);
				else
					logF.write(time + ": " + sConstants.ERROR + " ShutflyLTV.topXSimpleLTVCustomers("+num+") "+nl);
				if (c != null){
					for (int i=0; i<c.length; i++)
						logF.write(time + ": " + c[i].getID() + ", " + c[i].getLTV()+ ", " + c[i].getLastName()+ ", " + c[i].getCity()+ ", " + c[i].getState()+ nl);
				}
					
			}
			
	        JSONArray jsonObjectArray = new JSONArray();
        	FileWriter output = new FileWriter(".\\"+sConstants.OUTPUT+"\\"+filename+"_"+num+sConstants.DOTTXT);
			if (output != null){				
				if (c != null){
					JSONObject js;
					for (int i=0; i<c.length; i++){
						Map<String, String> m = new HashMap<String, String>();
						String str = c[i].getID();
						m.put(sConstants.CUSTOMER_ID, str);
						str = String.valueOf(c[i].getLTV());
						m.put(sConstants.LIFE_TIME_VALUE, str);
						str = String.valueOf(c[i].getWKAvgSpending());
						m.put(sConstants.WEEK_SPENDING, str);
						str = String.valueOf(c[i].getWKAvgVisits());
						m.put(sConstants.WEEK_VISITS, str);
						//m.put(sConstants.FIRST_VISIT, String.valueOf(c[i].getFirst()));
						//m.put(sConstants.LAST_VISIT, String.valueOf(c[i].getLast()));
						js = new JSONObject(m);
						jsonObjectArray.add((Object) js);
					} 
					
					jsonObjectArray.writeJSONString(output);
				}
				output.flush();
				output.close();
			}
			
			return sConstants.SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            return sConstants.OUTPUT_ERROR;
		}

	}
	
	/*
    public static Map<String, String> toMap(JSONObject object) throws ParseException {
        Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }
    */

}