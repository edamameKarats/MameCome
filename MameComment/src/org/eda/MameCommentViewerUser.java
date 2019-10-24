package org.eda;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MameCommentViewerUser {
    private StringProperty time;
    private StringProperty name;
    private StringProperty comment;
    private ObjectProperty<MameCommentViewerImage> image;
    public String screen_name;

    public MameCommentViewerUser(String aTime, String aName, String aComment, MameCommentViewerImage aImage, String aScreenName) {
    	time=new SimpleStringProperty(aTime);
    	name=new SimpleStringProperty(aName);
    	comment=new SimpleStringProperty(aComment);
        image = new SimpleObjectProperty<MameCommentViewerImage>(aImage);
        screen_name=aScreenName;
    }

    public StringProperty timeProperty() {
        return time;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public ObjectProperty<MameCommentViewerImage> imageProperty() {
    	return image;
    }
}
