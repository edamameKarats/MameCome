package org.eda;

import static org.eda.MameCommentCommon.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Twicasの認証を通すためのreceiver<br>
 * 一瞬8080及び8081のポートをバインドするため、脆弱性になりかねないため、注意して利用する
 * @author AA337121
 * @version 0.6
 */
public class MameCommentAuthReceiver {

	private MameCommentSettingController mameCommentSettingController;

	private HttpServer server;
	private HttpServer server2;

	private ExecutorService exec1;
	private ExecutorService exec2;

	/**
	 * コンストラクタ
	 * @param mcsController 呼び出し元の、settingController自身を指定
	 */
	public MameCommentAuthReceiver(MameCommentSettingController mcsController){
		mameCommentSettingController=mcsController;

	}

	/**
	 * Twitter認証のためのHttpServerを作成するメソッド<br>
	 * #以降のアンカーは通常Javaだと取得できないので、一旦8080で受けた後にjavascriptで取得し、リダイレクトの際に8081のページにクエリ文字列として渡す<br>
	 * クエリ文字列がリクエストに見える形で含まれる点、一時的とはいえ8080及び8081番のポートを開放してしまう点のセキュリティリスクがあることを考慮しながら使うこと
	 */
	public void service(){
		try {
			server = HttpServer.create(new InetSocketAddress(8080), 0);
			server2 = HttpServer.create(new InetSocketAddress(8081), 0);
			exec1=Executors. newFixedThreadPool(1);
			exec2=Executors. newFixedThreadPool(1);
			server.setExecutor(Executors.newCachedThreadPool());
			server2.setExecutor(Executors.newCachedThreadPool());
			server.createContext("/", exchange -> {
	            server2.start();

	            // レスポンスを返す
	            String responseText = "<html>\n" +
	            		"<script type=\"text/javascript\">\n" +
	            		"var urlHash=location.hash.replace(\"#\",\"?\");\n"+
	            		"window.location.replace(\"http://localhost:8081/receiver\"+urlHash);\n" +
	            		"</script>\n" +
	            		"<head>\n" +
	            		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
	            		"</head>\n" +
	            		"<body>\n" +
	            		"hello\n" +
	            		"</body>\n" +
	            		"</html>\n";
	            respond(exchange, responseText);

	            server.removeContext("/");
	            server.stop(1);
	            exec1.shutdownNow();
	        });
			server2.createContext("/receiver",exchange ->{
				String responseText="<html><body>Twitter認証が完了しました。<br>ウインドウを閉じて、アプリケーションに戻って下さい。</body></html>";
				respond(exchange, responseText);
	            server2.removeContext("/receiver");
				server2.stop(1);
				exec2.shutdownNow();
				mameCommentSettingController.replyUrlTextField.setText("http://localhost:8080/#"+exchange.getRequestURI().getQuery());
			});
			server.start();



		}catch(Exception e) {
			displayError("認証結果受領に失敗しました。",e);
			e.printStackTrace();
		}
	}

	/**
	 * exchangeにテキストを書き込み、応答を返す
	 * @param exchange createContextの際に使用されるexchange
	 * @param responseText 応答させるメッセージ
	 */
    private static void respond(HttpExchange exchange, String responseText){
        byte[] responseBody = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        try {
            exchange.sendResponseHeaders(200, responseBody.length);  // 明示的に返す必要あり
            exchange.getResponseBody().write(responseBody);

        }catch(IOException ex){
        	displayError("認証結果の応答メッセージ作成に失敗しました。",ex);
            ex.printStackTrace();
        }
    }
}
