package com.interset.DataIntegrationTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

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

		//read JSON file data to String
        byte[] jsonData = Files.readAllBytes(jsonFile);
         
        //instantiate ObjectMapper instance
        ObjectMapper objMapper = new ObjectMapper();
        
        //convert the JSON String into POJO Customer Data
        CustomerData custData = objMapper.readValue(jsonData, CustomerData.class);
         
        System.out.println(custData);

		
	}

}
