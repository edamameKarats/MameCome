### Japanese Only. 
### If you need English one, please translate yourself.
##################################################
#
# ソフト名  ： MameCome(仮)
# 著作権者  ： 枝豆の人
# 製作日    ： 2018/05/03
# 種別      ： フリーソフトウェア
# 連絡先    ： Twitter:edamameKarats
#              edamame.karats@gmail.com
#              ※Twitterメインです。
# 配布元    ： http://edamame-karats.seesaa.net/
# 転載      ： 不可
# 動作環境  ： Java8.0以上の環境
#              ※Windows10でのみテストをしています
#                Mac等では文字コード・改行コード
#                の問題が発生するかもしれません
# 
##################################################

≪著作権および免責事項≫

　本ソフトはフリーソフトですが、営利目的での使用は禁止とさせていただきます。
　なお、著作権は作者である枝豆の人に帰属します。

　このソフトウェアを使用したことによって生じたすべての障害・損害・不具
　合等に関しては、私と私の関係者および私の所属するいかなる団体・組織とも、
　一切の責任を負いません。各自の責任においてご使用ください。
  特に、テストは十分に行えているわけではありません。
  ご理解いただける方のみご利用ください。

・はじめに
　MameCome(仮)は、TwitCastingのコメント情報を取得し、
　それを表示するためのプログラムです。
　　主な特徴：
　　　1) idを指定し、一定間隔でコメント情報を取得する
　　　2) 取得したコメントをプログラム上のウインドウで、右から左に流れるように表示する

・ファイル構成
　MameCome.jar  (プログラム本体)
　MameCome.ini　(定義ファイル)

・導入、設定方法
　ファイルをすべて同一のフォルダに配置してください。
　また、起動の前提はJava8以上の環境になりますので、
　必ずJava8以上のランタイムを導入しておいてください。

　acc_tokenの設定
　　以下のURLをコピー&ペーストし、ブラウザに張り付けてください。
　　https://apiv2.twitcasting.tv/oauth2/authorize?client_id=2671027374.f688b31afb4ae712724112e8ff419ae4c376a54f35f762bc047403ef6e56918d&response_type=token
　　認証画面でID等を入力すると、http://localhost:8080にリダイレクトされ、エラー画面が表示されるはずです。
　　その際の、ブラウザ上のアドレスをすべてコピーし、iniファイルのacc_tokenに設定してください。

　reloadの設定
　　コメントのリロード間隔です、1以上の数字で設定してください。

　fontの設定
　　使用するフォントの設定です。各環境で使用可能なフォントを指定してください。
　　なお、テストではＭＳ　ゴシックを使用しています。
　　フォントのサイズによっては各行のコメントが重なって表示されたりするかもしれません。
  　※この機能はうまく動かないかもしれません。

　xpos、yposの設定
　　基本的には設定不要です。
　　プログラム終了時に現在のウインドウ位置をもとに更新します。
　　ウインドウがどこかに行って見つからなくなった場合、それぞれ0を入力すると解決するかもしれません。

　width、heightの設定
　　当プログラムはドラッグによるウインドウサイズ変更ができません。
　　希望するサイズを入力してください。
　　0はデフォルトサイズとして扱われます。
　　メニュー表示などの関係から、656*424以上のサイズに設定すると良いと思います。

・使用方法
　MameCome.jarをダブルクリックしてください。
　コメントを取得したいユーザー名を指定して、取得開始をクリックしてください。
　放送されていない場合は取得が開始されません。
　コメント部分を透過し、ツイキャス画面に重ねたい場合は、透過切り替えボタンをクリックしてください。(戻す場合も同じ)
　アプリケーションを終了する場合は、アプリケーションの終了ボタンをクリックしてください。
　ツイキャス画面に重ねた場合、常に最前面にコメントウインドウを表示したい場合はチェックボックスをONにしてください。

・更新履歴
　ver0.1	2018/05/03
　　公開するつもりだったがやめた版
　ver0.2	2018/05/05
　　movieIdではなく、ユーザー名で取得、ライブ配信していない場合は処理を行わないように修正
　　メッセージ表示部の透過ウインドウ対応
　　上記に伴い、ウインドウサイズの変更不可化
　　上記に伴い、アプリケーションの終了ボタン実装
　　上記に伴い、ボタン以外の部分のドラッグでのウインドウ移動機能実装
　　常に最前面に表示する機能追加
　ver0.3	2018/05/06
　　ウインドウサイズの外部パラメータ化
  

