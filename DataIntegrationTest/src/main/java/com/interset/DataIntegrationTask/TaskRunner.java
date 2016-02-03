package com.interset.DataIntegrationTask;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskRunner {

	public static void main(String args[]) {

		// Check arguments
		if (args.length != 2) {
			System.out.println(
					"We currently only expect 2 arguments! A path to a JSON file to read, and a path for a CSV file to write.");
			System.exit(1);
		}

		// Read arguments
		Path jsonFile = null;

		try {
			jsonFile = Paths.get(args[0]);
		} catch (InvalidPathException e) {
			System.err.println("Couldn't convert JSON file argument [" + args[0] + "] into a path!");
			throw e;
		}

		Path csvFile = null;

		try {
			csvFile = Paths.get(args[1]);
		} catch (InvalidPathException e) {
			System.err.println("Couldn't convert CSV file argument [" + args[1] + "] into a path!");
			throw e;
		}

		// Validate arguments
		if (!Files.exists(jsonFile)) {
			System.err.println("JSON file [" + jsonFile.toString() + "] doesn't exist!");
			System.exit(1);
		}

		if (!Files.isWritable(csvFile.getParent())) {
			System.err.println("Can't write to the directory [" + csvFile.getParent().toString()
					+ "] to create the CSV file! Does directory exist?");
			System.exit(1);
		}

		// Create the CSV file
		System.out.println(
				"Reading file [" + jsonFile.toString() + "], and writing to file [" + csvFile.toString() + "].");

		try {
			parseJsonFileAndCreateCsvFile(jsonFile, csvFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void parseJsonFileAndCreateCsvFile(Path jsonFile, Path csvFile) throws IOException {

		//		JsonNode eventIDNode, timeStampNode, ipAddrNode, userNode, fileNode, activityNode, timeOffsetNode;
		int arrayCounter=0, index;	
		Long csvEventID;
		String csvUser="", csvIpAddr="",csvFileName="", csvFilePath ="", csvAction="", csvTimestamp="", csvTimeOffset="";
		
		//read JSON file data to String
        byte[] jsonData = Files.readAllBytes(jsonFile);
        //instantiate ObjectMapper instance
        ObjectMapper objMapper = new ObjectMapper();
        //convert the JSON String into POJO Customer Data
        CustomerData custData = objMapper.readValue(jsonData, CustomerData.class); 
        
        ArrayList<CustomerData> customers = custData.get("");
        
        //Initialize CSV Formatters and Parsers
     
        //Create the CSVFormat object
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
         
        //initialize the CSVParser object
        CSVParser parser = new CSVParser(new FileReader(csvFile.toFile().toString()), format);
        List<CustomerData> custDataToCsv = new ArrayList<CustomerData>();
        
        //Now we have the Json Objects stored in an Array, in that case we must iterate over each object and store it so that we can export it to a CSV file
        for(CustomerData data: customers){
        	//we will be parsing each object in the metadata file and storing them in a CSV file on the fly
        	csvAction=getCsvActivity(data.getActivity());
        	
        	csvTimestamp=data.getTimestamp();
        	
        	csvFilePath=data.getFile().substring(25);
        	
        	csvFileName=data.getFile().trim().substring(0, 24);
        	
        	csvIpAddr=data.getIpAddr();
        	
        	index=data.getUser().indexOf('@');
        	csvUser=data.getUser().substring(0, index);
        			
        			
        	if(isEventDuplicated(data.getEventID(),customers.get(arrayCounter))){
        		csvEventID=data.getEventID();
        		customers.remove(arrayCounter);       	
        	}
        	arrayCounter++;
        	
            //Now, we will output our data to the CSV File        
        	 for(CSVRecord record : parser){
                 CustomerData emp = new CustomerData();
                 emp.setTimestamp(record.get(csvTimestamp));
                 emp.setActivity(record.get(csvAction));
                 emp.setUser(record.get(csvUser));
                 emp.setFile(record.get(csvFilePath));
                 emp.setFileName(record.get(csvFileName));
                 emp.setIpAddr(record.get(csvIpAddr));
                 custDataToCsv.add(emp);
             }
             
             //close the parser
             parser.close();
        	
        }//enhanced for loop 
        
               
	
	}//parseJsonFileAndCreateCsvFile
	
	
	
	/**
	 * This will evaluate if the JSON data for activity that can mapped to a CSV action such as:
	 * ADD,REMOVE, or ACCESSED
	 * @param activity
	 * @return
	 */
	private static String getCsvActivity(String activity){
		if(activity.equals("createdDoc") || activity.equals("addedText") || activity.equals("changedText"))
			return "ADD";
		
		else if(activity.equals("deletedDoc") || activity.equals("deletedText") || activity.equals("archived"))
			return "REMOVE";
		
		else if (activity.equals("viewDoc"))
			return "ACCESSED";
		
		else{
			return null;
		}
	}
	
	/**
	 * This will evauluate if the same EventID has appeared twice or more so that
	 * we do not include it in the CSV File
	 * @param eventID
	 * @param previousEventID
	 * @return
	 */
	private static boolean isEventDuplicated(Long eventID, CustomerData previousEventID){
		if(eventID.equals(previousEventID.getEventID()))
			return true;
		else
			return false;
	}

}//class
