//Windows,Macソース共有、ファイル名だけ変える
//Windows:MainWindow.xaml.cs,Mac:MainViewController
//差分についてはCompile時のフラグで切り替えてもいいが、Code中は頭でDEFINEしてしまう
#define MACOS
//#define WINDOWS

using System;
using System.Threading;
using System.Threading.Tasks;
#if MACOS
using AppKit;
using Foundation;
#elif WINDOWS
using System.Windows;
using System.Windows.Forms.Integration;
#endif

namespace MameComment
{
#if MACOS
    public partial class MainViewController : NSViewController
#elif WINDOWS
    public partial class MainWindow : Window
#endif
    {
        public MCSetting Mcs = new MCSetting();
#if MACOS
        public NSStoryboard SettingStoryBoard, BoardStoryBoard, ViewerStoryBoard;
        public NSWindowController SettingController, BoardController, ViewerController;
#elif WINDOWS
        public ViewerWindow viewerWindow;
        public SettingWindow settingWindow;
        public BoardWindow boardWindow;
#endif
        public Task GetTask = null;
        public CancellationTokenSource CancelTokenSource;
        public CancellationToken CancelToken;
        public LogWriter Writer;
        public int EndFlg = 0;
        public String Id;
        public String MovieId;

#if MACOS
        public MainViewController(IntPtr handle) : base(handle)
        {
        }
#elif WINDOWS 
        public MainWindow()
        {
            InitializeComponent();
        }
#endif

#if MACOS
        public override void ViewDidLoad()
#elif WINDOWS
        private void ViewDidLoad(object sender, RoutedEventArgs e)
#endif
        {
#if MACOS
            base.ViewDidLoad();
#endif
            LogWriter.DebugLog("デバッグモードで起動されました");
#if MACOS
            //Macの場合、ユーザーのApplication Supportに設定ファイルを配置する。
            //その準備として、ディレクトリがない場合に作成する
            if (!System.IO.Directory.Exists(Environment.GetFolderPath(Environment.SpecialFolder.Personal)+"/Library/Application Support/MameComment"))
            {
                System.IO.Directory.CreateDirectory(Environment.GetFolderPath(Environment.SpecialFolder.Personal) + "/Library/Application Support/MameComment");
            }
#endif
            //設定をファイルから読み込む
            if (!Mcs.ReadFromIni())
            {
                //失敗した場合はデフォルト値でファイルを作成する
                LogWriter.DebugLog("設定ファイルの読み込みに失敗しました、デフォルト値で起動します。");
                if (!Mcs.WriteToIni())
                {
                    //更に失敗した場合は、保存できない旨の通知をする
                    LogWriter.DebugLog("設定ファイルの保存に失敗しました。今後の設定変更は次回に反映されない可能性があります。");
                    //TODO 通知ダイアログをOpenする？
                }
            }
            LogWriter.DebugLog("設定ファイルから読み込んだ情報を以下に表示します。\n" + Mcs.GetValues());

#if MACOS
            //各種StoryBoardとWindowControllerを作成しておく
            SettingStoryBoard = NSStoryboard.FromName("Setting", null);
            SettingController = (NSWindowController)SettingStoryBoard.InstantiateControllerWithIdentifier("SettingWindow");
            BoardStoryBoard = NSStoryboard.FromName("Board", null);
            BoardController = (NSWindowController)BoardStoryBoard.InstantiateControllerWithIdentifier("BoardWindow");
            ViewerStoryBoard = NSStoryboard.FromName("Viewer", null);
            ViewerController = (NSWindowController)ViewerStoryBoard.InstantiateControllerWithIdentifier("ViewerWindow");
#elif WINDOWS
            //Windowsの場合は、毎回Newするので該当処理はSkip
#endif
            //ログ出力がONの場合、ログ出力を初期化
            Writer = new LogWriter();
            Writer.Init(Mcs);
            LogWriter.DebugLog("ログ出力が初期化されました。");
        }

#if MACOS
        //以下はMacでのみの独自ロジック
        public override NSObject RepresentedObject
        {
            get
            {
                return base.RepresentedObject;
            }
            set
            {
                base.RepresentedObject = value;
                // Update the view, if already loaded.
            }
        }
#endif

        //コメント取得のボタンが押された時の動作
#if MACOS
        partial void CtrlGet(Foundation.NSObject sender)
#elif WINDOWS
        private void CtrlGet(object sender, RoutedEventArgs e)
#endif
        {
#if MACOS
            if (IdText.StringValue.Equals(""))
#elif WINDOWS
            if (IdText.Text.Equals(""))
#endif
            {
                LogWriter.DebugLog("IDが空のため、取得を中止します。");
                return;
            }
            if (GetTask != null && GetTask.Status == TaskStatus.Running)
            {
                CancelTokenSource.Cancel();
                if (Mcs.LogFlg.Equals("On"))
                {
                    LogWriter.DebugLog("終了のログを記録します。");
                    Writer.Write(DateTime.Now.ToString("yyyy/MM/dd hh:mm:ss") + " ========== " + Id + "のコメント取得ここまで ==========");
                }
            }
            else
            {
                //スレッドキャンセル用のトークンを取得
                CancelTokenSource = new CancellationTokenSource();
                CancelToken = CancelTokenSource.Token;
#if MACOS
                LogWriter.DebugLog("別スレッドにてコメント取得を開始します。ユーザーID：" + IdText.StringValue + ",キー：" + Mcs.GetAccKey());
                //Runにオブジェクトの参照を渡すと正常に読み込めない？ので、一旦Stringに格納して渡す
                Id = IdText.StringValue;

#elif WINDOWS
                LogWriter.DebugLog("別スレッドにてコメント取得を開始します。ユーザーID：" + IdText.Text + ",キー：" + Mcs.GetAccKey());
                //Runにオブジェクトの参照を渡すと正常に読み込めない？ので、一旦Stringに格納して渡す
                Id = IdText.Text;
#endif
                Writer.Write(DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss") + " ========== " + Id + "のコメント取得ここから ==========");
                GetComment CommentThread = new GetComment(this);
                //別スレッドで実行
                GetTask = Task.Run(() =>
                {
                    CommentThread.GetMessage(CancelToken, Id);
                }, CancelToken);
                LogWriter.DebugLog("コメント取得スレッドの起動リクエストが終了しました。");
            }
        }

        //GetCommentスレッドから、MovieIdをUpdateする(主にPost用)
        public void UpdateMovieId(String MovieId)
        {
            this.MovieId = MovieId;
        }


#if MACOS
        partial void CtrlBoard(Foundation.NSObject sender)
#elif WINDOWS
        private void CtrlBoard(object sender, RoutedEventArgs e)
#endif
        {
            LogWriter.DebugLog("ボード操作を実行します。");
            //もし開いていた場合は閉じる
            if (Mcs.BoardOpenFlg == 1)
            {
                LogWriter.DebugLog("ボードがOpenされていたので一旦Closeします。");
#if MACOS
                BoardController.Close();
            }
            //WindowContrllerにMcsを渡す
            ((BoardWindowController)BoardController).SetValue(Mcs);
            //MainViewControllerにMcsを渡す
            ((BoardViewController)BoardController.ContentViewController).SetValue(Mcs);

#elif WINDOWS
                boardWindow.Close();
            }
            //Windowsの場合、Closeするとオブジェクトが破棄されてしまうので作り直し
            //WindowContrllerにMcsを渡すのはコンストラクタで行ってしまう。
            boardWindow = new BoardWindow(Mcs);
#endif
            //ボードはウインドウで開けばOK
            LogWriter.DebugLog("ボードをOpenします。");
#if MACOS
            BoardController.ShowWindow(this);
#elif WINDOWS
            boardWindow.Show();
#endif
            Mcs.BoardOpenFlg = 1;
        }



#if MACOS
        partial void CtrlSetting(Foundation.NSObject sender)
#elif WINDOWS
        private void CtrlSetting(object sender, RoutedEventArgs e)
#endif
        {
            LogWriter.DebugLog("設定ウインドウをOpenします。");
#if MACOS
            //Mcsをオブジェクトとして渡しておく
            ((SettingViewController)SettingController.ContentViewController).Mcs = this.Mcs;
            //設定画面をModal表示(元のウインドウに切り替えさせないため)
            this.PresentViewControllerAsSheet(SettingController.ContentViewController);
#elif WINDOWS
            settingWindow = new SettingWindow();
            //Mcsをオブジェクトとして渡しておく
            settingWindow.Mcs = Mcs;
            //親の関数を呼び出せるように、オーナーを設定しておく
            settingWindow.Owner = this;
            //設定画面をModal表示(元のウインドウに切り替えさせないため)
            settingWindow.ShowDialog();
#endif
            //Openは別スレッドなので、閉じたときの処理はここには書けないため、別関数で定義
        }

#if MACOS
        partial void CtrlViewer(Foundation.NSObject sender)
#elif WINDOWS
        private void CtrlViewer(object sender, RoutedEventArgs e)
#endif
        {
            LogWriter.DebugLog("ビューワー操作を実行します。");
            //もし開いていた場合は閉じる
            if (Mcs.ViewerOpenFlg == 1)
            {

                Console.WriteLine(Mcs.ViewerHeight);
                LogWriter.DebugLog("ビューワーがOpenされていたので一旦Closeします。");
#if MACOS
                ViewerController.Close();
            }
            //WindowContrllerにMcsを渡す
            ((ViewerWindowController)ViewerController).SetValue(Mcs);
            //MainViewControllerにMcsとMainViewControllerを渡す
            ((ViewerViewController)ViewerController.ContentViewController).SetValue(Mcs,this);
#elif WINDOWS
                viewerWindow.Close();
            }
            //Windowsの場合、Closeするとオブジェクトが破棄されてしまうので作り直し
            //WindowContrllerにMcsを渡すのはコンストラクタで行ってしまう。
            viewerWindow = new ViewerWindow(Mcs,this);
            Console.WriteLine(Mcs.ViewerHeight);
#endif
            //ボードはウインドウで開けばOK
            LogWriter.DebugLog("ビューワーをOpenします。");
#if MACOS
            ViewerController.ShowWindow(this);
#elif WINDOWS
            viewerWindow.Show();
#endif
            Mcs.ViewerOpenFlg = 1;
        }

        public void SendMessage(CommentInformation CInfo)
        {
            LogWriter.DebugLog("メッセージを表示します。");
            if (Mcs.BoardOpenFlg == 1)
            {
                LogWriter.DebugLog("ボードにメッセージを表示します。メッセージ：" + CInfo.Message);
#if MACOS
                ((BoardViewController)this.BoardController.ContentViewController).DisplayBoardMessage(CInfo.Message);
#elif WINDOWS
                ((BoardView)((ElementHost)boardWindow.Controls[0]).Child).DisplayBoardMessage(CInfo.Message);
#endif
            }
            if (Mcs.ViewerOpenFlg == 1)
            {
                LogWriter.DebugLog("ビューワーにメッセージを表示します");
#if MACOS
                ((ViewerViewController)this.ViewerController.ContentViewController).AddComment(CInfo);
#elif WINDOWS
                ((ViewerView)((ElementHost)viewerWindow.Controls[0]).Child).AddComment(CInfo);
#endif
            }
            if (Mcs.LogFlg.Equals("On"))
            {
                LogWriter.DebugLog("ログにメッセージを表示します");
                Writer.Write(CInfo.Timestamp + " " + CInfo.UserId + " " + CInfo.UserName + " : " + CInfo.Message);
            }
        }

        public void PostMessage(String Comment)
        {
            if (!MovieId.Equals(""))
            {
                LogWriter.DebugLog("メッセージをTwicasにPOSTします。");
                TwitcastingHTTPApi.PostComment(Comment, MovieId, Mcs.GetAccKey());
            }
            else
            {
                LogWriter.DebugLog("MovieIdが未取得のためメッセージ送信をキャンセルします。");
            }
        }

        public void CloseSettingWindow()
        {
            //設定ウインドウが閉じたときの処理が必要であれば実装
            LogWriter.DebugLog("設定ウインドウがCloseされました。");
            //ログ出力は再設定が必要
            LogWriter.DebugLog("ログ出力を再設定します。");
            Writer.Init(Mcs);
        }

#if MACOS
        public override void ViewWillDisappear()
#elif WINDOWS
        private void ViewWillDisappear(object sender, System.ComponentModel.CancelEventArgs e)
#endif
        {
            LogWriter.DebugLog("プログラム終了がリクエストされました。");
#if MACOS
            base.ViewWillDisappear();
#endif
            //現在の設定を保存する
            LogWriter.DebugLog("現在の設定値をファイルに保存します。");
            Mcs.WriteToIni();
            EndFlg = 1;

            //裏で動いているスレッドを止める
            if (GetTask != null && GetTask.Status == TaskStatus.Running)
            {
                LogWriter.DebugLog("コメント取得スレッドに停止要求を発行します。");
                CancelTokenSource.Cancel();
            }

            //強制的に0で終了する
            LogWriter.DebugLog("ログ出力を停止し、プログラムを正常終了します。");
            Writer.Close();
            Environment.Exit(0);
        }

        public void ChangeGetBtnView(Boolean ThreadRunning)
        {
            if (ThreadRunning == false)
            {
                LogWriter.DebugLog("ボタンを取得開始に変更します。");
#if MACOS
                CtrlGetBtn.Title = "取得開始";
#elif WINDOWS
                CtlGetBtn.Content = "取得開始";
#endif
            }
            else
            {
                LogWriter.DebugLog("ボタンを取得停止に変更します。");
#if MACOS
                CtrlGetBtn.Title = "取得停止";
#elif WINDOWS
                CtlGetBtn.Content = "取得停止";
#endif
            }

        }
    }
}
