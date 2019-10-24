package org.eda;

import static org.eda.MameCommentCommon.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MameCommentViewerController implements Initializable{

	@FXML TableView<MameCommentViewerUser> commentTableView;
	@FXML TableColumn<MameCommentViewerUser,String> timeTableColumn;
	@FXML TableColumn<MameCommentViewerUser,MameCommentViewerImage> imageTableColumn;
	@FXML TableColumn<MameCommentViewerUser,String> nameTableColumn;
	@FXML TableColumn<MameCommentViewerUser,String> commentTableColumn;
	@FXML TextArea inputTextArea;
	@FXML Button sendButton;

	private Stage viewerStage;
	private MameCommentMainController mameCommentMainController;
	private MameCommentSettingData mameCommentSettingData;
	private ScrollBar scrollBar;

	private HashMap<String,String> imageMap;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		imageMap=new HashMap<String,String>();
	}

	public void initAfterViewed(Stage vs,MameCommentMainController mcmController,MameCommentSettingData mcsData) {
		viewerStage=vs;
		mameCommentMainController=mcmController;
		mameCommentSettingData=mcsData;

		viewerStage.setX(mameCommentSettingData.viewerX);
		viewerStage.setY(mameCommentSettingData.viewerY);
		viewerStage.setWidth(mameCommentSettingData.viewerWidth);
		viewerStage.setHeight(mameCommentSettingData.viewerHeight);
		timeTableColumn.setPrefWidth(mameCommentSettingData.viewerTimeWidth);
		imageTableColumn.setPrefWidth(mameCommentSettingData.viewerImageWidth);
		nameTableColumn.setPrefWidth(mameCommentSettingData.viewerNameWidth);
		commentTableColumn.setPrefWidth(mameCommentSettingData.viewerCommentWidth);

		timeTableColumn.setStyle("-fx-background-color:#"+mameCommentSettingData.viewerBgColor.substring(2,8)+";-fx-text-fill: #"+mameCommentSettingData.viewerFontColor.substring(2,8)+";");
		imageTableColumn.setStyle("-fx-background-color:#"+mameCommentSettingData.viewerBgColor.substring(2,8)+";-fx-text-fill: #"+mameCommentSettingData.viewerFontColor.substring(2,8)+";");
		nameTableColumn.setStyle("-fx-background-color:#"+mameCommentSettingData.viewerBgColor.substring(2,8)+";-fx-text-fill: #"+mameCommentSettingData.viewerFontColor.substring(2,8)+";");
		commentTableColumn.setStyle("-fx-background-color:#"+mameCommentSettingData.viewerBgColor.substring(2,8)+";-fx-text-fill: #"+mameCommentSettingData.viewerFontColor.substring(2,8)+";");
		commentTableView.setStyle("-fx-background-color:#"+mameCommentSettingData.viewerBgColor.substring(2,8)+";");
		inputTextArea.setStyle("-fx-text-fill: #"+mameCommentSettingData.viewerFontColor.substring(2,8)+";");
		inputTextArea.lookup(".content").setStyle("-fx-background-color:#"+mameCommentSettingData.viewerBgColor.substring(2,8)+";");

		//Wrap setting
		commentTableColumn.setCellFactory(param ->{
			return new TableCell<MameCommentViewerUser,String>(){
				@Override
				protected void updateItem(String item,boolean empty){
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						Text text = new Text(item);
						text.setWrappingWidth(param.getPrefWidth());
						setGraphic(text);
					}
				}
			};
		});

		//Label when no item is registered.
		Label tmpLabel=new Label("");
		commentTableView.setPlaceholder(tmpLabel);

		//get scrollbar
		Set<Node> nodes=commentTableView.lookupAll(".scroll-bar");
		scrollBar=null;
		for(Node node:nodes) {
			if(node instanceof ScrollBar) {
				ScrollBar tmpScrollBar = (ScrollBar) node;
				if (tmpScrollBar.getOrientation() == Orientation.VERTICAL) {
	                scrollBar=tmpScrollBar;
	            }
			}
		}
		scrollBar.visibleProperty().addListener((observable,oldValue,newValue)->{
			if(oldValue==false&&newValue==true) {
				int count=commentTableView.getItems().size();
				commentTableView.scrollTo(count);
			}
		});
	}

	public void sendButtonClicked(ActionEvent evt) {
		if(!inputTextArea.getText().equals("")) {
			writeDebugLog("Message send. "+inputTextArea.getText());
			mameCommentMainController.postComment(inputTextArea.getText(), "none");
			inputTextArea.setText("");
		}
	}

	public void setComment(ArrayList<ArrayList<String>> commentsArray) {
		double beforeBarPosition=scrollBar.getValue();
		for(int i=0;i<commentsArray.size();i++) {
			MameCommentViewerImage image=new MameCommentViewerImage(commentsArray.get(i).get(2),mameCommentSettingData.viewerImageSize,imageMap);
			commentTableView.getItems().add(new MameCommentViewerUser(commentsArray.get(i).get(1),commentsArray.get(i).get(3),commentsArray.get(i).get(4),image));
		}
		timeTableColumn.setCellValueFactory(new PropertyValueFactory<MameCommentViewerUser, String>("time"));
		nameTableColumn.setCellValueFactory(new PropertyValueFactory<MameCommentViewerUser, String>("name"));
		commentTableColumn.setCellValueFactory(new PropertyValueFactory<MameCommentViewerUser, String>("comment"));
		imageTableColumn.setCellValueFactory(new PropertyValueFactory<MameCommentViewerUser, MameCommentViewerImage>("image"));
		imageTableColumn.setCellFactory(new Callback<TableColumn<MameCommentViewerUser,MameCommentViewerImage>,TableCell<MameCommentViewerUser, MameCommentViewerImage>>(){
			@Override
			public TableCell<MameCommentViewerUser,MameCommentViewerImage> call(TableColumn<MameCommentViewerUser,MameCommentViewerImage> param) {
				TableCell<MameCommentViewerUser,MameCommentViewerImage> cell = new TableCell<MameCommentViewerUser,MameCommentViewerImage>(){
					@Override
					public void updateItem(MameCommentViewerImage item, boolean empty) {
						if(item!=null){
							ImageView imageView = new ImageView();
							imageView.setImage(new Image("file:"+item.getFileName(),item.getImageSize(),0,true,false));
							setGraphic(imageView);
						}
					}
				};
				return cell;
			}
		});
		if(beforeBarPosition==1) {
			int count=commentTableView.getItems().size();
			commentTableView.scrollTo(count);
		}
	}



}
