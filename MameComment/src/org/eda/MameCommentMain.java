package org.eda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MameCommentMain extends Application {
	@Override
	public void start(Stage mainStage) {
		try {
			FXMLLoader mainLoader=new FXMLLoader(getClass().getResource("MameCommentMain.fxml"));
			Parent mainRoot = mainLoader.load();
			MameCommentMainController mainController=mainLoader.getController();
			mainStage.setTitle("MameComment v0.6");
			Scene mainScene = new Scene(mainRoot);
			mainScene.getStylesheets().add(getClass().getResource("MameCommentMain.css").toExternalForm());
			mainStage.setScene(mainScene);
			mainStage.show();
			mainController.initAfterViewed();
			mainStage.showingProperty().addListener((observable, oldvalue, newvalue)->{
				if(oldvalue==true && newvalue==false) {
					mainController.closeAll();
					System.exit(0);
				}
			});

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
