Japanese Only.  
If you need English one, please translate yourself.

---
　ソフト名　： MameComment  
　著作権者　： 枝豆の人  
　製作日　　： 製作中  
　種別　　　： フリーソフトウェア  
　連絡先　　： Twitter:edamameKarats  
　　　　　　　edamame.karats@gmail.com  
　　　　　　　　※Twitterメインです。  
　配布元　　： http://edamame-karats.seesaa.net/  
　転載　　　： 不可  
　動作環境　： Java11以上の環境  

---

## ≪著作権および免責事項≫

　本ソフトはフリーソフトですが、営利目的での使用は禁止とさせていただきます。  
　なお、著作権は作者である枝豆の人に帰属します。  

　このソフトウェアを使用したことによって生じたすべての障害・損害・不具合等に関しては、私と私の関係者および私の所属するいかなる団体・組織とも、一切の責任を負いません。各自の責任においてご使用ください。  
  特に、テストは十分に行えているわけではありません。ご理解いただける方のみご利用ください。  
  
  また、当プログラムはTwitcastingのトークン認証を通す際に、一瞬ポート(8080/8081)を開放し、利用します。  
  セキュリティ上あまり好ましいとは言えない実装ですので、トークン認証の際は注意してください。
  (手動で実施いただけばこの問題は発生しません)　　
  
  当プログラムは、OpenJFX(https://openjfx.io/)を使用しています。  
  OpenJFXはCreativeCommonsのライセンス(CC-BY-NC)に従います。  
  詳細は以下を参照してください。  
  https://creativecommons.org/licenses/by-nc/4.0/

---

## はじめに
　MameCommentは、TwitCastingのコメント情報を取得し、それを表示するためのプログラムです。  
　　主な特徴：  
　　　1) idを指定し、一定間隔でコメント情報を取得する  
　　　2) 取得したコメントをプログラム上のウインドウで、右から左に流れるように表示する(ボード機能)  
　　　3) 取得したコメントをプログラム上のウインドウに表示する(ビューワー機能)、  
   　　　同ウインドウにて、コメントを送信する

---

## ファイル構成
　MameCome.jar               ：　プログラム本体    
　MameCome.ini               ：　プログラム用定義ファイル  
　MameCommentStart.command   ：　MAC版起動用コマンド  
　MameCommentStart.bat       ：　Windows版起動用コマンド(作成予定)  
　MameCommentStart.ini       ：　起動用定義ファイル  

---

## 導入、設定方法
- Java11の導入  
- OpenJFXの導入  
- (MACのみ)起動用コマンドの権限設定  
- 起動用定義ファイルの修正

---

## 使用方法
- 設定画面での各種設定

---

## 更新履歴
- ver0.1	2018/05/03  
　　公開するつもりだったがやめた版  
- ver0.2	2018/05/05  
　　movieIdではなく、ユーザー名で取得、ライブ配信していない場合は処理を行わないように修正  
　　メッセージ表示部の透過ウインドウ対応  
　　上記に伴い、ウインドウサイズの変更不可化  
　　上記に伴い、アプリケーションの終了ボタン実装  
　　上記に伴い、ボタン以外の部分のドラッグでのウインドウ移動機能実装  
　　常に最前面に表示する機能追加  
- ver0.3	2018/05/06  
　　ウインドウサイズの外部パラメータ化  
- ver0.4  
　　欠番
- ver0.5  
　　C#への移植版、非公開  
- ver0.6   2019/xx/xx  
　　メイン画面、設定画面、ビューワーの実装による大幅改定  
　　コンパイル環境のJava8→Java11への変更

