package org.eda;

import static org.eda.MameCommentCommon.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MameCommentBoardController implements Initializable{

	@FXML AnchorPane boardParent;
	@FXML AnchorPane boardMenu;
	@FXML AnchorPane boardData;
	@FXML Button closeButton;
	@FXML CheckBox frontCheckBox;
	@FXML Button changeTransButton;

	private boolean xFlg;
	private boolean yFlg;
	private Stage boardStage;
	private double initStageX,initStageY,initStageWidth,initStageHeight,initEventX,initEventY,moveEventX,moveEventY,xDelta,yDelta;
	private int[] playFlag;
	private int isTrans;
	private int fontSize;

	public MameCommentSettingData mameCommentSettingData;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		xFlg=false;
		yFlg=false;
		playFlag=new int[10];
		for(int i=0;i<playFlag.length;i++) {
			playFlag[i]=0;
		}
		isTrans=0;
		boardParent.setOnMouseMoved(e->{
			if(e.getX()>=boardParent.getWidth()-10) {
				xFlg=true;
			}else {
				xFlg=false;
			}
			if(e.getY()>=boardParent.getHeight()-10) {
				yFlg=true;
			}else {
				yFlg=false;
			}
			if(xFlg==true&&yFlg==true) {
				boardParent.setCursor(Cursor.SE_RESIZE);
			}else if(xFlg==true) {
				boardParent.setCursor(Cursor.E_RESIZE);
			}else if(yFlg==true) {
				boardParent.setCursor(Cursor.S_RESIZE);
			}else {
				boardParent.setCursor(Cursor.DEFAULT);
			}
		});
		frontCheckBox.setOnAction((ActionEvent)->{
			boardStage.setAlwaysOnTop(frontCheckBox.isSelected());
		});

	}

	public void initAfterViewed(Stage bs,MameCommentSettingData mcsData) {
		boardStage=bs;
		mameCommentSettingData = mcsData;

		boardStage.setX(mameCommentSettingData.boardX);
		boardStage.setY(mameCommentSettingData.boardY);
		boardStage.setWidth(mameCommentSettingData.boardWidth);
		boardStage.setHeight(mameCommentSettingData.boardHeight);
		boardData.setStyle("-fx-background-color: #"+mameCommentSettingData.boardBgColor.substring(2,8)+";");

		boardStage.getScene().setOnMousePressed(event -> {
			initStageX=boardStage.getX();
			initStageY=boardStage.getY();
			initStageWidth=boardStage.getWidth();
			initStageHeight=boardStage.getHeight();
			initEventX=event.getScreenX();
			initEventY=event.getScreenY();
        });
		boardStage.getScene().setOnMouseDragged(event ->{
			moveEventX=event.getScreenX();
			moveEventY=event.getScreenY();
			xDelta=moveEventX - initEventX;
			yDelta=moveEventY - initEventY;
			if(xFlg==true&&yFlg==true) {
				boardStage.setWidth(initStageWidth+xDelta);
				boardStage.setHeight(initStageHeight+yDelta);
			}else if(xFlg==true) {
				boardStage.setWidth(initStageWidth+xDelta);
			}else if(yFlg==true) {
				boardStage.setHeight(initStageHeight+yDelta);
			}else {
				boardStage.setX(initStageX+xDelta);
				boardStage.setY(initStageY+yDelta);
			}
		});
	}

	public void playRequest(ArrayList<ArrayList<String>> commentArray) {
		writeDebugLog("Start to play.");
		for(int i=0;i<commentArray.size();i++) {
			try {
				writeDebugLog("Play message:"+commentArray.get(i).get(4));
				playMessage(commentArray.get(i).get(4));
				Thread.sleep(20);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void playMessage(String message) {
		double boardHeight=boardData.getHeight();
		double fontHeight=boardHeight/11;
		fontSize=(int)Math.floor(fontHeight);
		writeDebugLog("boardHeight:"+boardHeight+",fontSize:"+fontSize);

		Text com=new Text(message);
		System.out.println(mameCommentSettingData.boardFontColor);
		com.setFill(Color.web(mameCommentSettingData.boardFontColor.substring(0,8)));
		com.setFont(Font.font(mameCommentSettingData.boardFontName,FontWeight.BOLD,fontSize));
//		com.setStroke(Color.BLACK);
		double commentWidth = com.getLayoutBounds().getWidth();
		double commentHeight = fontSize*0.95;
		//com.getLayoutBounds().getHeight();
		TranslateTransition commentTransition = new TranslateTransition(Duration.seconds(5),com);
		commentTransition.setFromX(boardData.getWidth());
		commentTransition.setToX(-commentWidth);
		commentTransition.setInterpolator(Interpolator.LINEAR);
		Group comGroup = new Group(com);
		int height=getHeight(-1);
		comGroup.setLayoutY(commentHeight*(height+1));
		boardData.getChildren().add(comGroup);
		commentTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        		int comIndex=boardData.getChildren().indexOf(comGroup);
            	boardData.getChildren().remove(comIndex);
            	playFlag[height]=playFlag[height]-1;
            }
        });
		commentTransition.play();
	}

	@FXML
	public void closeClicked(ActionEvent e) {
		boardStage.close();
	}

	@FXML
	public void changeTransClicked(ActionEvent e) {
		if(isTrans==0) {
			writeDebugLog("Change to transparent.");
			//透過ウインドウの設定変更を行う
			boardStage.getScene().setFill(null);
			boardData.setStyle("-fx-background-color: rgba(0,0,0,0);");
			boardParent.setStyle("-fx-background-color: rgba(0,0,0,0);");
			isTrans=1;
		}else {
			writeDebugLog("Change to #"+mameCommentSettingData.boardBgColor.substring(2,8));
			//透過しない設定に戻す
			boardData.setStyle("-fx-background-color: #"+mameCommentSettingData.boardBgColor.substring(2,8)+";");
			boardParent.setStyle("-fx-background-color: WHITE;");
			boardStage.getScene().setFill(Color.WHITE);
			isTrans=0;
		}
	}

	private synchronized int getHeight(int height) {
		int result=0;
		int min=0;
		if (height==-1) {
			for (int i=0;i<playFlag.length;i++) {
				if(playFlag[min]>playFlag[i]) {
					min=i;
				}
			}
			result=min;
			playFlag[result]=playFlag[result]+1;
		}else {
			playFlag[height]=playFlag[height]-1;
			result=height;
		}
		return result;
	}

}
