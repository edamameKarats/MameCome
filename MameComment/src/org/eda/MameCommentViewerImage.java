package org.eda;

import static org.eda.MameCommentCommon.*;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.HashMap;

public class MameCommentViewerImage {
	private String fileName;
	private Double imageSize;

	public MameCommentViewerImage(String aFileUrl,Double aSize,HashMap<String,String> aImageMap) {
		fileName=aImageMap.get(aFileUrl);
		writeDebugLog("fileName is "+fileName);
		//fileNameが見つからない場合、URLの最後から２番目が多分固有なのでそこから引っ張ってきてファイル名を生成
		//URLからファイルをダウンロードしてきてcachedImagesに保存
		//次回からはそのファイルを使用するので、ダウンロードは１回だけのはず
		if(fileName==null){
			writeDebugLog("fileName is not found. get from url");
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
				dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream("cachedImages/"+tmpFileName)));
				byte[] b=new byte[4096];
				int readByte=0;

				while(-1 != (readByte=dis.read(b))) {
					dos.write(b,0,readByte);
				}
				fileName=Paths.get("cachedImages/"+tmpFileName).toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
				aImageMap.put(aFileUrl, fileName);
			}catch(Exception e) {
				//エラーが起きた場合はblankファイルを使用するようにする。
				//次回もトライするので、Mapは更新しない
				writeDebugLog("some exception is occured during file get and save");
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

	public void setFileName(String aFileName) {
		fileName=aFileName;
	}
	public void setImageSize(Double aSize) {
		imageSize=aSize;
	}

	public String getFileName() {
		return fileName;
	}
	public Double getImageSize() {
		return imageSize;
	}
}
