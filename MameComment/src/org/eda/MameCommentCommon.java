package org.eda;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * システム内共通変数、関数定義クラス
 * @author AA337121
 * @version 0.6
 */

public class MameCommentCommon {

	private MameCommentCommon() {

	}

	public static final String GET_START="取得開始";
	public static final String GET_STOP="取得停止";
	public static final String BOARD_OPEN="ボード表示";
	public static final String BOARD_CLOSE="ボード非表示";
	public static final String VIEWER_OPEN="ビューワー表示";
	public static final String VIEWER_CLOSE="ビューワー非表示";
	public static final String SETTING_OPEN="設定表示";
	public static final String SETTING_CLOSE="設定非表示";
	public static final String BOARD_TITLE="ボード";//not view
	public static final String VIEWER_TITLE="ビューワー";
	public static final String SETTING_TITLE="設定";
	public static final String AUTH_URL="https://apiv2.twitcasting.tv/oauth2/authorize?client_id=2671027374.f688b31afb4ae712724112e8ff419ae4c376a54f35f762bc047403ef6e56918d&response_type=tokenhttps://apiv2.twitcasting.tv/oauth2/authorize?client_id=2671027374.f688b31afb4ae712724112e8ff419ae4c376a54f35f762bc047403ef6e56918d&response_type=token";

	//Debug on/offのグローバル変数 ファイル読み込みで設定されるが、変更などはその後行わないし、ファイルへの保存も行わない
	public static boolean DEBUG_MODE=false;

	public static boolean DEVELOP_MODE=false;

	/**
	 * デバッグログ出力クラス<br>
	 * デバッグフラグ(当クラスにハードコード)がtrueの場合のみログを出力する
	 * @param message 出力するメッセージ
	 */
	public static void write_debug_log(String message) {
		if(DEBUG_MODE) {
			write_log("[DEBUG]"+message);
		}
	}

	/**
	 * ログ出力クラス<br>
	 * 時刻、クラス名を付与して標準出力にログを出力する
	 * @param message 出力するメッセージ
	 */
	public static void write_log(String message) {
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		System.out.println(sdf.format(cal.getTime())+" ["+Thread.currentThread().getStackTrace()[3].getClassName()+"] "+message);
	}

	/**
	 * ファイル出力クラス<br>
	 * 時刻、クラス名を付与してログを出力する
	 * @param message 出力するメッセージ
	 * @param fileName 出力先ファイル名
	 */
	public static void write_file(String message,String fileName) {
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

	public static void displayInformation(String message) {
		Alert alrt=new Alert(AlertType.INFORMATION);
		alrt.setTitle("情報");
		alrt.setHeaderText(null);
		alrt.setContentText(message);
		alrt.showAndWait();
	}


	public static void displayWarning(String message) {
		Alert alrt=new Alert(AlertType.WARNING);
		alrt.setTitle("警告");
		alrt.setHeaderText(null);
		alrt.setContentText(message);
		alrt.showAndWait();
	}

	public static void displayError(String message,Exception e) {
		Alert alrt=new Alert(AlertType.ERROR);
		alrt.setTitle("エラー");
		alrt.setHeaderText(null);
		StackTraceElement[] errTrace=e.getStackTrace();
		for(int i=0;i<errTrace.length;i++) {
			message=message+"\n"+errTrace[i].toString();
		}
		alrt.setContentText(message);
		alrt.showAndWait();
	}

}
