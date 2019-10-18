package org.eda;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;


public class MameCommentAuthReceiver {

	private MameCommentSettingData mameCommentSettingData;
	private MameCommentSettingController mameCommentSettingController;

	public HttpServer server;
	public HttpServer server2;

	public MameCommentAuthReceiver(MameCommentSettingData mcsData,MameCommentSettingController mcsController){
		mameCommentSettingData=mcsData;
		mameCommentSettingController=mcsController;

	}


	public void service(){
		try {
			server = HttpServer.create(new InetSocketAddress(8080), 0);
			server2 = HttpServer.create(new InetSocketAddress(8081), 0);
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
	        });
			server2.createContext("/receiver",exchange ->{
				String responseText="<html><body>Twitter認証が完了しました。<br>ウインドウを閉じて、アプリケーションに戻って下さい。</body></html>";
				respond(exchange, responseText);
	            server2.removeContext("/receiver");
				server2.stop(1);
				mameCommentSettingController.replyUrlTextField.setText("http://localhost:8080/#"+exchange.getRequestURI().getQuery());
			});
			server.start();



		}catch(Exception e) {
			e.printStackTrace();
		}
	}

    private static void respond(HttpExchange exchange, String responseText){
        byte[] responseBody = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        try {
            exchange.sendResponseHeaders(200, responseBody.length);  // 明示的に返す必要あり
            exchange.getResponseBody().write(responseBody);

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
