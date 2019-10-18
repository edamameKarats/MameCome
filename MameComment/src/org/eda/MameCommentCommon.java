package org.eda;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MameCommentCommon {

	private MameCommentCommon() {

	}

	public static final String getStart="取得開始";
	public static final String getStop="取得停止";
	public static final String boardTitle="ボード";//not view
	public static final String viewerTitle="ビューワー";
	public static final String settingTitle="設定";
	public static final String authTitle="認証画面";
	public static final String authURL="https://apiv2.twitcasting.tv/oauth2/authorize?client_id=2671027374.f688b31afb4ae712724112e8ff419ae4c376a54f35f762bc047403ef6e56918d&response_type=tokenhttps://apiv2.twitcasting.tv/oauth2/authorize?client_id=2671027374.f688b31afb4ae712724112e8ff419ae4c376a54f35f762bc047403ef6e56918d&response_type=token";

	public static final boolean debugFlg=true;

	public static void writeDebugLog(String message) {
		if(debugFlg) {
			writeLog("[DEBUG]"+message);
		}
	}

	public static void writeLog(String message) {
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		System.out.println(sdf.format(cal.getTime())+" ["+Thread.currentThread().getStackTrace()[3].getClassName()+"] "+message);
	}

	public static void writeFile(String message,String fileName) {
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		BufferedWriter bw=null;
		try {
			bw=new BufferedWriter(new FileWriter(new File(fileName)));
			bw.write(sdf.format(cal.getTime())+" ["+Thread.currentThread().getStackTrace()[3].getClassName()+"] "+message);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(bw!=null) {
				try {
					bw.close();
				}catch(Exception e) {
				}
			}
		}
	}

}
