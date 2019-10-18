package org.eda;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class TwitCastingApiWrapper {

	//Token情報をもとに、自分のユーザー名、ID、画像URLを取得する
	public static ArrayList<String> getSelfData(String accessToken){
		ArrayList<String> result=new ArrayList<String>();
		String resultString=verifyCredentialsWrapper(accessToken);
		JSONObject resultJSON=new JSONObject(resultString);
		if(resultJSON != null) {
			JSONObject userJSON=new JSONObject(resultJSON.get("user").toString());
			result.add(userJSON.getString("name"));
			result.add(userJSON.getString("screen_id"));
			result.add(userJSON.getString("image"));
		}
		return result;
	}

	//指定されたuserIdをもとに、movieId、放送ステータスを取得する
	public static ArrayList<String> getMovieData(String screenId,String accessToken){
		ArrayList<String> result=new ArrayList<String>();
		String resultString=getUserInfoWrapper(screenId,accessToken);
		JSONObject resultJSON=new JSONObject(resultString);
		if(resultJSON != null) {
			JSONObject userJSON=new JSONObject(resultJSON.get("user").toString());
			result.add(userJSON.getString("last_movie_id"));
			result.add(String.valueOf(userJSON.getBoolean("is_live")));
		}
		return result;
	}

	//指定されたmovieId,sliceId,tokenをもとに、コメント時刻、ユーザー画像、コメントユーザー、コメントをコメント時刻でソートして取得する
	public static ArrayList<ArrayList<String>> getCommentData(String movieId,String sliceId,String accessToken){
		ArrayList<ArrayList<String>> result=new ArrayList<ArrayList<String>>();
		HashMap<Integer,ArrayList<String>> tmpResult;
		ArrayList<String>  commentData=new ArrayList<String>();
		ArrayList<String>  tmpComment;
		String resultString=getCommentsWrapper(movieId,sliceId,accessToken);
		JSONObject resultJSON=new JSONObject(resultString);
		JSONArray commentsJSON;
		JSONObject commentJSON;
		Instant instant;
		LocalDateTime ldt;
		if(resultJSON!=null) {
			commentsJSON=new JSONArray(resultJSON.get("comments").toString());
			tmpResult=new HashMap<Integer,ArrayList<String>>();
			for(int i=0;i<commentsJSON.length();i++) {
				commentJSON=new JSONObject(commentsJSON.get(i).toString());
				tmpComment=new ArrayList<String>();
				tmpComment.add(commentJSON.getString("id"));
				instant = Instant.ofEpochMilli(((int)commentJSON.get("created"))*(long)1000);
				ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				tmpComment.add(String.format("%02d:%02d:%02d", ldt.getHour(),ldt.getMinute(),ldt.getSecond()));
				JSONObject userJSON=new JSONObject(commentJSON.get("from_user").toString());
				tmpComment.add(userJSON.getString("image"));
				tmpComment.add(userJSON.getString("name"));
				tmpComment.add(commentJSON.getString("message"));
				tmpResult.put((int)commentJSON.get("created"), tmpComment);
			}
			//sort
			Object[] mapKey=tmpResult.keySet().toArray();
			Arrays.sort(mapKey);
			for(int i=0;i<mapKey.length;i++) {
				result.add(tmpResult.get(mapKey[i]));
			}
		}
		return result;
	}

	//指定されたmovieId、comment、sns、tokenをもとにリクエストし、createdのタイムスタンプが入っているかどうかでコメントできたかどうかを判断し、
	//true or falseで応答する
	public static boolean postCommentData(String movieId, String comment, String sns, String accessToken) {
		boolean result=false;
		String postData="{\"comment\": \""+comment+"\", \"sns\": \""+sns+"\"}";
		String resultString=postCommentWrapper(movieId,postData,accessToken);
		JSONObject resultJSON=new JSONObject(resultString);
		try {
			resultJSON.get("created");
			result=true;
		}catch(Exception e) {
		}
		return result;
	}

	//GetUserInfoのWrapper
	public static String getUserInfoWrapper(String screenId,String accessToken) {
		String result;
		result=execGetRequest("https://apiv2.twitcasting.tv/users/"+screenId,accessToken);
		return result;
	}

	//VerifyGredentialsのWrapper
	public static String verifyCredentialsWrapper(String accessToken) {
		String result;
		result=execGetRequest("https://apiv2.twitcasting.tv/verify_credentials",accessToken);
		return result;

	}


	//GetCommentsのWrapper(途中から)
	public static String getCommentsWrapper(String movieId,String sliceId,String accessToken) {
		String result;
		if(sliceId.equals("0")) {
			result=execGetRequest("https://apiv2.twitcasting.tv/movies/"+movieId+"/comments?limit=50",accessToken);

		}else {
			result=execGetRequest("https://apiv2.twitcasting.tv/movies/"+movieId+"/comments?slice_id="+sliceId+"&limit=50",accessToken);
		}
		return result;

	}

	//PostCommentのWrapper
	public static String postCommentWrapper(String movieId, String comment, String accessToken) {
		String result;
		result=execPostRequest("https://apiv2.twitcasting.tv/movies/"+movieId+"/comments",comment,accessToken);
		return result;
	}

	//HTTP GETリクエストを投げる処理
	public static String execGetRequest(String urlString,String accessToken) {
		URL url;
		HttpURLConnection connection=null;
		BufferedReader br=null;
		String line,result="";
		try {
			url = new URL(urlString);
			connection=(HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("X-Api-Version", "2.0");
			connection.setRequestProperty("Authorization", "Bearer "+accessToken);
			connection.connect();
			br=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			while((line=br.readLine())!=null) {
				result=result+line;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(br!=null) {
				try {
					br.close();
				}catch(Exception e) {
				}
			}
			if(connection!=null) {
				try {
					connection.disconnect();
				}catch(Exception e) {
				}
			}
		}
		return result;
	}

	//HTTP POSTリクエストを投げる処理
	public static String execPostRequest(String urlString,String postData,String accessToken) {
		URL url;
		HttpURLConnection connection=null;
		OutputStreamWriter ow=null;
		BufferedReader br=null;
		String line,result="";
		try {
			url = new URL(urlString);
			connection=(HttpURLConnection)url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-Api-Version", "2.0");
			connection.setRequestProperty("Authorization", "Bearer "+accessToken);
			ow=new OutputStreamWriter(connection.getOutputStream());
			ow.write(postData);
			connection.connect();
			br=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			while((line=br.readLine())!=null) {
				result=result+line;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(ow!=null) {
				try {
					ow.close();
				}catch(Exception e) {
				}
			}
			if(br!=null) {
				try {
					br.close();
				}catch(Exception e) {
				}
			}
			if(connection!=null) {
				try {
					connection.disconnect();
				}catch(Exception e) {
				}
			}
		}
		return result;
	}
}
