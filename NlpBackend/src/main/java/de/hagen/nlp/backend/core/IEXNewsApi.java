package de.hagen.nlp.backend.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class IEXNewsApi implements IEXAccessApi {

	public List<IEXNews> getStockNews(String tickerSymbol) {

		URL url;
		try {
			
			url = new URL("https://api.iextrading.com/1.0/stock/" + tickerSymbol + "/news");

			StringBuffer content = sendRequest(url);		

			return parseNews(content.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private StringBuffer sendRequest(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);

		int status = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		
		return content;
	}

	private List<IEXNews> parseNews(String string) {

		List<IEXNews> result = new ArrayList<IEXNews>();

		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(string);

			JSONArray msg = (JSONArray) obj;

			for (Iterator<JSONObject> iterator = msg.iterator(); iterator.hasNext();) {
				JSONObject object = iterator.next();
				result.add(new IexNewsEntity(object.get("headline").toString(), object.get("summary").toString()));
			}

			return result;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	public List<IEXNews> getMarketNews() {

		try {
			URL url = new URL("https://api.iextrading.com/1.0/stock/market/news");

			StringBuffer content = sendRequest(url);		

			return parseNews(content.toString());

//			JSONParser parser = new JSONParser();
//			System.out.println(content.toString());
//			Object obj = parser.parse(content.toString());
//			con.disconnect();
//
//			JSONArray msg = (JSONArray) obj;
//
//			SentimentAnalyzer analyzer = new SentimentAnalyzer();
//			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//			int sentAll = 0;
//
//			for (Iterator<JSONObject> iterator = msg.iterator(); iterator.hasNext();) {
//				StringBuilder news = new StringBuilder();
//				JSONObject object = iterator.next();
//				news.append(object.get("headline"));
//				news.append(object.get("summary"));
//				int sentiment = analyzer.getSentiment(news.toString());
//				sentAll = sentAll + sentiment;
//				map.put(msg.indexOf(object), sentiment);
//			}
//
//			int result = sentAll / msg.size();
//
//			return result;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
