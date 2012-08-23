package com.api;

public class API_Class 
{
	public static final String baseUrl = "http://api2.transitchatterdev.com/";
	//public static final String baseUrl = "http://api2.transitchatterdev.com/admin/tests/test-api.php";
	//Username: quartus
	//Pass: cheers88
	 //    User/Login
	public static final String login = "user/login";
	public static final String userInfo = "user";
	public static final String sessionInfo = "user/session";
	public static final String changeUserName = "user/setusername?";
	public static final String logout = "user/logout";
	public static final String postFeedBack = "user/feedback?";
	public static final String reportResponceTime = "user/reportresponsetime?";
	
	
	//  Rooms  (Room names are formatted as <operator>-<type>-<route>-<destination> )
	// Example (cta-rail-Red-Howard)
	public static final String enterRoom = "/enter";
	public static final String exitChatRoom = "/exit";
	public static final String listOfUsers = "/users";
	public static final String postMessage = "/messages?"; // body=test message
	public static final String lastMessages = "/messages/last-"; // 2
	public static final String messagesSince = "/messages/since-"; // 100
	public static final String sendHeartbeat = "/heartbeat?"; // latitude=41.980041&longitude=-87.658740
	public static final String vote = "/vote/"; // safety?vote_value=0.5
	
	// Transit Info
	public static final String allRoutes = "transit/allroutes";
	public static final String arrivalTimes = "transit/arrivals?"; // stop_name=Sheridan+%26+Foster
	public static final String routeStops = "transit/routestops"; // route_name=Red
	public static final String closestStop = "transit/closeststops"; // latitude=41.980041&longitude=-87.658740
	
	
	// Misc
	public static final String MisclastMessages = "recentactivity/last-";  // 2
	public static final String miscMessagesSince = "recentactivity/since-"; // 100
	public static final String weather = "weather?"; // latitude=41.980041&longitude=-87.658740
	public static final String testForNextStop = "test?";
	
	
}
