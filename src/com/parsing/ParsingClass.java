package com.parsing;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.provider.Settings.Secure;

import com.api.API_Class;



public class ParsingClass 
{
	private String response;
	DefaultHttpClient hc = new DefaultHttpClient();
	ResponseHandler<String> responceHandler = new BasicResponseHandler();
	private List<Cookie> cookies;
	HttpPost httpost;
	DefaultHttpClient httpclient = new DefaultHttpClient();
	HttpGet httpget;
	HttpResponse responseHttp;
	HttpEntity entity;
	private ArrayList<String> single;
	public static RestClient rest;

	// API INnfo / GET
	public synchronized Object apiInfo()
	{
		System.out.println("Print inside API Info API calling time");		
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.userInfo);// Passing URL STRING			

		RestClient rest = new RestClient("http://api2.transitchatterdev.com");
		System.out.println("URL of API INFO API :"+"http://api2.transitchatterdev.com");
		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);

		return response;
	}

	// User Login    / Post Method
	public synchronized RestClient login(String userName , String diviceId)
	{
		System.out.println("Print inside Login API calling time");
		rest = new RestClient("http://api2.transitchatterdev.com/user/login");
		System.out.println("URL of Login API "+"http://api2.transitchatterdev.com/user/login");
		rest.AddParam("username", userName);
		rest.AddParam("uuid", diviceId.toString());
		try 
		{
			rest.Execute(RequestMethod.POST);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}
		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return rest;
	}


	// User Info	 /  Get Method
	public synchronized Object userInfo()
	{
		System.out.println("Print inside User Info API calling time");		
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.userInfo);// Passing URL STRING			

		RestClient rest = new RestClient("http://api2.transitchatterdev.com/user");
		System.out.println("URL of USer INFO "+API_Class.baseUrl+API_Class.userInfo);
		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}
		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);

		return response;
	}

	// Session Info   /  Get Method
	public synchronized Object sessionInfo()
	{
		System.out.println("Print inside session Info API calling time");
		RestClient rest = new RestClient("http://api2.transitchatterdev.com/user/session");
		System.out.println("URL of USer INFO "+"http://api2.transitchatterdev.com/user/session");
		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + rest.getResponse());

		return response;
	}

	// Change UserName / Screen Name    / Post Method
	public synchronized String changeUsername(String username)
	{
		System.out.println("Print inside Change User Name API calling time");
		RestClient rest = new RestClient("http://api2.transitchatterdev.com/user/setusername");
		System.out.println("URL of Change User Name API "+"http://api2.transitchatterdev.com/user/setusername");
		rest.AddParam("username", username);

		try 
		{
			rest.Execute(RequestMethod.POST);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}
		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return response;
	}

	// Change Announcement Type // Post 
	public synchronized Object changeAnnouncementTypes()
	{
		System.out.println("Print inside Change Announcement Type API calling time");
		RestClient rest = new RestClient("http://api2.transitchatterdev.com/user/setann?");
		System.out.println("URL of Change Announcement Type API "+"http://api2.transitchatterdev.com//user/setann?");
		rest.AddParam("ann_types", "deal,event,info,wiki,alert,crime&ann_delay=5");

		try 
		{
			rest.Execute(RequestMethod.POST);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);

		return response;

	}

	// User Logout   / Post Method
	public synchronized RestClient logout()
	{
		System.out.println("Print inside Logout  API calling time");		
		RestClient rest = new RestClient("http://api2.transitchatterdev.com/user/logout");
		System.out.println("URL of Logout  API  "+"http://api2.transitchatterdev.com/user/logout");

		try 
		{
			rest.Execute(RequestMethod.POST);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}
		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return rest;		
	}

	// Post Feedback   / Post Method
	public synchronized Object postFeedBack(String type , String message , String contact_info)
	{
		System.out.println("Print inside Post FeedBack API calling time");
		RestClient rest = new RestClient("http://api2.transitchatterdev.com/user/feedback?"); // Passing URL STRING
		rest.AddParam("type", type);
		rest.AddParam("message", message);
		rest.AddParam("contact_info", contact_info);	

		try 
		{		
			rest.Execute(RequestMethod.POST);			
		} 
		catch (UnsupportedEncodingException e1) 
		{
			e1.printStackTrace();
			return null;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);

		return response;
	}

	// Report Responce Time  / Post Method
	public synchronized Object reportResponceTime(String url , String duration)
	{
		System.out.println("Print inside Report Responce Time API calling time");		
		RestClient rest = new RestClient("http://api2.transitchatterdev.com/user/reportresponsetime?");
		System.out.println("URL of Report Responce Time API "+"http://api2.transitchatterdev.com/user/reportresponsetime?");
		rest.AddParam("url", url);
		rest.AddParam("duration", duration);

		try 
		{
			rest.Execute(RequestMethod.POST);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}
		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);

		return response;
	}

	// Enter In room  / Post Method
	public synchronized RestClient enterRoom(String roomName)
	{
		System.out.println("Print inside Enter in Room API calling time");
		System.out.println("Room Name "+roomName);

		RestClient rest=null;
		String encodeData= null;
		try {
			encodeData = URLEncoder.encode(roomName, "utf-8");


		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		rest = new RestClient("http://api2.transitchatterdev.com/"+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+"/enter");
		System.out.println("URL of Enter in Room API "+"http://api2.transitchatterdev.com/"+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+"/enter");

		try 
		{
			rest.Execute(RequestMethod.POST);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return rest;	
	}

	// Exit Room / leave Room   / Post Method
	public synchronized String exitRoom(String roomName)
	{
		System.out.println("Print inside Exit from Room API calling time");		
		//HttpPost postMethod = new HttpPost(API_Class.baseUrl+API_Class.enterRoom);// Passing URL STRING
		//HttpPost postMethod = new HttpPost(API_Class.baseUrl+roomName+"/exit");// Passing URL STRING
		RestClient rest = new RestClient(API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+"/exit");
		System.out.println("URL of Exit from Room API "+API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+"/exit");

		try 
		{
			rest.Execute(RequestMethod.POST);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return response;	
	}

	// List Of users / Get Method
	public synchronized String listOfUsers(String roomName)
	{
		System.out.println("Print inside List Of users API calling time");
		RestClient rest = null;
		try 
		{
			rest = new RestClient(API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+"/users");
			System.out.println("URL of USer INFO "+API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+"/users");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try 
		{
			rest.Execute(RequestMethod.GET);
			response = rest.getResponse();
			System.out.println("responsecode is .. " + rest.getResponseCode());
			System.out.println("response is .. " + response);
			if(response != null)
			{
				response = response.replaceAll("\n", "");
			}
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		return response;	
	}

	// Post Message   /  Post Method
	public synchronized RestClient postMessage(String body , String roomName)
	{
		System.out.println("Print inside Post Message API calling time");		
		//HttpPost postMethod = new HttpPost(API_Class.baseUrl+API_Class.enterRoom);// Passing URL STRING
		rest = new RestClient(API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+"/messages?");
		System.out.println("URL of Login API "+API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+"/messages");
		rest.AddParam("body", body);

		try 
		{
			rest.Execute(RequestMethod.POST);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);

		return rest;
	}

	// Last Messages  / Get Method
	public synchronized Object lastMessages(String roomName)
	{
		System.out.println("Print inside last Messages API calling time");	

		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.sessionInfo);// Passing URL STRING			
		RestClient rest = null;

		rest = new RestClient(API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+"/messages/last-50");

		System.out.println("URL of last Messages API "+API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+"/messages/last-10");

		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return response;	
	}

	// Message since from /  Get Method
	public synchronized RestClient messageSince(String lastMessageID , String roomName) 
	{
		System.out.println("Print inside Messages since API calling time");		
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.sessionInfo);// Passing URL STRING			
		RestClient rest = new RestClient(API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+API_Class.messagesSince+lastMessageID);
		System.out.println("URL of Messages since API "+API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+API_Class.messagesSince+lastMessageID);

		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);

		return rest;
	}

	// Send HeartBeat (Latitude ANd Longitude ) /  Post Method
	public synchronized Object sendHeartBeat(String latitude , String longitude , String roomName)
	{
		System.out.println("Print inside Send HeartBeat API calling time");		
		//HttpPost postMethod = new HttpPost(API_Class.baseUrl+API_Class.enterRoom);// Passing URL STRING

		try {
			rest = new RestClient(API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+API_Class.sendHeartbeat);
			System.out.println("URL of Send HeartBeat API "+(API_Class.baseUrl+(roomName.replaceAll(" ", "%20")).replaceAll("/", "%2f")+API_Class.sendHeartbeat));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		rest.AddParam("latitude", latitude);
		rest.AddParam("longitude", longitude);

		try 
		{
			rest.Execute(RequestMethod.POST);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return response;	
	}

	// Give Rate For a Room /  Post Method
	public synchronized Object voteToRoom(String roomName , String safty , String vote_value )
	{
		System.out.println("Print inside Give Rate for Room API calling time");		
		//HttpPost postMethod = new HttpPost(API_Class.baseUrl+API_Class.enterRoom);// Passing URL STRING

		rest = new RestClient(API_Class.baseUrl+roomName.replaceAll(" ", "%20")+"/vote/"+safty+"?");
		System.out.println("URL of Give Rate for Room API "+API_Class.baseUrl+roomName+"/vote/"+safty+"?");
		rest.AddParam("vote_value", vote_value);		

		try 
		{
			rest.Execute(RequestMethod.POST);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);

		return response;
	}


	// Give All Routes of train And bus  / Get Method
	public synchronized String allRoutes()
	{
		System.out.println("Print inside All Routes Of trains And Bus API calling time");
		rest = new RestClient("http://api2.transitchatterdev.com/transit/allroutes");
		System.out.println("URL of All Routes Of trains "+"https://api2.transitchatterdev.com/transit/allroutes");

		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}
		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return response;	
	}


	// Arrival Time  params(stop name)   / Get Method
	public synchronized RestClient arrivalTime(String stop_name)
	{
		System.out.println("Print inside Arrival Time API calling time");		
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.sessionInfo);// Passing URL STRING			

		rest = new RestClient(API_Class.baseUrl+API_Class.arrivalTimes+"stop_name="+(stop_name.trim()).replaceAll(" ", "%20"));
		System.out.println("URL of Arrival Time API  "+API_Class.baseUrl+API_Class.arrivalTimes);

		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return rest;	
	}

	// Route Stops params(route_name=red) / Get Method
	public synchronized RestClient routeStops(String route_name)
	{
		System.out.println("Print inside Route Stops API calling time");		
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.sessionInfo);// Passing URL STRING	

		rest = new RestClient("http://api2.transitchatterdev.com/transit/routestops?route_name="+route_name);
		System.out.println("http://api2.transitchatterdev.com/transit/routestops?route_name="+route_name);
		System.out.println("URL of Route Stops API  "+API_Class.baseUrl+API_Class.routeStops+"route_name="+route_name);
		//rest.AddParam("route_name", route_name);
		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return rest;	
	}

	// Closest Stops params(Latitude And longitude) /  Get Method
	public synchronized RestClient closestStops(String latitude , String longitude)
	{
		System.out.println("Print inside Closest Stops API calling time");		
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.sessionInfo);// Passing URL STRING			

		rest = new RestClient(API_Class.baseUrl+API_Class.closestStop);
		System.out.println("URL of Closest Stops API  "+API_Class.baseUrl+API_Class.closestStop);
		System.out.println("Lat"+latitude+" lon "+longitude );
		rest.AddParam("latitude", latitude);
		rest.AddParam("longitude", longitude);
		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return rest;	
	}

	// Last Messages Recently  params(2) /  Get Method
	public synchronized Object lastMessagesRecently(String lastMessageId ,String roomName)
	{
		System.out.println("Print inside Last Messages Misc API calling time");		
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.sessionInfo);// Passing URL STRING			
		//	HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.MisclastMessages+"2");// Passing URL STRING

		rest = new RestClient(API_Class.baseUrl+roomName+API_Class.messagesSince+lastMessageId);
		System.out.println("URL of Last Messages Misc API  "+API_Class.baseUrl+API_Class.messagesSince+lastMessageId);

		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return response;	
	}

	// Messages Since Recently params(100) /  Get Method
	public synchronized RestClient messagesSinceRecently(String lastMessageId , String roomName)
	{
		System.out.println("Print inside Messages Since Misc API calling time");		
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.sessionInfo);// Passing URL STRING			
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.miscMessagesSince+"100");// Passing URL STRING

		rest = new RestClient(API_Class.baseUrl+roomName+API_Class.miscMessagesSince+lastMessageId);
		System.out.println("URL of Last Messages Since Misc API  "+API_Class.baseUrl+API_Class.messagesSince+lastMessageId);

		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}
		response =rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is all Route.. " + response);

		return rest;
	}

	// Weather params(Latitude And longitude)   / Get Method
	public synchronized Object weather(String latitude  , String longitude)
	{
		System.out.println("Print inside Weather API calling time");		
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.sessionInfo);// Passing URL STRING			
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.weather);// Passing URL STRING

		rest = new RestClient(API_Class.baseUrl+API_Class.weather);
		System.out.println("URL of Weather API  "+API_Class.baseUrl+API_Class.weather);

		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return response;	
	}

	public synchronized String testForNextStop(String lat , String lon)
	{
		System.out.println("Print inside Test For next Stop API calling time");		
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.sessionInfo);// Passing URL STRING			
		//HttpGet getMethod = new HttpGet(API_Class.baseUrl+API_Class.weather);// Passing URL STRING

		rest = new RestClient(API_Class.baseUrl+API_Class.testForNextStop);
		System.out.println("URL of Test For next Stop API  API  "+API_Class.baseUrl+API_Class.testForNextStop);
		rest.AddParam("latitude", lat);
		rest.AddParam("longitude", lon);

		try 
		{
			rest.Execute(RequestMethod.GET);
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
		}

		response = rest.getResponse();
		System.out.println("responsecode is .. " + rest.getResponseCode());
		System.out.println("response is .. " + response);
		if(response != null)
		{
			response = response.replaceAll("\n", "");
		}

		return response;	
	}
}
