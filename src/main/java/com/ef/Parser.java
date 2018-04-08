package com.ef;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Stream;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.ef.dtos.LogDTO;
import com.ef.models.BlockedIps;
import com.ef.models.Log;
import com.ef.utils.CliParser;
import com.ef.utils.ParsingCliException;

/*
 * @author Wilson Pena
 */
public class Parser 
{
	public static Integer i = 0;
	
    public static void main( String[] args )
    {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    	SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		
		print("Starting server log parsing...");
		
    	try {
    		print("Truncating old table...");
    		session.createSQLQuery("truncate table log").executeUpdate();
    		CliParser cli = new CliParser(args);
    		HashMap<String, Object> argMap = cli.getArguments();
    		
    		String fileName = argMap.get("accesslog").toString();
    		print("Inserting new values...\n");
    		Stream<String> stream = Files.lines(Paths.get(fileName));
			stream
				.filter(line -> line != null && !Objects.equals(line, ""))
				.forEach(line -> 
				{
					String[] values = line.split("\\|");
					if(values.length > 0){
						Log log = new Log();
						Date date = null;
						try {
							date = cli.str2date(values[0]);
						} catch (ParseException e) {
							print("Error parsing the date - " + e.getMessage());
						}
						String ip = values[1];
	    				String status = values[3];
	    				String userAgent = values[4]; 
	    				log.setIp(ip);
	    				log.setAccessDate(date);
	    				log.setStatus(status);
	    				log.setUserAgent(userAgent.substring(1, userAgent.length()-1));
	    				session.beginTransaction();
	    				session.save(log);
	    				session.getTransaction().commit();
					}
				});
			
    		List<LogDTO> ips = findIps(session, (Date)argMap.get("startDate"), (Date)argMap.get("endDate"), 
    				Integer.parseInt(argMap.get("threshold").toString()), 
    				argMap.get("duration").toString());
    		
    		ips.forEach(logDto -> 
    		{
    			String comment = "User with ip " + logDto.getIp() + " made " + logDto.getNumOfRequests() + " "
    					+ "requests between " + cli.formatDate((Date)argMap.get("startDate"))
    					+ " and " + cli.formatDate((Date)argMap.get("endDate"));
    			
    			print(comment);
    			
    			BlockedIps bi = new BlockedIps();
    			bi.setComment(comment);
    			session.beginTransaction();
				session.save(bi);
				session.getTransaction().commit();
    		});
    		
    		if(ips.size() == 0){
    			print("No results found.");
    		}
    		
    		if(session.isConnected()){
    			try{
    				session.close();
    			}catch(Exception e){
    				System.out.println("Error closing session: " + e.getMessage());
    			}
    		}
		} catch (Exception e) {
			print("Error: " + e.getMessage());
		}
    	print("\nEnd of process.");
    	System.exit(0);
    }
    
    
	public static List<LogDTO> findIps(Session session, 
			Date startDate, Date endDate, Integer qThreshold, String duration){
    	Query query = session.getNamedQuery("getBetweenDate")
    			.setParameter("startDate", startDate)
    			.setParameter("endDate", endDate)
    			.setParameter("threshold", qThreshold);
    	return query.list();
    }
    
	
    public static void print(String str){
    	System.out.println(str);
    }
    
    
    public static void print(Object str){
    	System.out.println(str.toString());
    }
    
    
    public static void printList(String[] str){
    	for(String s: str){
    		print(s);
    	}
    }
    
    
    /*
     * Function create for testing
     * it doesn't add the values to the database
     * only reads them.
     */
    public void funcForTesting( String[] args )
	{
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		CliParser cli;
		
		try {
			cli = new CliParser(args);
			HashMap<String, Object> argMap = cli.getArguments();
			List<LogDTO> ips = findIps(session, (Date)argMap.get("startDate"), (Date)argMap.get("endDate"), 
					Integer.parseInt(argMap.get("threshold").toString()), 
					argMap.get("duration").toString());
			
			ips.forEach(logDto -> 
			{
				String comment = "User with ip " + logDto.getIp() + " made " + logDto.getNumOfRequests() + " "
						+ "requests between " + cli.formatDate((Date)argMap.get("startDate"))
						+ " and " + cli.formatDate((Date)argMap.get("endDate"));
				
				print(comment);
				
				BlockedIps bi = new BlockedIps();
				bi.setComment(comment);
				bi.setIp(logDto.getIp());
				session.beginTransaction();
				session.save(bi);
				session.getTransaction().commit();
			});
		} catch (ParsingCliException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(session.isConnected()){
			try{
				session.close();
			}catch(Exception e){
				System.out.println("Error closing session: " + e.getMessage());
			}
		}
		System.exit(0);
	}
}

