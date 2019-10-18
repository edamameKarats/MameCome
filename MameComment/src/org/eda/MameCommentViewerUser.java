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

    public MameCommentViewerUser(String aTime, String aName, String aComment, MameCommentViewerImage aImage) {
    	time=new SimpleStringProperty(aTime);
    	name=new SimpleStringProperty(aName);
    	comment=new SimpleStringProperty(aComment);
        image = new SimpleObjectProperty<MameCommentViewerImage>(aImage);
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
