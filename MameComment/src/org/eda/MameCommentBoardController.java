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


/**
 * ボード表示のコントローラークラス
 * @author AA337121
 * @version 0.6
 */
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
	/**
	 * 初期化メソッドのオーバーライド<br>
	 * マウスが右、下の端に来たときのイベントを定義<br>
	 * 最前面表示のチェックボックスに対するアクションイベントを定義
	 */
	public void initialize(URL location, ResourceBundle resources) {
		xFlg=false;
		yFlg=false;

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

	/**
	 * 画面の表示後に各項目を調整するメソッド<br>
	 * 画面の場所、サイズ、背景色を定義するとともに、<br>
	 * マウスを押下、ドラッグした時のイベントを定義する<br>
	 * playFlgの初期化もここで行う
	 * @param bs 呼び出し元のBoardStage自身
	 * @param mcsData 設定情報
	 */
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

		playFlag=new int[mameCommentSettingData.boardLineNum];
		for(int i=0;i<playFlag.length;i++) {
			playFlag[i]=0;
		}

	}

	/**
	 * ArrayList形式で受信したコメントを、playMessage()で発行する
	 * なお、表示は20ms間隔で実行する。(ArrayListが最大50件/sのため)
	 * @param commentArray コメントのリスト
	 */
	public void playRequest(ArrayList<ArrayList<String>> commentArray) {
		write_debug_log("Start to play.");
		for(int i=0;i<commentArray.size();i++) {
			try {
				write_debug_log("Play message:"+commentArray.get(i).get(4));
				playMessage(commentArray.get(i).get(4));
				Thread.sleep(20);
			}catch(Exception e) {
				//おそらくここには来ないので、StackTraceのみ出力する
				e.printStackTrace();
			}
		}
	}

	/**
	 * コメントをボードに表示する
	 * 設定内容を参照して、フォント、行数、色を決定して表示する
	 * 表示は5秒で行う
	 * 表示位置はgetHeight()によって取得するが、複数行メッセージで、下にはみ出てしまうような場合は上に補正する
	 * @param message メッセージ本文
	 */
	public void playMessage(String message) {
		double boardHeight=boardData.getHeight();
		double fontHeight=boardHeight/11;
		fontSize=(int)Math.floor(fontHeight);

		char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
		byte[] dataBytes=message.getBytes();
		char[] hexChars=new char[dataBytes.length*2];
		for(int i=0;i<dataBytes.length;i++) {
			int v=dataBytes[i]&0xFF;
			hexChars[i*2]=HEX_ARRAY[v >>> 4];
			hexChars[i*2+1]=HEX_ARRAY[v&0x0F];
		}
		String tmp=new String(hexChars);
		write_debug_log("message "+message);
		write_debug_log("hex "+tmp);

		Text com=new Text(message);
		com.setFill(Color.web(mameCommentSettingData.boardFontColor.substring(0,8)));
		com.setFont(Font.font(mameCommentSettingData.boardFontName,FontWeight.BOLD,fontSize));
		double commentWidth = com.getLayoutBounds().getWidth();
		double commentHeight = fontSize*0.95;
		TranslateTransition commentTransition = new TranslateTransition(Duration.seconds(5),com);
		commentTransition.setFromX(boardData.getWidth());
		commentTransition.setToX(-commentWidth);
		commentTransition.setInterpolator(Interpolator.LINEAR);
		Group comGroup = new Group(com);
		//\nの数を数えて、表示可能な位置を割り出す
		int height=getHeight(-1,message);
		comGroup.setLayoutY(commentHeight*(height+1));
		boardData.getChildren().add(comGroup);
		commentTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        		int comIndex=boardData.getChildren().indexOf(comGroup);
            	boardData.getChildren().remove(comIndex);
            	getHeight(height,"/remove play counts.");
            }
        });
		commentTransition.play();
	}

	@FXML
	/**
	 * 閉じるボタンをクリックしたときの動作<br>
	 * 透過設定ができるようにしているため、xボタンが表示されないための対処
	 * @param e
	 */
	public void closeClicked(ActionEvent e) {
		boardStage.close();
	}

	@FXML
	/**
	 * 透過ボタンをクリックしたときの動作<br>
	 * 透過したり、元の色に戻したりする
	 * @param e
	 */
	public void changeTransClicked(ActionEvent e) {
		if(isTrans==0) {
			write_debug_log("Change to transparent.");
			//透過ウインドウの設定変更を行う
			boardStage.getScene().setFill(null);
			boardData.setStyle("-fx-background-color: rgba(0,0,0,0);");
			boardParent.setStyle("-fx-background-color: rgba(0,0,0,0);");
			isTrans=1;
		}else {
			write_debug_log("Change to #"+mameCommentSettingData.boardBgColor.substring(2,8));
			//透過しない設定に戻す
			boardData.setStyle("-fx-background-color: #"+mameCommentSettingData.boardBgColor.substring(2,8)+";");
			boardParent.setStyle("-fx-background-color: WHITE;");
			boardStage.getScene().setFill(Color.WHITE);
			isTrans=0;
		}
	}

	/**
	 * 表示位置を取得する<br>
	 * 指定位置が-1(任意表示)の場合、なるべく上から順番に使用できるように、playFlg配列の一番小さい物を探して、そこをカウントアップして番号を応答する<br>
	 * 行を指定して表示したい場合は該当の場所をカウントアップして番号を応答する<br>
	 * ただしこれだとメッセージが複数行に渡る場合切れてしまうかもしれないので、改行の数を数えて、表示したときに切れそうなら上にずらす対応を行う<br>
	 * また、playが終わったあとの減算も当メソッドで行ってしまう<br>
	 * その際は行を指定、メッセージに/remove play counts.を入力させる(なので、行指定ロジック実装時にはその文字列が来た場合に変換をかける処理を入れる必要がある)
	 * @param height 指定位置
	 * @param message 表示したいメッセージ
	 * @return
	 */
	private synchronized int getHeight(int height,String message) {
		int result=0;
		int min=0;
		int count=(int)(message.chars().filter(ch -> ch == '\n').count());
		if (height==-1) {
			for (int i=0;i<playFlag.length;i++) {
				if(playFlag[min]>playFlag[i]) {
					min=i;
				}
			}
			if(count+min>=mameCommentSettingData.boardLineNum) {
				result=mameCommentSettingData.boardLineNum-count-1;
				if(result<0) {
					result=0;
				}
			}else {
				result=min;
			}
			playFlag[result]=playFlag[result]+1;
		}else {
			if(message.equals("/remove play counts.")) {
				playFlag[height]=playFlag[height]-1;
			}else {
				playFlag[height]=playFlag[height]+1;
				result=height;
			}
		}
		return result;
	}

/*過去ロジックのバックアップ
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
			playFlag[height]=playFlag[height]+1;
			result=height;
		}
		return result;
	}
 	private int chkCount(String message) {
		int result=0;
		int count=(int)(message.chars().filter(ch -> ch == '\n').count());
		int height=getHeight(-1);
		if(count+height>=mameCommentSettingData.boardLineNum) {
			if(height+count>=mameCommentSettingData.boardLineNum) {
				result=mameCommentSettingData.boardLineNum-count-1;
				if(result<0) {
					result=0;
				}
			}
			else result=height;
		}
		return result;
	}
*/

}
