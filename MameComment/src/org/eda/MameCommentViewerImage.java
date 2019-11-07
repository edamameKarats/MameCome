package org.eda;

import static org.eda.MameCommentCommon.*;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * ビューワーにて表示する画像イメージに関するクラス
 * @author AA337121
 * @version 0.6
 */
public class MameCommentViewerImage {
	private String fileName;
	private Double imageSize;

	/**
	 * コンストラクタ
	 * @param aFileUrl 画像ファイルのURL
	 * @param aSize 画像表示のサイズ
	 * @param aImageMap 画像ファイルのURLと実イメージファイルのMAP
	 */
	public MameCommentViewerImage(String aFileUrl,Double aSize,HashMap<String,String> aImageMap) {
		fileName=aImageMap.get(aFileUrl);
		write_debug_log("fileName is "+fileName);
		//fileNameが見つからない場合、URLの最後から２番目が多分固有なのでそこから引っ張ってきてファイル名を生成
		//URLからファイルをダウンロードしてきてcachedImagesに保存
		//次回からはそのファイルを使用するので、ダウンロードは１回だけになる
		if(fileName==null){
			write_debug_log("fileName is not found. get from url");
			String[] urlString=aFileUrl.split("/");
			String tmpFileName=urlString[urlString.length-2]+".jpg";
			URL url=null;
			HttpURLConnection conn=null;
			int httpStatusCode;
			DataInputStream dis=null;
			DataOutputStream dos=null;
			try {
				url=new URL(aFileUrl);
				conn=(HttpURLConnection)url.openConnection();
				conn.setAllowUserInteraction(false);
				conn.setInstanceFollowRedirects(true);
				conn.setRequestMethod("GET");
				conn.connect();
				httpStatusCode = conn.getResponseCode();
				if(httpStatusCode != HttpURLConnection.HTTP_OK){
					throw new Exception();
				}
				dis=new DataInputStream(conn.getInputStream());
				write_debug_log("target file is cachedImages/"+tmpFileName);
				File tmpFile=new File("cachedImages/"+tmpFileName);
				write_debug_log("abs path is "+tmpFile.getAbsolutePath());
				dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream("cachedImages/"+tmpFileName)));
				byte[] b=new byte[4096];
				int readByte=0;

				while(-1 != (readByte=dis.read(b))) {
					dos.write(b,0,readByte);
				}
				fileName=Paths.get("cachedImages/"+tmpFileName).toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
				aImageMap.put(aFileUrl, fileName);
			}catch(Exception e) {
				displayError("画像ファイルのダウンロードに失敗しました。blankファイルを使用します。",e);
				e.printStackTrace();
				//エラーが起きた場合はblankファイルを使用するようにする。
				//次回もトライするので、Mapは更新しない
				fileName="blank.jpg";
			}finally {
				try {
					if(conn!=null)conn.disconnect();
				}catch(Exception e) {
				}
				try {
					if(dis!=null)dis.close();
				}catch(Exception e) {
				}
				try {
					if(dos!=null)dos.close();
				}catch(Exception e) {
				}
			}
		}
		imageSize=aSize;
	}

	/**
	 * filenameのSetter
	 * @param aFileName ファイル名
	 */
	public void setFileName(String aFileName) {
		fileName=aFileName;
	}

	/**
	 * imageSizeのSetter
	 * @param aSize 画像サイズ
	 */
	public void setImageSize(Double aSize) {
		imageSize=aSize;
	}

	/**
	 * fileNameのGetter
	 * @return fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * imageSizeのGetter
	 * @return imageSize
	 */
	public Double getImageSize() {
		return imageSize;
	}
}
