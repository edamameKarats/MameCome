package org.eda;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonPerser {
	public static HashMap<String,String> getJsonMap(String jsonString) throws Exception{
		HashMap<String,String> resultMap=new HashMap<String,String>();
		if(jsonString.startsWith("{")) {
			String tmpString=jsonString.replaceFirst("\\{", "");
			tmpString=tmpString.replaceFirst("\\}$", "");
			String[] tmpArray=tmpString.split(",");
			String concatString="";
			int count=0;
			String key="";
			for(int i=0;i<tmpArray.length;i++) {
				if(count==0) {
					concatString=tmpArray[i].replaceFirst(tmpArray[i].split(":")[0]+":", "");
					count+=countItems("{",tmpArray[i]);
					count+=countItems("[",tmpArray[i]);
					key=getKey(tmpArray[i]);
				}else {
					concatString=concatString+","+tmpArray[i];
					count+=countItems("{",tmpArray[i]);
					count+=countItems("[",tmpArray[i]);
					count-=countItems("}",tmpArray[i]);
					count-=countItems("]",tmpArray[i]);
				}
				if(count==0) {
					resultMap.put(key.replaceFirst("\"", "").replaceFirst("\"$",""), concatString.replaceFirst("^\"", "").replaceFirst("\"$",""));
				}
			}
		}else {
			throw new Exception("First character must be {");
		}
		return resultMap;
	}

	public static ArrayList<String> getJsonArray(String jsonString) throws Exception{
		ArrayList<String> result=new ArrayList<String>();
		if(jsonString.startsWith("[{")) {
			String tmpString=jsonString.replaceFirst("\\[", "");
			tmpString=tmpString.replaceFirst("\\]$", "");
			String[] tmpArray=tmpString.split(",");
			String concatString="";
			int count=0;
			for(int i=0;i<tmpArray.length;i++) {
				if(count==0) {
					concatString=tmpArray[i];
					count+=countItems("{",tmpArray[i]);
					count+=countItems("[",tmpArray[i]);
					count-=countItems("}",tmpArray[i]);
					count-=countItems("]",tmpArray[i]);
				}else {
					concatString=concatString+","+tmpArray[i];
					count+=countItems("{",tmpArray[i]);
					count+=countItems("[",tmpArray[i]);
					count-=countItems("}",tmpArray[i]);
					count-=countItems("]",tmpArray[i]);
				}
				if(count==0) {
					result.add(concatString);
				}
			}
		}else {
			throw new Exception("First characters must be [{");
		}
		return result;
	}


	private static int countItems(String chkString,String targetString) {
		int result=0;
		for(int i=0;i<targetString.length();i++) {
			if(targetString.charAt(i)==chkString.charAt(0)) {
				result+=1;
			}
		}
		return result;
	}

	private static String getKey(String input) {
		String result="";
		int startFlg=0;
		for(int i=0;i<input.length();i++) {
			if(input.charAt(i)=="\"".charAt(0)) {
				if(startFlg==0) {
					startFlg=1;
					result=String.valueOf(input.charAt(i));
				}else {
					startFlg=0;
					result=result+String.valueOf(input.charAt(i));
					break;
				}
			}else {
				if(startFlg==1) {
					result=result+String.valueOf(input.charAt(i));
				}
			}
		}

		return result;
	}
}
