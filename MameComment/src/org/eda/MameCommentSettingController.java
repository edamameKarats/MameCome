package org.eda;

import static org.eda.MameCommentCommon.*;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MameCommentSettingController implements Initializable{

	@FXML Button authButton,saveButton,canselButton;
	@FXML TextField replyUrlTextField,logPathTextField,boardXTextField,boardYTextField,boardWidthTextField,boardHeightTextField,boardLineNumTextField,viewerXTextField,viewerYTextField,viewerWidthTextField,viewerHeightTextField,viewerFontSizeTextField,viewerImageSizeTextField;
	@FXML CheckBox logFlgCheckBox;
	@FXML ComboBox<String> boardFontNameComboBox,viewerFontNameComboBox;
	@FXML ColorPicker boardFontColorPicker,boardBgColorPicker,viewerFontColorPicker,viewerBgColorPicker;

	Stage settingStage;

	MameCommentSettingData mameCommentSettingData;
	MameCommentAuthReceiver receiver;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		boardFontNameComboBox.getItems().addAll(Font.getFamilies());
		viewerFontNameComboBox.getItems().addAll(Font.getFamilies());
	}

	public void initAfterViewed(Stage mcsStage,MameCommentSettingData mcsData) {
		settingStage=mcsStage;
		mameCommentSettingData=mcsData;
		setFormDataFromSetting();
	}

	public void setFormDataFromSetting() {
		replyUrlTextField.setText(mameCommentSettingData.replyUrl);
		logFlgCheckBox.setSelected(mameCommentSettingData.logFlg);
		logPathTextField.setText(mameCommentSettingData.logPath);
		boardXTextField.setText(String.valueOf(mameCommentSettingData.boardX));
		boardYTextField.setText(String.valueOf(mameCommentSettingData.boardY));
		boardWidthTextField.setText(String.valueOf(mameCommentSettingData.boardWidth));
		boardHeightTextField.setText(String.valueOf(mameCommentSettingData.boardHeight));
		boardFontNameComboBox.setValue(mameCommentSettingData.boardFontName);
		boardLineNumTextField.setText(String.valueOf(mameCommentSettingData.boardLineNum));
		boardFontColorPicker.setValue(Color.valueOf(mameCommentSettingData.boardFontColor));
		boardBgColorPicker.setValue(Color.valueOf(mameCommentSettingData.boardBgColor));
		viewerXTextField.setText(String.valueOf(mameCommentSettingData.viewerX));
		viewerYTextField.setText(String.valueOf(mameCommentSettingData.viewerY));
		viewerWidthTextField.setText(String.valueOf(mameCommentSettingData.viewerWidth));
		viewerHeightTextField.setText(String.valueOf(mameCommentSettingData.viewerHeight));
		viewerFontNameComboBox.setValue(mameCommentSettingData.viewerFontName);
		viewerFontSizeTextField.setText(String.valueOf(mameCommentSettingData.viewerFontSize));
		viewerFontColorPicker.setValue(Color.valueOf(mameCommentSettingData.viewerFontColor));
		viewerBgColorPicker.setValue(Color.valueOf(mameCommentSettingData.viewerBgColor));
		viewerImageSizeTextField.setText(String.valueOf(mameCommentSettingData.viewerImageSize));
	}

	public void setSettingFromFormData() {
		mameCommentSettingData.replyUrl=replyUrlTextField.getText();
		mameCommentSettingData.setToken();
		mameCommentSettingData.logFlg=logFlgCheckBox.isSelected();
		mameCommentSettingData.logPath=logPathTextField.getText();
		mameCommentSettingData.boardX=Double.parseDouble(boardXTextField.getText());
		mameCommentSettingData.boardY=Double.parseDouble(boardYTextField.getText());
		mameCommentSettingData.boardWidth=Double.parseDouble(boardWidthTextField.getText());
		mameCommentSettingData.boardHeight=Double.parseDouble(boardHeightTextField.getText());
		mameCommentSettingData.boardFontName=boardFontNameComboBox.getValue();
		mameCommentSettingData.boardLineNum=Integer.parseInt(boardLineNumTextField.getText());
		mameCommentSettingData.boardFontColor=boardFontColorPicker.getValue().toString();
		mameCommentSettingData.boardBgColor=boardBgColorPicker.getValue().toString();
		mameCommentSettingData.viewerX=Double.parseDouble(viewerXTextField.getText());
		mameCommentSettingData.viewerY=Double.parseDouble(viewerYTextField.getText());
		mameCommentSettingData.viewerWidth=Double.parseDouble(viewerWidthTextField.getText());
		mameCommentSettingData.viewerHeight=Double.parseDouble(viewerHeightTextField.getText());
		mameCommentSettingData.viewerFontName=viewerFontNameComboBox.getValue();
		mameCommentSettingData.viewerFontSize=Double.parseDouble(viewerFontSizeTextField.getText());
		mameCommentSettingData.viewerFontColor=viewerFontColorPicker.getValue().toString();
		mameCommentSettingData.viewerBgColor=viewerBgColorPicker.getValue().toString();
		mameCommentSettingData.viewerImageSize=Double.parseDouble(viewerImageSizeTextField.getText());
	}



	@FXML
	public void authButtonClicked(ActionEvent evt) {
		try {
			Desktop.getDesktop().browse(new URI(authURL));
			receiver=new MameCommentAuthReceiver(mameCommentSettingData,this);
			receiver.service();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void saveButtonClicked(ActionEvent evt) {
		setSettingFromFormData();
		settingStage.close();
	}
	@FXML
	public void canselButtonClicked(ActionEvent evt) {
		settingStage.close();
	}

}
