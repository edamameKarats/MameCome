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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * Main画面のControllerクラス
 * @author AA337121
 * @version 0.6
 */
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
	/**
	 * 初期化クラスのオーバーライド<br>
	 * 設定データをiniファイルから読み込む
	 */
	public void initialize(URL location, ResourceBundle resources) {
		try{
			settingData=new MameCommentSettingData();
			settingData.readFromIni();
		}catch(FileNotFoundException e) {
			displayWarning("MameCommentSetting.iniが見つかりません。デフォルト値で新規作成します。");
			e.printStackTrace();
		}catch(Exception e) {
			displayWarning("MameCommentSetting.iniが読み込めません。デフォルト値で新規作成します。プログラム終了時に設定が保存されない可能性があります。");
			e.printStackTrace();
		}
		try{
			File dir=new File("cachedImages");
			if(dir.exists()) {
				File[] files=dir.listFiles();
				for(int i=0;i<files.length;i++) {
					if(files[i].isFile()) {
						files[i].delete();
					}
				}
			}else {
				dir.mkdir();
			}
		}catch(Exception e) {
			displayWarning("キャッシュファイルの削除に失敗しました。cachedImagesフォルダの権限や、その内部のファイルの権限を確認してください。");
			e.printStackTrace();
		}
	}

	/**
	 * 表示後に値を調整する<br>
	 * 開始ボタンのテキストを”取得開始”に変更、tokenの情報をもとに、ユーザー名などを設定
	 */
	public void initAfterViewed() {
		//initialize Texts
		getStartButton.setText(GET_START);
		//set names
		setAccountUserInfo();
	}

	@FXML
	/**
	 * 取得開始ボタンをクリックしたときの動作<br>
	 * ボタンのテキストが”取得開始”の場合はGetThreadを起動し、”取得停止”にテキストを変更、スタイルもpushに変更する
	 * ボタンのテキストが"取得停止"の場合はGetThreadに対しstopThread()を発行し、スレッドの停止を待ち、止まったら"取得開始"にテキストを変更、スタイルもデフォルトに戻す
	 * @param evt
	 */
	public void getStartClicked(ActionEvent evt) {
		if(getStartButton.getText().equals(GET_START)) {
			getThread=new MameCommentGetThread(settingData,movieUserId.getText(),this);
			getThread.start();
			getStartButton.setText(GET_STOP);
			getStartButton.getStyleClass().add("push");
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
			getStartButton.setText(GET_START);
			getStartButton.getStyleClass().remove("push");
		}
	}

	@FXML
	/**
	 * ボード表示ボタンをクリックしたときの動作<br>
	 * 開いているときにクリックしたら閉じる、閉じているときにクリックしたら開く<br>
	 * 合わせて、ボタンのスタイルや表示文字を変更する
	 * @param evt
	 */
	public void displayBoardClicked(ActionEvent evt) {
		if(boardStage!=null && boardStage.isShowing()) {
			boardStage.close();
		}
		else {
			try {
				boardStage=new Stage();
				boardLoader=new FXMLLoader(getClass().getResource("MameCommentBoard.fxml"));
				boardRoot = boardLoader.load();
				boardController=boardLoader.getController();
				boardStage.setTitle(BOARD_TITLE);
				boardScene = new Scene(boardRoot);
				boardScene.getStylesheets().add(getClass().getResource("MameCommentBoard.css").toExternalForm());
				boardStage.setScene(boardScene);
				boardStage.initStyle(StageStyle.TRANSPARENT);
				boardStage.show();
				boardController.initAfterViewed(boardStage,settingData);

				boardStage.showingProperty().addListener((observable, oldvalue, newvalue)->{
					if(oldvalue==true && newvalue==false) {
						settingData.boardX=boardStage.getX();
						settingData.boardY=boardStage.getY();
						settingData.boardWidth=boardStage.getWidth();
						settingData.boardHeight=boardStage.getHeight();
						displayBoardButton.setText(BOARD_OPEN);
						displayBoardButton.getStyleClass().remove("push");
					}
				});
				displayBoardButton.setText(BOARD_CLOSE);
				displayBoardButton.getStyleClass().add("push");
			} catch(Exception ex) {
				displayError("ボード画面の表示に失敗しました。",ex);
				ex.printStackTrace();
			}
		}
	}
	@FXML
	/**
	 * ビューワー表示ボタンをクリックしたときの動作<br>
	 * 開いているときにクリックしたら閉じる、閉じているときにクリックしたら開く<br>
	 * 合わせて、ボタンのスタイルや表示文字を変更する
	 * @param evt
	 */
	public void displayViewerClicked(ActionEvent evt) {
		if(viewerStage!=null && viewerStage.isShowing()) {
			viewerStage.close();
		}else {
			try {
				viewerStage=new Stage();
				viewerLoader=new FXMLLoader(getClass().getResource("MameCommentViewer.fxml"));
				viewerRoot = viewerLoader.load();
				viewerController=viewerLoader.getController();
				viewerStage.setTitle(VIEWER_TITLE);
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
						displayViewerButton.setText(VIEWER_OPEN);
						displayViewerButton.getStyleClass().remove("push");

					}
				});
				displayViewerButton.setText(VIEWER_CLOSE);
				displayViewerButton.getStyleClass().add("push");
			} catch(Exception ex) {
				displayError("ビューワー画面の表示に失敗しました。",ex);
				ex.printStackTrace();
			}
		}
	}

	@FXML
	/**
	 * 設定表示ボタンをクリックしたときの動作<br>
	 * 開いているときにクリックしたら閉じる、閉じているときにクリックしたら開く<br>
	 * なお、ウインドウはモーダル形式で開くものとし、メインに戻ることは許可しない<br>
	 * 合わせて、ボタンのスタイルや表示文字を変更する
	 * @param evt
	 */
	public void displaySettingClicked(ActionEvent evt) {
		if(settingStage!=null && settingStage.isShowing()) {//押せないけど
			settingStage.close();
		}else {
			try {
				settingStage=new Stage();
				settingStage.initModality(Modality.APPLICATION_MODAL);
				settingLoader = new FXMLLoader(getClass().getResource("MameCommentSetting.fxml"));
				settingRoot = settingLoader.load();
				settingController=settingLoader.getController();
				settingStage.setTitle(SETTING_TITLE);
				settingScene = new Scene(settingRoot);
				settingScene.getStylesheets().add(getClass().getResource("MameCommentSetting.css").toExternalForm());
				settingStage.setScene(settingScene);

				settingStage.showingProperty().addListener((observable, oldvalue, newvalue)->{
					if(oldvalue==true && newvalue==false) {
						try {
							settingData.writeToIni();
						}catch(Exception e) {
							displayError("設定情報のファイルへの書き込みに失敗しました。",e);
							e.printStackTrace();
						}
						setAccountUserInfo();
						displaySettingButton.setText(SETTING_OPEN);
						displaySettingButton.getStyleClass().remove("push");
					}
				});
				settingStage.show();
				settingController.initAfterViewed(settingStage,settingData);
				displaySettingButton.setText(SETTING_CLOSE);
				displaySettingButton.getStyleClass().add("push");

			} catch(Exception ex) {
				displayError("設定画面の表示に失敗しました。",ex);
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 閉じるボタンが押されたときの挙動<br>
	 * 他のウインドウが開かれている場合は閉じ、iniファイルに設定を書き込み、cashedImagesディレクトリ配下のイメージファイルを削除する
	 */
	public void closeAll() {
		if(boardStage!=null && boardStage.isShowing()) {
			boardStage.close();
		}
		if(viewerStage!=null && viewerStage.isShowing()) {
			viewerStage.close();
		}
		if(settingStage!=null && settingStage.isShowing()) {
			settingStage.close();
		}
		try {
			settingData.writeToIni();
		}catch(Exception e) {
			displayError("設定情報のファイルへの書き込みに失敗しました。",e);
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
			displayWarning("キャッシュファイルの削除に失敗しました。cachedImagesフォルダの権限や、その内部のファイルの権限を確認してください。");
			e.printStackTrace();
		}
	}

	/**
	 * トークンをもとにアカウントの各情報を設定する
	 *
	 */
	public void setAccountUserInfo() {
		if(settingData.token!=null&&!settingData.token.equals("")) {
			try {
				ArrayList<String> accountUserInfo=TwitCastingApiWrapper.getSelfData(settingData.token);
				write_debug_log("userName:"+accountUserInfo.get(0)+",userId:"+accountUserInfo.get(1)+",imageUrl:"+accountUserInfo.get(2));
				accountUserName.setText(accountUserInfo.get(0));
				accountUserId.setText(accountUserInfo.get(1));
				accountUserImage.setImage(new Image(accountUserInfo.get(2)));
			}catch(Exception e) {
				displayWarning("アカウント情報の取得に失敗しました。トークンの有効期限が切れている可能性があります。");
				e.printStackTrace();
				accountUserName.setText("token error");
				accountUserId.setText("token error");
				File tmp=new File("blank.jpg");
				accountUserImage.setImage(new Image("file:"+tmp.getAbsolutePath()));
			}
		}else {
			displayWarning("トークン情報が未登録です。設定画面よりトークンの取得を行ってください。");
			accountUserName.setText("no token");
			accountUserId.setText("no token");
			File tmp=new File("blank.jpg");
			accountUserImage.setImage(new Image("file:"+tmp.getAbsolutePath()));
		}
	}

	/**
	 * コメントの受信をコントロールをするメソッド<br>
	 * ログ出力ONであればファイルにログを出力する<br>
	 * ボードが表示されていればボードにコメント表示をリクエストする<br>
	 * ビューワーが表示されていればビューワーにコメント表示をリクエストする<br>
	 * @param commentsArray
	 */
	public void sendComment(ArrayList<ArrayList<String>> commentsArray) {
		write_debug_log("Send comment request received.");
		if(settingData.logFlg==true&&!settingData.logPath.contentEquals("")) {
			for(int i=0;i<commentsArray.size();i++) {
				write_log("Comment received. Time:"+commentsArray.get(i).get(0)+",User:"+commentsArray.get(i).get(2)+",Comment:"+commentsArray.get(i).get(3));
				write_file("Comment received. Time:"+commentsArray.get(i).get(0)+",User:"+commentsArray.get(i).get(2)+",Comment:"+commentsArray.get(i).get(3),settingData.logPath);
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

	/**
	 * コメントの送信をコントロールするメソッド<br>
	 * TwitCastingApiWrapperのpostCommentDataを呼び出す
	 * @param comment コメント本文
	 * @param sns SNS連携方式
	 * @return 書き出し結果をbooleanで応答
	 */
	public boolean postComment(String comment,String sns) {
		write_debug_log("Post comment request received. comment:"+comment+", sns:"+sns+",to "+getThread.movieId);
		return TwitCastingApiWrapper.postCommentData(getThread.movieId, comment, sns, settingData.token);
	}


	/**
	 * このプログラムについてを表示するメソッド
	 */
	@FXML
	public void displayAbout() {
		try {
			Stage aboutStage=new Stage();
			aboutStage.initModality(Modality.APPLICATION_MODAL);
			FXMLLoader aboutLoader = new FXMLLoader(getClass().getResource("MameCommentAbout.fxml"));
			Parent aboutRoot = aboutLoader.load();
			aboutStage.setTitle("About MameComment");
			Scene aboutScene = new Scene(aboutRoot);
			aboutScene.getStylesheets().add(getClass().getResource("MameCommentAbout.css").toExternalForm());
			aboutStage.setScene(aboutScene);
			aboutStage.show();
		}catch(Exception ex) {
			displayError("このプログラムについての画面の表示に失敗しました。",ex);
			ex.printStackTrace();
		}
	}
}
