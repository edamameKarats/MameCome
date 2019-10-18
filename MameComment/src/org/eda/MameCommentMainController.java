package org.eda;

import static org.eda.MameCommentCommon.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MameCommentMainController implements Initializable{
	@FXML Label accountUserName;
	@FXML Label accountUserId;
	@FXML ImageView accountUserImage;
	@FXML TextField movieUserId;
	@FXML Button getStartButton;
	@FXML Button displayBoardButton;
	@FXML Button displayViewerButton;
	@FXML Button displaySettingButton;

	Stage boardStage,viewerStage,settingStage;
	FXMLLoader boardLoader,viewerLoader,settingLoader;
	Parent boardRoot,viewerRoot,settingRoot;
	Scene boardScene,viewerScene,settingScene;
	MameCommentBoardController boardController;
	MameCommentViewerController viewerController;
	MameCommentSettingController settingController;
	MameCommentSettingData settingData;
	MameCommentGetThread getThread;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try{
			settingData=new MameCommentSettingData();
			settingData.readFromIni();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void initAfterViewed() {
		//initialize Texts
		getStartButton.setText(getStart);
		//set names
		setAccountUserInfo();
	}

	@FXML
	public void getStartClicked(ActionEvent evt) {
		if(getStartButton.getText().equals(getStart)) {
			getThread=new MameCommentGetThread(settingData,movieUserId.getText(),this);
			getThread.start();
			getStartButton.setText(getStop);
		}else {
			getThread.stopThread();
			int count=0;
			while(getThread.isAlive()) {
				try {
					Thread.sleep(500);
				}catch(Exception e) {
				}
				count+=1;
				if(count==10)break;
			}
			getStartButton.setText(getStart);
		}
	}

	@FXML
	public void displayBoardClicked(ActionEvent evt) {
		try {
			boardStage=new Stage();
			boardLoader=new FXMLLoader(getClass().getResource("MameCommentBoard.fxml"));
			boardRoot = boardLoader.load();
			boardController=boardLoader.getController();
			boardStage.setTitle(boardTitle);
			boardScene = new Scene(boardRoot);
			boardScene.getStylesheets().add(getClass().getResource("MameCommentBoard.css").toExternalForm());
			boardStage.setScene(boardScene);
			boardStage.initStyle(StageStyle.TRANSPARENT);
			boardStage.show();
			boardController.initAfterViewed(boardStage,settingData);

			boardStage.showingProperty().addListener((observable, oldvalue, newvalue)->{
				if(oldvalue==true && newvalue==false) {
					try {
						settingData.boardX=boardStage.getX();
						settingData.boardY=boardStage.getY();
						settingData.boardWidth=boardStage.getWidth();
						settingData.boardHeight=boardStage.getHeight();
					}catch(Exception e) {
						e.printStackTrace();
					}
					setAccountUserInfo();
				}
			});

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	@FXML
	public void displayViewerClicked(ActionEvent evt) {
		try {
			viewerStage=new Stage();
			viewerLoader=new FXMLLoader(getClass().getResource("MameCommentViewer.fxml"));
			viewerRoot = viewerLoader.load();
			viewerController=viewerLoader.getController();
			viewerStage.setTitle(viewerTitle);
			viewerScene = new Scene(viewerRoot);
			viewerScene.getStylesheets().add(getClass().getResource("MameCommentViewer.css").toExternalForm());
			viewerStage.setScene(viewerScene);
			viewerStage.show();
			viewerController.initAfterViewed(viewerStage,this,settingData);
			viewerStage.showingProperty().addListener((observable, oldvalue, newvalue)->{
				if(oldvalue==true && newvalue==false) {
					try {
						settingData.viewerX=viewerStage.getX();
						settingData.viewerY=viewerStage.getY();
						settingData.viewerWidth=viewerStage.getWidth();
						settingData.viewerHeight=viewerStage.getHeight();
						settingData.viewerTimeWidth=viewerController.timeTableColumn.getWidth();
						settingData.viewerImageWidth=viewerController.imageTableColumn.getWidth();
						settingData.viewerNameWidth=viewerController.nameTableColumn.getWidth();
						settingData.viewerCommentWidth=viewerController.commentTableColumn.getWidth();
					}catch(Exception e) {
						e.printStackTrace();
					}
					setAccountUserInfo();
				}
			});
		} catch(Exception ex) {
			ex.printStackTrace();
		}

	}

	@FXML
	public void displaySettingClicked(ActionEvent evt) {
		try {
			settingStage=new Stage();
			settingLoader = new FXMLLoader(getClass().getResource("MameCommentSetting.fxml"));
			settingRoot = settingLoader.load();
			settingController=settingLoader.getController();
			settingStage.setTitle(settingTitle);
			settingScene = new Scene(settingRoot);
			settingScene.getStylesheets().add(getClass().getResource("MameCommentSetting.css").toExternalForm());
			settingStage.setScene(settingScene);

			settingStage.showingProperty().addListener((observable, oldvalue, newvalue)->{
				if(oldvalue==true && newvalue==false) {
					try {
						settingData.writeToIni();
					}catch(Exception e) {
						e.printStackTrace();
					}
					setAccountUserInfo();
				}
			});




			settingStage.show();
			settingController.initAfterViewed(settingStage,settingData);

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void closeAll() {
		if(boardStage!=null && boardStage.isShowing()==true) {
			boardStage.close();
		}
		if(viewerStage!=null && viewerStage.isShowing()==true) {
			viewerStage.close();
		}
		if(settingStage!=null && settingStage.isShowing()==true) {
			settingStage.close();
		}
		try {
			settingData.writeToIni();
		}catch(Exception e) {
			e.printStackTrace();
		}
		try{
			File dir=new File("cachedImages");
			File[] files=dir.listFiles();
			for(int i=0;i<files.length;i++) {
				if(files[i].isFile()) {
					files[i].delete();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setAccountUserInfo() {
		if(settingData.token!=null&&!settingData.token.equals("")) {
			try {
				ArrayList<String> accountUserInfo=TwitCastingApiWrapper.getSelfData(settingData.token);
				accountUserName.setText(accountUserInfo.get(0));
				accountUserId.setText(accountUserInfo.get(1));
				accountUserImage.setImage(new Image(accountUserInfo.get(2)));
			}catch(Exception e) {
				e.printStackTrace();
				accountUserName.setText("");
				accountUserId.setText("");
				accountUserImage.setImage(new Image("blank.jpg"));
			}
		}else {
			accountUserName.setText("");
			accountUserId.setText("");
			accountUserImage.setImage(new Image("blank.jpg"));
		}
	}

	public void sendComment(ArrayList<ArrayList<String>> commentsArray) {
		writeDebugLog("Send comment request received.");
		if(settingData.logFlg==true&&!settingData.logPath.contentEquals("")) {
			for(int i=0;i<commentsArray.size();i++) {
				writeLog("Time:"+commentsArray.get(i).get(0)+",User:"+commentsArray.get(i).get(2)+",Comment:"+commentsArray.get(i).get(3));
			}
		}
		if(boardStage!=null&&boardStage.isShowing()) {
			Platform.runLater(() ->
				boardController.playRequest(commentsArray)
			);
		}
		if(viewerStage!=null&&viewerStage.isShowing()) {
			Platform.runLater(() ->
				viewerController.setComment(commentsArray)
			);
		}
	}

	public boolean postComment(String comment,String sns) {
		writeDebugLog("Post comment request received. comment:"+comment+", sns:"+sns);
		return TwitCastingApiWrapper.postCommentData(getThread.movieId, comment, sns, settingData.token);
	}
}
