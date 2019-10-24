package org.eda;

import static org.eda.MameCommentCommon.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
	@FXML CheckBox snsCheckBox,repCheckBox;
	@FXML Label snsLabel,repLabel;


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
		inputTextArea.setStyle("-fx-text-fill: #"+mameCommentSettingData.viewerFontColor.substring(2,8)+";");
		inputTextArea.lookup(".content").setStyle("-fx-background-color:#"+mameCommentSettingData.viewerBgColor.substring(2,8)+";");

		snsLabel.setStyle("-fx-background-color:#"+mameCommentSettingData.viewerBgColor.substring(2,8)+";-fx-text-fill: #"+mameCommentSettingData.viewerFontColor.substring(2,8)+";");
		repLabel.setStyle("-fx-background-color:#"+mameCommentSettingData.viewerBgColor.substring(2,8)+";-fx-text-fill: #"+mameCommentSettingData.viewerFontColor.substring(2,8)+";");

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

		//add size change listener
		viewerStage.widthProperty().addListener(new ChangeListener<Number>() {
			final Timer timer=new Timer();
			TimerTask task=null;
			final long delayTime=200;
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				if(task!=null) {
					task.cancel();
				}
				task=new TimerTask() {
					@Override
					public void run() {
						writeDebugLog("Adjust column size");
						double tmpTableWidth = commentTableView.getWidth();
						double tmpTimeColumnWidth = timeTableColumn.getWidth();
						double tmpImageColumnWidth = imageTableColumn.getWidth();
						double tmpNameColumnWidth = nameTableColumn.getWidth();
						double tmpCommentColumnWidth = commentTableColumn.getWidth();
						double tmpScrollBarWidth=scrollBar.getWidth()+5;//ちょっとずれてる
						double tmpTotalColumnWidth=tmpTimeColumnWidth+tmpImageColumnWidth+tmpNameColumnWidth+tmpNameColumnWidth;
						if(tmpTableWidth>=tmpTotalColumnWidth+scrollBar.getWidth()) {
							commentTableColumn.setPrefWidth(tmpTableWidth-tmpTimeColumnWidth-tmpImageColumnWidth-tmpNameColumnWidth-tmpScrollBarWidth);
						}else {
							timeTableColumn.setPrefWidth((tmpTableWidth-tmpScrollBarWidth)/tmpTotalColumnWidth*tmpTimeColumnWidth);
							imageTableColumn.setPrefWidth((tmpTableWidth-tmpScrollBarWidth)/tmpTotalColumnWidth*tmpImageColumnWidth);
							nameTableColumn.setPrefWidth((tmpTableWidth-tmpScrollBarWidth)/tmpTotalColumnWidth*tmpNameColumnWidth);
							commentTableColumn.setPrefWidth((tmpTableWidth-tmpScrollBarWidth)/tmpTotalColumnWidth*tmpCommentColumnWidth);
						}
					}
				};
				timer.schedule(task, delayTime);
			}
		});

		commentTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 2) {
					Node node=((Node)event.getTarget()).getParent();
					TableRow<MameCommentViewerUser> row;
					if(node instanceof TableRow) {
						row=(TableRow<MameCommentViewerUser>)node;
					}else {
						node=node.getParent();
						if(node instanceof TableRow) {
							row=(TableRow<MameCommentViewerUser>)node;
						}else {
							//not row object
							return;
						}
					}
					if(!inputTextArea.getText().equals("")) {
						inputTextArea.setText(inputTextArea.getText()+" @"+row.getItem().screen_name+" ");
					}else {
						inputTextArea.setText("@"+row.getItem().screen_name+" ");
					}
				}
			}
		});
	}

	public void sendButtonClicked(ActionEvent evt) {
		if(!inputTextArea.getText().equals("")) {
			String snsOption="none";
			if(snsCheckBox.isSelected()) {
				snsOption="normal";
			}else if(repCheckBox.isSelected()) {
				snsOption="reply";
			}
			writeDebugLog("Message send with sns option "+snsOption+". Message:"+inputTextArea.getText());
			mameCommentMainController.postComment(inputTextArea.getText(), snsOption);
			inputTextArea.setText("");
		}
	}

	public void setComment(ArrayList<ArrayList<String>> commentsArray) {
		double beforeBarPosition=scrollBar.getValue();
		for(int i=0;i<commentsArray.size();i++) {
			MameCommentViewerImage image=new MameCommentViewerImage(commentsArray.get(i).get(2),mameCommentSettingData.viewerImageSize,imageMap);
			commentTableView.getItems().add(new MameCommentViewerUser(commentsArray.get(i).get(1),commentsArray.get(i).get(3),commentsArray.get(i).get(4),image,commentsArray.get(i).get(5)));
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

	public void snsChecked(ActionEvent evt){
		if(repCheckBox.isSelected()) {
			repCheckBox.setSelected(false);
		}
	}
	public void repChecked(ActionEvent evt){
		if(snsCheckBox.isSelected()) {
			snsCheckBox.setSelected(false);
		}
	}


}
