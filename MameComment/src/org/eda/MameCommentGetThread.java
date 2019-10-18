package org.eda;

import static org.eda.MameCommentCommon.*;

import java.util.ArrayList;

import javafx.application.Platform;

public class MameCommentGetThread extends Thread{

	private MameCommentSettingData mameCommentSettingData;
	private MameCommentMainController mameCommentMainController;
	private String movieUserId;
	public String movieId;
	private int stopFlg;


	public MameCommentGetThread(MameCommentSettingData mcsData,String movId,MameCommentMainController mainController) {
		mameCommentSettingData=mcsData;
		movieUserId=movId;
		stopFlg=1;
		mameCommentMainController=mainController;
	}

	public void stopThread() {
		stopFlg=1;
	}

	public void run() {
		movieId=checkMovie();
		stopFlg=0;
		long lastSliceId=0;
		ArrayList<ArrayList<String>> commentData;
		while(stopFlg!=1&& movieId!=null && !movieId.equals("")) {
			writeDebugLog("Start to get comment.");
			try {
				commentData=TwitCastingApiWrapper.getCommentData(movieId,String.valueOf(lastSliceId),mameCommentSettingData.token);
				ArrayList<String> comment;
				if (commentData.size()!=0) {
					writeDebugLog("New comment received.");
					for(int i=0;i<commentData.size();i++) {
						comment=commentData.get(i);
						if(lastSliceId<Long.parseLong(comment.get(0))) {
							lastSliceId=Long.parseLong(comment.get(0));
							writeDebugLog("Update last slice_id to "+lastSliceId);
						}
					}
					mameCommentMainController.sendComment(commentData);
				}
				Thread.sleep(1000);
				movieId=checkMovie();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		Platform.runLater(() ->
			mameCommentMainController.getStartButton.setText(getStart)
		);

	}

	private String checkMovie() {
		ArrayList<String> movieData=TwitCastingApiWrapper.getMovieData(movieUserId,mameCommentSettingData.token);
		if(movieData==null||movieData.get(0)==null||movieData.get(0).contentEquals("")||movieData.get(1)==null) {
			System.out.println("user is not found.");
			return "";
		}

/*		if(movieData.get(1).contentEquals("false")) {
			System.out.println("user is not live.");
			return "";
		}
*/		return movieData.get(0);
	}

}
