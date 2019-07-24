//Windows,Macソース共有、ファイル名だけ変える
//Windows:BoardWindow.cs,Mac:BoardWindowController
//差分についてはCompile時のフラグで切り替えてもいいが、Code中は頭でDEFINEしてしまう
#define MACOS
//#define WINDOWS
using System;
#if MACOS
using Foundation;
using AppKit;
#elif WINDOWS
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;
using System.Drawing;
using System.Windows.Forms.Integration;
#endif

namespace MameComment
{
#if MACOS
    public partial class BoardWindowController : NSWindowController
#elif WINDOWS
    public class BoardWindow : Form
#endif
    {
        private MCSetting Mcs;
#if WINDOWS
        private int initialX;
        private int initialY;
        private const int winWindowHeight = 43;
        private const int winWindowWidth = 14;
        BoardView boardView;
#endif

#if MACOS
        //MAC版のイニシャライザでは何もしない
        public BoardWindowController(IntPtr handle) : base(handle)
        {
        }
#elif WINDOWS
        public BoardWindow(MCSetting Mcs)
        {
           this.Mcs = Mcs;
            //Windows-WPF版の場合、Viewとの紐付けをイニシャライザで行う必要がある
            ElementHost elementHost = new ElementHost();
            boardView = new BoardView();
            boardView.ParentWindow = this;
            boardView.SetValue(Mcs);
            this.SuspendLayout();

            elementHost.Dock = DockStyle.Fill;
            elementHost.TabIndex = 0;
            elementHost.Child = (UIElement)boardView;

            this.Bounds = new Rectangle(0,0,494,308);
            System.Windows.Media.Color bgColor = ColorList.GetColor(Mcs.BoardBgColor);
            this.BackColor = Color.FromArgb(bgColor.A,bgColor.R,bgColor.G,bgColor.B);
           //TransparencyKeyを変更すると、透過が可能になる
            //            this.TransparencyKey = ColorList.GetColor(Mcs.BoardBgColor);
            this.Controls.Add(elementHost);
            this.ResumeLayout(false);

            this.SuspendLayout();
            // 
            // BoardWindow
            // 
            this.ClientSize = new System.Drawing.Size(284, 264);
            this.Name = "BoardWindow";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.WillClose);
            this.Shown += new System.EventHandler(this.ShownEvent);
            this.ResizeEnd += new System.EventHandler(this.DidEndLiveResize);
            this.Move += new System.EventHandler(this.DidMove);
            this.ResumeLayout(false);

            //Window表示時に勝手に座標を変更する仕様のため、初期値を保管して後で復元させる
            initialX = (int)Mcs.BoardX;
            initialY = (int)Mcs.BoardY;
        }
#endif

#if MACOS
        //Mac版はイニシャライザで実施しなかった値の保管と、
        //ウインドウ位置の変更を先行して実施しておく
        public void SetValue(MCSetting Mcs)
        {
            this.Mcs = Mcs;
            ChangeWindowSize();
        }
#endif

#if MACOS
        //MAC版ではLoad時にイベントのセットなどの紐付けが必要
        //Window自体の透過は画面の表示のタイミングでしなければならない
        public override void WindowDidLoad()
        {
            //透過の設定
            base.WindowDidLoad();
            base.Window.IsOpaque = false;
            base.Window.HasShadow = false;
            base.Window.BackgroundColor = NSColor.FromRgba(1, 1, 1, 0);
            //Bvc = (BoardViewController)this.ContentViewController;

            //サイズ変更イベント検知
            Window.DidEndLiveResize += (object sender, EventArgs e) =>
            {
                LogWriter.DebugLog("ウインドウサイズの変更を検知しました。");
                //
                Mcs.BoardWidth = base.Window.Frame.Width;
                Mcs.BoardHeight = base.Window.Frame.Height;
                ((BoardViewController)base.ContentViewController).ResetValue();

            };

            //位置変更イベント検知
            Window.DidMove += (object sender, EventArgs e) =>
            {
                LogWriter.DebugLog("ウインドウ位置の変更を検知しました。");
                Mcs.BoardX = base.Window.Frame.X;
                Mcs.BoardY = base.Window.Frame.Y;
            };

            //閉じるイベント検知
            Window.WillClose += (object sender, EventArgs e) =>
            {
                LogWriter.DebugLog("ウインドウを閉じます。");
                Mcs.BoardOpenFlg = 0;
            };
        }
#endif

#if WINDOWS
        //Windowsの場合は、イベントは画面デザインから紐付けできるため、
        //Controllerの挙動の紐付けのような感じで普通に関数定義を行う。

        //BoardWindowController.viewDidLoad内のサイズ変更イベント
        private void DidEndLiveResize(object sender, System.EventArgs e)
        {
            LogWriter.DebugLog("ウインドウサイズの変更を検知しました。");
            Mcs.BoardWidth = this.Width;
            Mcs.BoardHeight = this.Height;
            if (boardView.Mclabels != null)
            {
                boardView.ResetValue();
            }
        }

        //BoardWindowController.viewDidLoad内の位置変更イベント
        private void DidMove(object sender, System.EventArgs e)
        {
            LogWriter.DebugLog("ウインドウ位置の変更を検知しました。");
            Mcs.BoardX = this.Left;
            Mcs.BoardY = this.Top;
        }

        //BoardWindowController.viewDidLoad内の閉じるイベント
        private void WillClose(object sender, FormClosingEventArgs e)
        {
            LogWriter.DebugLog("ウインドウを閉じます。");
            Mcs.BoardOpenFlg = 0;
        }

#endif

        public void ChangeWindowSize()
        {
#if MACOS
            base.Window.SetFrame(new CoreGraphics.CGRect(Mcs.BoardX, Mcs.BoardY, Mcs.BoardWidth, Mcs.BoardHeight), false);
#elif WINDOWS
            this.Bounds = new Rectangle((int)Mcs.BoardX, (int)Mcs.BoardY, (int)Mcs.BoardWidth, (int)Mcs.BoardHeight);
#endif
        }

#if WINDOWS
        //Windows版は画面表示後にウインドウサイズの変更
        //というより、勝手に移動されるウインドウ位置の補正を行う必要がある
        private void ShownEvent(object sender, EventArgs e)
        {
            //表示した瞬間にWindows側で勝手に位置を変更し、位置変更イベントが発生してしまう。
            //Mcsの座標は変更された位置に更新されてしまうので、
            //初期値で再度上書きしてウインドウ位置の変更を行う
            Mcs.BoardX = initialX;
            Mcs.BoardY = initialY;
            ChangeWindowSize();
            //ViewController側の表示処理を追加
            boardView.ViewDidAppear();
        }
#endif
    }
}
