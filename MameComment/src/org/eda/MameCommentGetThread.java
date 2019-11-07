package org.eda;

import static org.eda.MameCommentCommon.*;

import java.util.ArrayList;

import javafx.application.Platform;

/**
 * コメントを定期的に取得し、取得できた場合はMainControllerに送信リクエストを投げるクラス
 * @author AA337121
 * @version 0.6
 */
public class MameCommentGetThread extends Thread{

	private MameCommentSettingData mameCommentSettingData;
	private MameCommentMainController mameCommentMainController;
	private String movieUserId;
	public String movieId;
	private int stopFlg;

	/**
	 * コンストラクタ
	 * @param mcsData 設定データオブジェクト
	 * @param movId 放送取得対象のユーザーのscreen_id
	 * @param mainController 呼び出し元のMainController自身
	 */
	public MameCommentGetThread(MameCommentSettingData mcsData,String movId,MameCommentMainController mainController) {
		mameCommentSettingData=mcsData;
		movieUserId=movId;
		stopFlg=1;
		mameCommentMainController=mainController;
	}

	/**
	 * スレッド停止のための関数<br>
	 * これでフラグがONになり、次のループでスレッドが終了する
	 */
	public void stopThread() {
		stopFlg=1;
	}

	/**
	 * スレッド実行クラス<br>
	 * 1秒間隔でTwitCastingApiWrapperのgetCommentData()を発行し、コメントが有る場合はMainControllerのsendComment()を呼び出す<br>
	 * エラー発生、もしくは外部からのstopThread()呼び出しによりstopFlgがONになるとループを終了し、Main画面のgetStartButtonの表示をもとに戻す
	 */
	public void run() {
		movieId=checkMovie();
		stopFlg=0;
		long lastSliceId=0;
		ArrayList<ArrayList<String>> commentData;
		if(DEVELOP_MODE!=true) { //初回データ読み捨て
			if(!movieId.equals("")){
				write_debug_log("Start to get initial comment.");
				commentData=TwitCastingApiWrapper.getCommentData(movieId,String.valueOf(lastSliceId),mameCommentSettingData.token);
				ArrayList<String> comment;
				if (commentData.size()!=0) {
					for(int i=0;i<commentData.size();i++) {
						comment=commentData.get(i);
						if(lastSliceId<Long.parseLong(comment.get(0))) {
							lastSliceId=Long.parseLong(comment.get(0));
							write_debug_log("Update last slice_id to "+lastSliceId);
						}
					}
				}
			}
		}
		while(stopFlg!=1 && movieId!=null && !movieId.equals("")) {
			write_debug_log("Start to get comment.");
			try {
				commentData=TwitCastingApiWrapper.getCommentData(movieId,String.valueOf(lastSliceId),mameCommentSettingData.token);
				ArrayList<String> comment;
				if (commentData.size()!=0) {
					write_debug_log("New comment received.");
					for(int i=0;i<commentData.size();i++) {
						comment=commentData.get(i);
						if(lastSliceId<Long.parseLong(comment.get(0))) {
							lastSliceId=Long.parseLong(comment.get(0));
							write_debug_log("Update last slice_id to "+lastSliceId);
						}
					}
					mameCommentMainController.sendComment(commentData);
				}
				Thread.sleep(1000);
				movieId=checkMovie();
			}catch(Exception e) {
				//多分ここには来ない？けど、StackTraceだけ出す。
				e.printStackTrace();
				stopFlg=1;
			}
		}
		Platform.runLater(() ->
			mameCommentMainController.getStartButton.setText(GET_START)
		);
		Platform.runLater(() ->
			mameCommentMainController.getStartButton.getStyleClass().remove("push")
		);

	}

	/**
	 * 内部実行クラス<br>
	 * コンストラクタで指定した配信者のscreen_idをもとに、TwitCastingApiWrapperのgetMovieData()を呼び出して配信有無を応答する<br>
	 * @return movieData:放送のID
	 */
	private String checkMovie() {
		ArrayList<String> movieData=TwitCastingApiWrapper.getMovieData(movieUserId,mameCommentSettingData.token);
		if(movieData==null||movieData.get(0)==null||movieData.get(0).contentEquals("")||movieData.get(1)==null) {
			Platform.runLater(() ->
				displayWarning("ユーザーが見つかりませんでした。")
			);
			return "";
		}
		if(DEVELOP_MODE!=true) {
	 		if(movieData.get(1).equals("false")) {
				Platform.runLater(() ->
					displayWarning("ユーザーは現在放送中ではありません。")
				);
				return "";
			}
		}
		return movieData.get(0);
	}

}
