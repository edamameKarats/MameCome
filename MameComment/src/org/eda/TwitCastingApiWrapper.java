package org.eda;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * TwitCastingのAPIのWrapし、GET/POSTリクエストを行うクラス<br>
 * 更にそれを利用し、必要な情報を加工するメソッドを実装する
 * @author AA337121
 * @version 0.6
 */

public class TwitCastingApiWrapper {

	/**
	 * 指定されたアクセストークンに紐づくユーザー情報を取得して応答する
	 * @param accessToken 確認対象のアクセストークン
	 * @return 名前、screen_id、画像URLをArrayList<String>に格納して応答する
	 */
	public static ArrayList<String> getSelfData(String accessToken){
		ArrayList<String> result=new ArrayList<String>();
		String resultString=verifyCredentialsWrapper(accessToken);
		HashMap<String,String> userJSON=new HashMap<String,String>();
		try{
			userJSON=JsonPerser.getJsonMap(JsonPerser.getJsonMap(resultString).get("user"));
			result.add(userJSON.get("name"));
			result.add(userJSON.get("screen_id"));
			result.add(userJSON.get("image"));
		}catch(Exception e) {
			System.err.println("Cannot get user data.");
			System.err.println("result is :"+resultString);
			e.printStackTrace();
			result=null;
		}
		return result;
	}

	/**
	 * 指定されたユーザーのmovie_id、放送ステータスを取得して応答する
	 * @param screenId コメントを送る対象のscreen_id
	 * @param accessToken 使用するアクセストークン
	 * @return movie_id及び放送ステータスをArrayList<String>に格納して応答
	 */
	public static ArrayList<String> getMovieData(String screenId,String accessToken){
		ArrayList<String> result=new ArrayList<String>();
		if(screenId==null||screenId.equals("")) {
			return null;
		}
		String resultString=getUserInfoWrapper(screenId,accessToken);
		HashMap<String,String> resultJSON=null;
		try {
			resultJSON=JsonPerser.getJsonMap(resultString);
			HashMap<String,String> userJSON=JsonPerser.getJsonMap(resultJSON.get("user"));
			result.add(userJSON.get("last_movie_id"));
			result.add(userJSON.get("is_live"));
		}catch (Exception e) {
			System.err.println("Cannot get movie data.");
			System.err.println("result is :"+resultString);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 放送のコメントについて、コメント時刻でソートして、コメント時刻、ユーザー画像URL、ユーザー名、コメント、ユーザーのscreen_idを取得するして応答する。
	 * @param movieId コメントを送る対象のmovie_id
	 * @param sliceId コメント取得の起点となるslice_id
	 * @param accessToken 使用するアクセストークン
	 * @return コメント時刻、ユーザー画像URL、ユーザー名、コメント、ユーザーのscreen_idをArrayList<String>に格納し、それらをコメント時刻でソートしてArrayListに格納して応答
	 */
	public static ArrayList<ArrayList<String>> getCommentData(String movieId,String sliceId,String accessToken){
		ArrayList<ArrayList<String>> result=new ArrayList<ArrayList<String>>();
		HashMap<Integer,ArrayList<String>> tmpResult;
		ArrayList<String>  tmpComment;
		String resultString=getCommentsWrapper(movieId,sliceId,accessToken);
		HashMap<String,String> resultJSON=null;
		try {
			resultJSON=JsonPerser.getJsonMap(resultString);
			ArrayList<String> commentsJSON;
			HashMap<String,String> commentJSON;
			Instant instant;
			LocalDateTime ldt;
			if(resultJSON!=null) {
				commentsJSON=JsonPerser.getJsonArray(resultJSON.get("comments"));
				tmpResult=new HashMap<Integer,ArrayList<String>>();
				for(int i=0;i<commentsJSON.size();i++) {
					commentJSON=JsonPerser.getJsonMap(commentsJSON.get(i));
					tmpComment=new ArrayList<String>();
					tmpComment.add(commentJSON.get("id"));
					instant = Instant.ofEpochMilli((Integer.parseInt(commentJSON.get("created")))*(long)1000);
					ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
					tmpComment.add(String.format("%02d:%02d:%02d", ldt.getHour(),ldt.getMinute(),ldt.getSecond()));
					HashMap<String,String> userJSON=JsonPerser.getJsonMap(commentJSON.get("from_user"));
					tmpComment.add(userJSON.get("image"));
					tmpComment.add(userJSON.get("name"));
					//改行コード対応
					tmpComment.add(commentJSON.get("message").replaceAll("\\\\n","\n"));
					tmpComment.add(userJSON.get("screen_id"));
					tmpResult.put(Integer.parseInt(commentJSON.get("created")), tmpComment);
				}
				//sort
				Object[] mapKey=tmpResult.keySet().toArray();
				Arrays.sort(mapKey);
				for(int i=0;i<mapKey.length;i++) {
					result.add(tmpResult.get(mapKey[i]));
				}
			}
		}catch(Exception e) {
			System.err.println("Cannot convert GetComment result to JSON.");
			System.err.println("result is :"+resultString);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 指定したコメントをAPIに適応するようにJSON形式に変更し、送信、成否をbooleanで応答する
	 * @param movieId コメントを送る対象のmovie_id
	 * @param comment コメント文字列
	 * @param sns SNS投稿を行うかどうか normal(通常の投稿)、reply(配信者にリプライ)、none(投稿無し)で指定する
	 * @param accessToken 使用するクセストークン
	 * @return 投稿が成功したかどうか 投稿後のAPIの戻りに、createdの文字列が入っているかどうかで判断する
	 */
	public static boolean postCommentData(String movieId, String comment, String sns, String accessToken) {
		boolean result=false;
		String postData="{\"comment\": \""+comment+"\", \"sns\": \""+sns+"\"}";
		String resultString=postCommentWrapper(movieId,postData,accessToken);
		HashMap<String,String> resultJSON=null;
		try {
			resultJSON=JsonPerser.getJsonMap(resultString);
			resultJSON=JsonPerser.getJsonMap(resultJSON.get("comment"));
			if(resultJSON.get("created")!=null&&!resultJSON.get("created").equals("")) {
				result=true;
			}
		}catch(Exception e) {
			System.err.println("Cannot convert PostComment result to JSON.");
			System.err.println("result is :"+resultString);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Twicas API GetUserInfoのWrapper
	 * @param screenId 取得するユーザーのscreen_id
	 * @param accessToken 使用するアクセストークン
	 * @return APIから取得した文字列、取得できなかった場合はnullを返す
	 */
	public static String getUserInfoWrapper(String screenId,String accessToken) {
		String result;
		result=execGetRequest("https://apiv2.twitcasting.tv/users/"+screenId,accessToken);
		return result;
	}

	/**
	 * Twicas API VerifyGredentialsのWrapper
	 * @param accessToken 検査対象のアクセストークン
	 * @return APIから取得した文字列、取得できなかった場合はnullを返す
	 */
	public static String verifyCredentialsWrapper(String accessToken) {
		String result;
		result=execGetRequest("https://apiv2.twitcasting.tv/verify_credentials",accessToken);
		return result;

	}


	/**
	 * Twicas API GetCommentのWrapper、50件を上限として取得する
	 * @param movieId コメントをGETする配信のID
	 * @param sliceId コメントの取得起点となるメッセージのID、0の場合は最初から取得
	 * @param accessToken リクエストに使用するアクセストークン
	 * @return APIから取得した文字列、取得できなかった場合はnullを返す
	 */
	public static String getCommentsWrapper(String movieId,String sliceId,String accessToken) {
		String result;
		if(sliceId.equals("0")) {
			result=execGetRequest("https://apiv2.twitcasting.tv/movies/"+movieId+"/comments?limit=50",accessToken);

		}else {
			result=execGetRequest("https://apiv2.twitcasting.tv/movies/"+movieId+"/comments?slice_id="+sliceId+"&limit=50",accessToken);
		}
		return result;
	}

	/**
	 * Twicas API PostCommentのWrapper
	 * @param movieId コメントをPOSTする配信のID
	 * @param comment POSTするデータ文字列、JSON形式になっている必要がある
	 * @param accessToken リクエストに使用するアクセストークン
	 * @return APIから取得した文字列、取得できなかった場合はnullを返す
	 */
	public static String postCommentWrapper(String movieId, String comment, String accessToken) {
		String result;
		result=execPostRequest("https://apiv2.twitcasting.tv/movies/"+movieId+"/comments",comment,accessToken);
		return result;
	}

	/**
	 * Twicas APIのURLに対し、HTTP GETリクエストを投げる処理
	 * @param urlString リクエストURL文字列
	 * @param accessToken リクエストに使用するアクセストークン
	 * @return APIから取得した文字列、取得できなかった場合はnullを返す
	 */
	public static String execGetRequest(String urlString,String accessToken) {
		if(urlString==null||urlString.equals("")) {
			System.err.println("Request url string must not be blank or null.");
			return null;
		}
		if(accessToken==null||accessToken.equals("")) {
			System.err.println("Acces token string must not be blank or null.");
			return null;
		}
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
			System.err.println("Exception is occured during GET process.");
			e.printStackTrace();
			result=null;
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

	/**
	 * Twicas APIのURLに対し、HTTP POSTリクエストを投げる処理
	 * @param urlString リクエストURL文字列
	 * @param postData POSTするデータ文字列
	 * @param accessToken リクエストに使用するアクセストークン
	 * @return APIから取得した文字列、取得できなかった場合はnullを返す
	 */
	public static String execPostRequest(String urlString,String postData,String accessToken) {
		if(urlString==null||urlString.equals("")) {
			System.err.println("Request url string must not be blank or null.");
			return null;
		}
		//通常のPOSTであればブランクも許容されるが、
		//今回はTwicasAPI向けなので、ブランクはNG
		if(postData==null||postData.equals("")) {
			System.err.println("Post string must not be blank or null.");
			return null;
		}
		if(accessToken==null||accessToken.equals("")) {
			System.err.println("Access token string must not be blank or null.");
			return null;
		}

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
			connection.setRequestProperty("Content-Length",""+postData.getBytes(Charset.forName("UTF-8")).length);
			connection.setRequestProperty("X-Api-Version", "2.0");
			connection.setRequestProperty("Authorization", "Bearer "+accessToken);
			ow=new OutputStreamWriter(connection.getOutputStream());
			ow.write(postData);
			ow.flush();
			connection.connect();
			br=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			while((line=br.readLine())!=null) {
				result=result+line;
			}
		}catch (Exception e) {
			System.err.println("Exception is occured during GET process.");
			e.printStackTrace();
			result=null;
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
