package org.eda;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * テーブルに表示するユーザーの情報を格納するクラス
 * @author AA337121
 * @version 0.6
 */

public class MameCommentViewerUser {
    private StringProperty time;
    private StringProperty name;
    private StringProperty comment;
    private ObjectProperty<MameCommentViewerImage> image;
    public String screen_name;//@ユーザーを登録する際の参照用

    /**
     * コンストラクタ
     * @param aTime コメント時刻
     * @param aName コメント者のユーザー名
     * @param aComment コメント
     * @param aImage コメント者の画像データ
     * @param aScreenName コメント者のscreen_name
     */
    public MameCommentViewerUser(String aTime, String aName, String aComment, MameCommentViewerImage aImage, String aScreenName) {
    	time=new SimpleStringProperty(aTime);
    	name=new SimpleStringProperty(aName);
    	comment=new SimpleStringProperty(aComment);
        image = new SimpleObjectProperty<MameCommentViewerImage>(aImage);
        screen_name=aScreenName;
    }

    /**
     * プロパティ参照(time)
     * @return time
     */
    public StringProperty timeProperty() {
        return time;
    }

    /**
     * プロパティ参照(name)
     * @return namee
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * プロパティ参照(comment)
     * @return comment
     */
    public StringProperty commentProperty() {
        return comment;
    }

    /**
     * プロパティ参照(image)
     * @return image
     */
    public ObjectProperty<MameCommentViewerImage> imageProperty() {
    	return image;
    }
}
