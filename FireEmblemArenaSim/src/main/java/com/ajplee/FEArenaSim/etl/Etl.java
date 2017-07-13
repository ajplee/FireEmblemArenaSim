package com.ajplee.FEArenaSim.etl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.HttpURLConnection;

import com.ajplee.FEArenaSim.etl.heroes.Heroes;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Etl {
	final static Logger logger = LoggerFactory.getLogger("ETL");
	private String jsonText;
	private Heroes[] heroes;
	
	public void run() throws Exception {
		getData();
		//transformData();
		//loadData();
	}
	//https://api.hearthstonejson.com/v1/20022/enUS/cards.json
	//https://kagerochart.com/dist/scripts/damage-calc/char.js
	public void getData() throws Exception {
		HttpURLConnection conn = null;
		try {
			URL FireEmblemHeroesUrl = new URL("https://kagerochart.com/dist/scripts/damage-calc/char.js");
			conn = (HttpURLConnection) FireEmblemHeroesUrl.openConnection();
			conn.setRequestProperty(
			          "User-Agent",
			          "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; .NET CLR 1.2.30703)");
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-length", "0");
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setConnectTimeout(6000);
			conn.setReadTimeout(1500000);
			conn.connect();
			InputStreamReader ir = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(ir);
			StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = br.readLine()) != null) {
	          sb.append(line + "\n");
	        }
	        ir.close();
	        br.close();
	        	
	        jsonText = sb.toString();
	        jsonText = jsonText.substring(13);
		} catch (Exception e) {
			logger.error("Parsing failed" + e.getMessage());
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
        System.out.println(jsonText);
	}
	
	public void transformData() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		try {
			heroes = mapper.readValue(jsonText, Heroes[].class);	
		} catch (Exception e) {
			logger.error("Transforming data failed" + e.getMessage());
		}
	}
	
	public void loadData() throws Exception {
		
	}
	 


	public static void main(String[] args) throws Exception {
		Etl etl = new Etl();
		etl.run();
	}
}
