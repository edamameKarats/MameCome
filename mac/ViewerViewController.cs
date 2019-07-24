//Windows,Macソース共有、ファイル名だけ変える
//Windows:ViewerView.xaml.cs,Mac:ViewerViewController
//差分についてはCompile時のフラグで切り替えてもいいが、Code中は頭でDEFINEしてしまう
#define MACOS
//#define WINDOWS

using System;
#if MACOS
using AppKit;
using CoreGraphics;
#elif WINDOWS
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Threading;
using System.Collections.ObjectModel;
#endif

namespace MameComment
{
#if MACOS
    public partial class ViewerViewController : NSViewController
#elif WINDOWS
    public partial class ViewerView : UserControl
#endif
    {
        private MCSetting Mcs;
#if MACOS
        private CommentTableDataSource DataSource;
        public MainViewController Vc;
#elif WINDOWS
        private const int winWindowWidth = 14;
        private const int winWindowHeight = 37;
        public ViewerWindow ParentWindow;
        private ObservableCollection<CommentInformation> CommentCollection;
        private int rowCount;
        public MainWindow Vc;
#endif

#if MACOS
        public ViewerViewController(IntPtr handle) : base(handle)
        {
        }
        //Mac版ではコンストラクタでMcsを読み込まないので、読み込み用のロジックを用意する
        public void SetValue(MCSetting Mcs, MainViewController Vc)
        {
            this.Mcs = Mcs;
            this.Vc = Vc;
        }
#elif WINDOWS
        public ViewerView(MCSetting Mcs, MainWindow Vc)
        {
            InitializeComponent();
            CommentCollection = new ObservableCollection<CommentInformation>();
            this.CommentTableView.ItemsSource = CommentCollection;
            this.Mcs = Mcs;
            this.Vc = Vc;
        }
#endif

#if MACOS
        public override void ViewDidLoad()
        {
            base.ViewDidLoad();
            base.View.WantsLayer = true;

            // Create the Product Table Data Source and populate it
            DataSource = new CommentTableDataSource();


            //テーブルへのデータソースの紐づけと生成
            CommentTable.DataSource = DataSource;


        }
#endif

        //Windows版は表示後の修正なので厳密にはWillAppearとDidAppearで挙動が異なるが、一瞬なのでOKとする。
#if MACOS
        public override void ViewWillAppear()
#elif WINDOWS
        public void ViewDidAppear()
#endif
        {
            LogWriter.DebugLog("ビューワーを表示します。");
#if MACOS
            base.ViewWillAppear();
            //一旦データをClearしておく
            DataSource.Comments.Clear();
            CommentTable.BackgroundColor = NSColor.FromCGColor(ColorList.GetColor(Mcs.ViewerBgColor));//AlternatingRowsをFalseにしないと変わらない
            CommentTable.FontColor = NSColor.FromCGColor(ColorList.GetColor(Mcs.ViewerFontColor));
            SendCommentArea.BackgroundColor = NSColor.FromCGColor(ColorList.GetColor(Mcs.ViewerBgColor));
            SendCommentArea.TextColor = NSColor.FromCGColor(ColorList.GetColor(Mcs.ViewerFontColor));
#elif WINDOWS
            this.CommentTableView.Background = new SolidColorBrush(ColorList.GetColor(Mcs.ViewerBgColor));
            this.CommentTableView.RowBackground = new SolidColorBrush(ColorList.GetColor(Mcs.ViewerBgColor));
            this.CommentTableView.Foreground = new SolidColorBrush(ColorList.GetColor(Mcs.ViewerFontColor));
            this.SendCommentArea.Background = new SolidColorBrush(ColorList.GetColor(Mcs.ViewerBgColor));
            this.SendCommentArea.Foreground = new SolidColorBrush(ColorList.GetColor(Mcs.ViewerFontColor));
            //            BrushConverter Bconv = new BrushConverter();
            //            this.CommentTableView.Background = (Brush)Bconv.ConvertFromString(Mcs.ViewerBgColor);
            //            this.CommentTableView.RowBackground = (Brush)Bconv.ConvertFromString(Mcs.ViewerBgColor);
            //            this.CommentTableView.Foreground = (Brush)Bconv.ConvertFromString(Mcs.ViewerFontColor);
            //            this.SendCommentArea.Background = (Brush)Bconv.ConvertFromString(Mcs.ViewerBgColor);
            //            this.SendCommentArea.Foreground = (Brush)Bconv.ConvertFromString(Mcs.ViewerFontColor);
#endif
            ResetValue();
#if MACOS
            CommentTable.Delegate = new CommentTableDelegate(DataSource);
            CommentTable.ReloadData();
#endif
        }

        //とにかく外からはコメントの情報をまとめて投げてくれればこっちで表示を何とかする
        public void AddComment(CommentInformation CInfo)
        {
            //現在位置が一番最後かどうか判定する
            int ScrollFlg = 0;
#if WINDOWS
            int initFlg = 0;
            //行数が0だった場合(初期状態)は行数をUpdateする
            if (rowCount == 0)
            {
                //コメントがない場合は追加する
                if (CommentCollection.Count == 0)
                {
                    initFlg = 1;
                    CommentInformation InitialInfo = new CommentInformation();
                    InitialInfo.Timestamp = "XX:XX:XX";
                    CommentCollection.Add(InitialInfo);
                }
                //実は同期的に変更していないみたいなので、ラベルの値の変更を待ってから後続作業を実施する
                Dispatcher.Invoke(new Action(() =>
                {
                }), DispatcherPriority.Loaded);
                //高さは行の高さ+1(罫線)
                int rowHeight = (int)Math.Ceiling(((DataGridRow)CommentTableView.ItemContainerGenerator.ContainerFromIndex(0)).ActualHeight + 1);
                //現在のTableViewの中で、何行まで表示できるか計算する、あとで最終行かどうかの判定でも使うのでprivateの変数に入れておく
                rowCount = (int)Math.Floor((double)CommentTableView.Height / rowHeight);
            }
            LogWriter.DebugLog(CommentCollection.Count + "," + rowCount + "," + ((ScrollViewer)((Decorator)VisualTreeHelper.GetChild(CommentTableView, 0)).Child).VerticalOffset);
#endif
#if MACOS
            if (CommentTable.VisibleRect().Bottom == CommentTable.Frame.Height)
#elif WINDOWS
            if (CommentCollection.Count - rowCount <= ((ScrollViewer)((Decorator)VisualTreeHelper.GetChild(CommentTableView, 0)).Child).VerticalOffset)
#endif
            {
                LogWriter.DebugLog("ScrollFlg set to ON");
                ScrollFlg = 1;
            }
#if WINDOWS
            //コメントがなかった場合は追加されたダミーを削除する
            if (initFlg == 1)
            {
                CommentCollection.RemoveAt(0);
            }
#endif

            //データソースに書き込み
            LogWriter.DebugLog("コメントを追加します。内容:" + CInfo.Timestamp + "," + CInfo.UserImage + "," + CInfo.UserName + "," + CInfo.Message);
#if MACOS
            DataSource.Comments.Add(new Comment(CInfo.Timestamp, CInfo.UserImage, CInfo.UserName, CInfo.Message));
            LogWriter.DebugLog("コメント行数 " + DataSource.Comments.Count);
#elif WINDOWS
            CommentCollection.Add(CInfo);
#endif

#if MACOS
            //追加されたメッセージをもとに、再描画
            //本当はこうしたくないんだけど・・・
            CommentTable.ReloadData();
#endif

            //一番最後なら一番下のコメントまでスクロールする
            if (ScrollFlg == 1)
            {
#if MACOS
                CommentTable.ScrollPoint(new CGPoint(0, CommentTable.RowHeight * (DataSource.Comments.Count - 2)));
#elif WINDOWS
                ((ScrollViewer)((Decorator)VisualTreeHelper.GetChild(CommentTableView, 0)).Child).ScrollToBottom();
#endif
            }
        }


        //コメントの送信
#if MACOS
        partial void SendComment(Foundation.NSObject sender)
#elif WINDOWS
        private void SendComment(object sender, RoutedEventArgs e)
#endif
        {
            String Comment;
#if MACOS
            Comment = SendCommentArea.StringValue;
#elif WINDOWS
            Comment = SendCommentArea.Text;
#endif
            if (Comment.Equals(""))
            {
                LogWriter.DebugLog("コメントが空のため、送信処理を行いません。");
            }
            else
            {
                LogWriter.DebugLog("コメントを送信します。メッセージ:" + Comment);
                Vc.PostMessage(Comment);

#if MACOS
                SendCommentArea.StringValue = "";
#elif WINDOWS
                SendCommentArea.Text = "";
#endif
            }
        }


        public void ResetValue()
        {
            LogWriter.DebugLog("画面サイズを変更します。");

            //MACの場合固定で上に表示されるズレ
            float BufferSize = 22.0f;

            //フォントの変更
#if MACOS
            SendCommentArea.Font = NSFont.FromFontName(Mcs.ViewerFont, (System.nfloat)Mcs.ViewerFontSize);
#elif WINDOWS
            SendCommentArea.FontFamily = new FontFamily(Mcs.ViewerFont);
            SendCommentArea.FontSize = Mcs.ViewerFontSize;
#endif
            //フォントサイズをもとに、入力エリアを3行分確保する
#if MACOS
            NSTextField TmpField = new NSTextField();
            TmpField.StringValue = "TestString";
            TmpField.Font = SendCommentArea.Font;
            TmpField.SizeToFit();
            LogWriter.DebugLog("InputFont Height is " + TmpField.Frame.Height + ", FontName=" + TmpField.Font.FontName);
            float InputHeight = (float)((TmpField.Frame.Height - 4) * 3);//SystemRegular 13pt で55

#elif WINDOWS
            Label TmpField = new Label();
            TmpField.Content = "XX:XX:XX";
            TmpField.Foreground = new SolidColorBrush(Colors.Transparent);
            TmpField.FontFamily = SendCommentArea.FontFamily;
            TmpField.FontSize = SendCommentArea.FontSize;
            TmpField.Height = Double.NaN;
            ((Canvas)this.Content).Children.Add(TmpField);
            //実は同期的に変更していないみたいなので、ラベルの値の変更を待ってから後続作業を実施する
            Dispatcher.Invoke(new Action(() =>
            {
                TmpField.Height = TmpField.ActualHeight;
                TmpField.Width = TmpField.ActualWidth;
            }), DispatcherPriority.Loaded);
            LogWriter.DebugLog("InputFont Height is " + TmpField.Height + " , FontName=" + TmpField.FontFamily.ToString());
            double TimeColumnWidth = TmpField.Width;
            float InputHeight = (float)((TmpField.Height - 4) * 3);//SystemRegular 13pt で55
            ((Canvas)this.Content).Children.Remove(TmpField);
#endif

            //画面サイズをもとに、ボタンのサイズを決める
            float ButtonWidth;
            if (Mcs.ViewerWidth < 300)
            {
#if MACOS
                ButtonWidth = (float)((Mcs.ViewerWidth) / 3);
#elif WINDOWS
                ButtonWidth = (float)((Mcs.ViewerWidth - winWindowWidth) / 3);
#endif
            }
            else
            {
                ButtonWidth = 100.0f;//105*55
            }
            //画面端からの位置は3ptずらす
            float InsertWidth = 3.0f;

#if MACOS
            SendCommentArea.SetFrameSize(new CGSize(Mcs.ViewerWidth - ButtonWidth - InsertWidth * 3, InputHeight));
            SendCommentArea.SetFrameOrigin(new CGPoint(InsertWidth, InsertWidth));
            SendCommentBtn.SetFrameSize(new CGSize(ButtonWidth, InputHeight));
            SendCommentBtn.SetFrameOrigin(new CGPoint(Mcs.ViewerWidth - ButtonWidth - InsertWidth, InsertWidth));
            Comment.SetFrameSize(new CGSize(Mcs.ViewerWidth - InsertWidth * 2, Mcs.ViewerHeight - InputHeight - InsertWidth * 3 - BufferSize));
            Comment.SetFrameOrigin(new CGPoint(InsertWidth, InputHeight + InsertWidth * 2));
#elif WINDOWS
            double SendCommentAreaX = InsertWidth;
            double SendCommentAreaHeight = InputHeight;
            double SendCommentBtnWidth = ButtonWidth;
            double SendCommentBtnHeight = InputHeight;
            double CommentTableViewX = InsertWidth;
            double CommentTableViewY = InsertWidth;
            double SendCommentAreaWidth = Mcs.ViewerWidth - ButtonWidth - InsertWidth * 3 - winWindowWidth;
            double CommentTableViewWidth = Mcs.ViewerWidth - InsertWidth * 2 - winWindowWidth;
            double CommentTableViewHeight = Mcs.ViewerHeight - InsertWidth * 3 - InputHeight - winWindowHeight;
            double SendCommentAreaY = InsertWidth * 2 + CommentTableViewHeight;
            double SendCommentBtnY = SendCommentAreaY;
            double SendCommentBtnX = SendCommentAreaWidth + InsertWidth * 2;

            SendCommentArea.Margin = new Thickness(SendCommentAreaX, SendCommentAreaY, 0, 0);
            SendCommentArea.Width = SendCommentAreaWidth;
            SendCommentArea.Height = SendCommentAreaHeight;
            SendCommentBtn.Margin = new Thickness(SendCommentBtnX, SendCommentBtnY, 0, 0);
            SendCommentBtn.Width = SendCommentBtnWidth;
            SendCommentBtn.Height = SendCommentBtnHeight;
            CommentTableView.Margin = new Thickness(CommentTableViewX, CommentTableViewY, 0, 0);
            CommentTableView.Width = CommentTableViewWidth;
            CommentTableView.Height = CommentTableViewHeight;
#endif

#if MACOS
            CommentTable.TableFont = NSFont.FromFontName(Mcs.ViewerFont, (System.nfloat)Mcs.ViewerFontSize);
            TmpField = new NSTextField();
            TmpField.StringValue = "TestString";
            TmpField.Font = CommentTable.TableFont;
            TmpField.SizeToFit();
            CommentTable.RowHeight = TmpField.Frame.Height - 4;
#elif WINDOWS
            CommentTableView.FontFamily = SendCommentArea.FontFamily;
            CommentTableView.FontSize = SendCommentArea.FontSize;
            CommentTableView.RowHeight = Double.NaN;
#endif

            //もし今の幅が必須の時刻幅より狭ければ調整する
#if MACOS
#elif WINDOWS
            if (CommentTableView.Columns[0].ActualWidth < TimeColumnWidth)
            {
                CommentTableView.Columns[0].Width = TimeColumnWidth;
            }
            if (CommentTableView.Columns[1].ActualWidth < TimeColumnWidth)
            {
                CommentTableView.Columns[1].Width = TimeColumnWidth;
            }
            //実は同期的に変更していないみたいなので、ラベルの値の変更を待ってから後続作業を実施する
            Dispatcher.Invoke(new Action(() =>
            {
            }), DispatcherPriority.Loaded);
            CommentTableView.Columns[2].Width = CommentTableView.ActualWidth - CommentTableView.Columns[0].ActualWidth - CommentTableView.Columns[1].ActualWidth - 25;
#endif
        }


#if WINDOWS
        //Windowsの場合、イベントドリブンでヘッダーとかを調整する
        private void SetColumns(object sender, DataGridAutoGeneratingColumnEventArgs e)
        {
            switch (e.PropertyName)
            {
                case "Timestamp":
                    e.Column.Header = "時刻";
                    e.Column.DisplayIndex = 0;
                    e.Column.IsReadOnly = true;
                    break;
                case "UserName":
                    e.Column.Header = "ユーザー";
                    e.Column.DisplayIndex = 1;
                    e.Column.IsReadOnly = true;
                    break;
                case "Message":
                    e.Column.Header = "コメント";
                    e.Column.DisplayIndex = 2;
                    e.Column.IsReadOnly = true;
                    break;
                default:
                    e.Cancel = true;
                    break;
            }
        }
#endif


    }
}
