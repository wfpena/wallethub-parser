package com.ef.utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;

/* @author Wilson Pena
 * Utility class to parse command line arguments
 */
public class CliParser{	
	private HashMap<String, Object> arguments;
	public static SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
	

	public CliParser(){
		
	}
	
	public CliParser(String[] args) throws ParsingCliException, Exception{	
		arguments = parseCommandArgs(args);
	}
	
	
	/**
     * This function parses the command line arguments
     * throws custom Exception in case some arguments are missing
     * or are invalid.
     *
     * @param application arguments
     * @return HashMap with key equals name of the argument and
     * 			value equals its value.
     */
	public HashMap<String, Object> parseCommandArgs(String[] args) throws Exception, ParsingCliException{
		Date startDate = new Date();
		Date endDate = new Date();
		int threshold = 0;
		String duration = null;
		String filePath = null;
		File f;
		
		if(args.length < 4){
			throw new ParsingCliException("Some arguments are missing");
		}
		
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		String[] cmdPair = new String[0];

		for(String cmd : args){
			cmdPair = cmd.split("=");
			
			if(Objects.equals(cmdPair[0], "--startDate")){
				startDate = dt.parse(cmdPair[1]);
			} else if(Objects.equals(cmdPair[0], "--duration")){
				duration = cmdPair[1];
				if(!Objects.equals(duration, "hourly") && !Objects.equals(duration, "daily")){
					throw new ParsingCliException("Invalid duration type: Should be either \"hourly\" or \"daily\".");
				}
			} else if(Objects.equals(cmdPair[0], "--threshold")){
				threshold = Integer.parseInt(cmdPair[1]);
			} else if(Objects.equals(cmdPair[0], "--accesslog")){
				f = new File(cmdPair[1]);
				if(!f.exists() && !f.isDirectory()) { 
				    throw new ParsingCliException("Log file doesn't exist");
				}
				else{
					filePath = cmdPair[1];
				}
				
			}
		}
		
		if(filePath == null){
			throw new ParsingCliException("Path to accesslog not defined");
		}
		
		if(duration == null){
			throw new ParsingCliException("Duration not defined");
		}
		
		if(Objects.equals(duration, "hourly")){
    		endDate = DateUtils.addHours(startDate, 1);
    	} else{
    		endDate = DateUtils.addDays(startDate, 1);
    	}
		
		arguments.put("endDate", endDate);
		arguments.put("startDate", startDate);
		arguments.put("duration", duration);
		arguments.put("threshold", threshold);
		arguments.put("accesslog", filePath);
		
		return arguments;
    }
	
	public Date str2date(String str) throws ParseException {
		return dt.parse(str.substring(0,19).replaceAll("\\s+","."));
	}
	
	public String formatDate(Date date){
		return dt.format(date);
	}
	
	public HashMap<String, Object> getArguments() {
		return arguments;
	}

	public void setArguments(HashMap<String, Object> arguments) {
		this.arguments = arguments;
	}
	
	public void printArguments(){
		System.out.println(Arrays.asList(arguments));
	}
	
	public void printArguments(HashMap<String, Object> args){
		System.out.println(Arrays.asList(args));
	}
}