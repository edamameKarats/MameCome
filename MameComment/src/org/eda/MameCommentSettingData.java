package org.eda;

import static org.eda.MameCommentCommon.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 設定データの格納クラス
 * @author AA337121
 * @version 0.6
 */
public class MameCommentSettingData {

	public String replyUrl,token,logPath,boardFontName,boardFontColor,boardBgColor,viewerFontName,viewerFontColor,viewerBgColor;
	public double boardX,boardY,boardWidth,boardHeight,viewerX,viewerY,viewerWidth,viewerHeight,viewerTimeWidth,viewerImageWidth,viewerNameWidth,viewerCommentWidth,viewerFontSize,viewerImageSize;
	public int boardLineNum;
	public boolean logFlg;
	private final String[] paramList= {"replyUrl","token","logFlg","logPath","boardX","boardY","boardWidth","boardHeight","boardFontName","boardLineNum","boardFontColor"
			,"boardBgColor","viewerX","viewerY","viewerWidth","viewerHeight","viewerTimeWidth","viewerImageWidth","viewerNameWidth","viewerCommentWidth","viewerFontName","viewerFontSize","viewerFontColor","viewerBgColor","viewerImageSize"};

	/**
	 * コンストラクタ。ここでデフォルト値の設定を行う。
	 */
	public MameCommentSettingData(){
		//set initial value
		replyUrl="";
		token="";
		logFlg=false;
		logPath="";
		boardX=0.0;
		boardY=0.0;
		boardWidth=640.0;
		boardHeight=480.0;
		boardFontName="";
		boardLineNum=10;
		boardFontColor="0xFFFFFFFF";
		boardBgColor="0x006600FF";
		viewerX=0.0;
		viewerY=0.0;
		viewerWidth=640.0;
		viewerHeight=480.0;
		viewerTimeWidth=20.0;
		viewerImageWidth=20.0;
		viewerNameWidth=100.0;
		viewerCommentWidth=340.0;
		viewerFontName="";
		viewerFontSize=10.0;
		viewerFontColor="0x000000FF";
		viewerBgColor="0xFFFFFFFF";
		viewerImageSize=16;
	}

	/**
	 * iniファイルの読み込みクラス<br>
	 * カレントディレクトリのMameCommentSetting.iniファイルを読み込む
	 * @throws IOException ファイル読み込みエラー
	 * @throws FileNotFoundException ファイルなし
	 */
	public void readFromIni() throws IOException,FileNotFoundException {
		//read ini file
		BufferedReader bufferedReader=new BufferedReader(new FileReader(new File("MameCommentSetting.ini")));
		String line="";
		while((line=bufferedReader.readLine())!=null) {
			String[] lineArray;
			try {
				lineArray = line.split("=",2);
			} catch (Exception e1) {
				//変な文字列だった場合の対処
				lineArray= new String[2];
				lineArray[0]="";
				lineArray[1]="";
			}
			switch (lineArray[0]) {
			case "replyUrl":
				replyUrl=lineArray[1];
				break;
			case "token":
				token=lineArray[1];
				break;
			case "logFlg":
				logFlg=Boolean.valueOf(lineArray[1]);
				break;
			case "logPath":
				logPath=lineArray[1];
				break;
			case "boardX":
				try {
					boardX=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "boardY":
				try {
					boardY=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "boardWidth":
				try {
					boardWidth=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "boardHeight":
				try {
					boardHeight=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "boardFontName":
				boardFontName=lineArray[1];
				break;
			case "boardLineNum":
				boardLineNum=Integer.parseInt(lineArray[1]);
				break;
			case "boardFontColor":
				boardFontColor=lineArray[1];
				break;
			case "boardBgColor":
				boardBgColor=lineArray[1];
				break;
			case "viewerX":
				try {
					viewerX=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "viewerY":
				try {
					viewerY=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "viewerWidth":
				try {
					viewerWidth=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "viewerHeight":
				try {
					viewerHeight=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "viewerTimeWidth":
				try {
					viewerTimeWidth=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "viewerImageWidth":
				try {
					viewerImageWidth=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "viewerNameWidth":
				try {
					viewerNameWidth=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "viewerCommentWidth":
				try {
					viewerCommentWidth=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "viewerFontName":
				viewerFontName=lineArray[1];
				break;
			case "viewerFontSize":
				try {
					viewerFontSize=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "viewerFontColor":
				viewerFontColor=lineArray[1];
				break;
			case "viewerBgColor":
				viewerBgColor=lineArray[1];
				break;
			case "viewerImageSize":
				try {
					viewerImageSize=Double.parseDouble(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "debugMode":
				try {
					DEBUG_MODE=Boolean.valueOf(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			case "developMode":
				try {
					DEVELOP_MODE=Boolean.valueOf(lineArray[1]);
				} catch (NumberFormatException e) {
				}
				break;
			default:
			}
		}
		bufferedReader.close();
	}

	/**
	 * iniファイルへの書き込みクラス<br>
	 * カレントディレクトリのMameCommentSetting.iniに書き込む
	 * @throws IOException 書き込みエラー
	 * @throws FileNotFoundException
	 */
	public void writeToIni() throws IOException,FileNotFoundException{
		String line;
		ArrayList<String> lineArray=new ArrayList<String>();
		//read ini file
		try {
			BufferedReader bufferedReader=new BufferedReader(new FileReader(new File("MameCommentSetting.ini")));
			while ((line=bufferedReader.readLine())!=null) {
				lineArray.add(line);
			}
			bufferedReader.close();
		}catch(Exception e) {
			displayError("設定ファイルの読み込みに失敗しました。新規作成します。",e);
			e.printStackTrace();
		}
		//replace value
		for(int i=0;i<paramList.length;i++) {
			String tmpParam=paramList[i];
			boolean tmpFlg=false;
			for(int j=0;j<lineArray.size();j++) {
				if(lineArray.get(j).startsWith(tmpParam+"=")|| lineArray.get(j).matches("^ *"+tmpParam+"=")) {
					lineArray.remove(j);
					try {
						lineArray.add(j,tmpParam+"="+this.getClass().getField(tmpParam).get(this));
						tmpFlg=true;
					}catch(Exception e) {
						//多分ここには来ないのでStackTraceだけ出す
						e.printStackTrace();
					}
				}
			}
			if(tmpFlg==false) {
				try {
					lineArray.add(tmpParam+"="+this.getClass().getField(tmpParam).get(this));
				}catch(Exception e) {
					//多分ここには来ないのでStackTraceだけ出す
					e.printStackTrace();
				}
			}

		}
		//write ini file
		BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(new File("MameCommentSetting.ini"),false));
		for(int i=0;i<lineArray.size();i++) {
			if(i==lineArray.size()-1 && lineArray.get(i).contentEquals("")) {
				bufferedWriter.write(lineArray.get(i));
			}else {
				bufferedWriter.write(lineArray.get(i)+"\n");
			}
		}
		bufferedWriter.close();
	}

	/**
	 * repyUrlからToken文字列を抜き出し、tokenに格納する
	 * @return T/F 抜き出せたかどうか
	 */
	public boolean setToken() {
		try{
			if(replyUrl!=null&&!replyUrl.equals("")) {
				token=replyUrl.split("#",2)[1].split("&")[0].split("=",2)[1];
				write_debug_log("token is "+token);
			}else {
				return false;
			}
		}catch(Exception ex) {
			displayError("応答されたURL文字列に異常があります。\nURL:"+replyUrl,ex);
			write_log("ReplyUrl format may be wrong. "+replyUrl);
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
