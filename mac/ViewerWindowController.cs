//Windows,Macソース共有、ファイル名だけ変える
//Windows:ViewerWindow.cs,Mac:ViewerWindowController
//差分についてはCompile時のフラグで切り替えてもいいが、Code中は頭でDEFINEしてしまう
#define MACOS
//#define WINDOWS

using System;
#if MACOS
using AppKit;
#elif WINDOWS
using System.Windows;
using System.Windows.Forms;
using System.Drawing;
using System.Windows.Forms.Integration;
#endif

namespace MameComment
{
#if MACOS
    public partial class ViewerWindowController : NSWindowController
#elif WINDOWS
    public partial class ViewerWindow : Form
#endif
    {
        private MCSetting Mcs;
#if WINDOWS
        private int initialX;
        private int initialY;
        private const int winWindowHeight = 43;
        private const int winWindowWidth = 14;
        ViewerView viewerView;
#endif

#if MACOS
        public ViewerWindowController(IntPtr handle) : base(handle)
        {
        }
        //Mac版ではコンストラクタでMcsを読み込まないので、読み込み用のロジックを用意する
        public void SetValue(MCSetting Mcs)
        {
            this.Mcs = Mcs;
            ChangeWindowSize();
        }
#elif WINDOWS
        public ViewerWindow(MCSetting Mcs,MainWindow Vc)
        {
            this.Mcs = Mcs;
            //Windows-WPF版の場合、Viewとの紐付けをイニシャライザで行う必要がある
            ElementHost elementHost = new ElementHost();
            viewerView = new ViewerView(Mcs,Vc);
            viewerView.ParentWindow = this;
            this.SuspendLayout();

            elementHost.Dock = DockStyle.Fill;
            elementHost.TabIndex = 0;
            elementHost.Child = (UIElement)viewerView;

            this.Bounds = new Rectangle(0, 0, 652, 380);
            this.Controls.Add(elementHost);
            this.ResumeLayout(false);

            this.ClientSize = new System.Drawing.Size(652, 380);
            this.Text = "ViewerWindow";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.WillClose);
            this.Shown += new System.EventHandler(this.ShownEvent);
            this.ResizeEnd += new System.EventHandler(this.DidEndLiveResize);
            this.Move += new System.EventHandler(this.DidMove);
            this.ResumeLayout(false);

            initialX = (int)Mcs.ViewerX;
            initialY = (int)Mcs.ViewerY;
        }
#endif

#if MACOS
        //MAC版ではLoad時にイベントのセットなどの紐付けが必要
        public override void WindowDidLoad()
        {
            //サイズ変更イベント検知
            Window.DidEndLiveResize += (object sender, EventArgs e) =>
            {
                LogWriter.DebugLog("ウインドウサイズの変更を検知しました。");
                //ただし、下限より小さい値にした場合は変更を受け付けないようにする(予定)

                Mcs.ViewerWidth = base.Window.Frame.Width;
                Mcs.ViewerHeight = base.Window.Frame.Height;
                ((ViewerViewController)base.ContentViewController).ResetValue();

            };

            //位置変更イベント検知
            Window.DidMove += (object sender, EventArgs e) =>
            {
                Mcs.ViewerX = base.Window.Frame.X;
                Mcs.ViewerY = base.Window.Frame.Y;
            };

            //閉じるイベント検知
            Window.WillClose += (object sender, EventArgs e) =>
            {
                LogWriter.DebugLog("ウインドウを閉じます。");
                Mcs.ViewerOpenFlg = 0;
            };
        }
#elif WINDOWS
        //ViewerWindowController.viewDidLoad内のサイズ変更イベント
        private void DidEndLiveResize(object sender, System.EventArgs e)
        {
            LogWriter.DebugLog("ウインドウサイズの変更を検知しました。");
            Mcs.ViewerWidth = this.Width;
            Mcs.ViewerHeight = this.Height;
            viewerView.ResetValue();
        }

        //ViewerWindowController.viewDidLoad内の位置変更イベント
        private void DidMove(object sender, System.EventArgs e)
        {
            LogWriter.DebugLog("ウインドウ位置の変更を検知しました。");
            Mcs.ViewerX = this.Left;
            Mcs.ViewerY = this.Top;
        }

        //ViewerWindowController.viewDidLoad内の閉じるイベント
        private void WillClose(object sender, FormClosingEventArgs e)
        {
            LogWriter.DebugLog("ウインドウを閉じます。");
            Mcs.ViewerOpenFlg = 0;
        }
#endif

        public void ChangeWindowSize()
        {
#if MACOS
            base.Window.SetFrame(new CoreGraphics.CGRect(Mcs.ViewerX, Mcs.ViewerY, Mcs.ViewerWidth, Mcs.ViewerHeight), false);
#elif WINDOWS
            this.Bounds = new Rectangle((int)Mcs.ViewerX, (int)Mcs.ViewerY, (int)Mcs.ViewerWidth, (int)Mcs.ViewerHeight);
#endif
        }

#if WINDOWS
        private void ShownEvent(object sender, System.EventArgs e)
        {
            //表示した瞬間にWindows側で勝手に位置を変更し、位置変更イベントが発生してしまう。
            //Mcsの座標は変更された位置に更新されてしまうので、
            //初期値で再度上書きしてウインドウ位置の変更を行う
            Mcs.ViewerX = initialX;
            Mcs.ViewerY = initialY;
            ChangeWindowSize();
            //ViewController側の表示処理を追加
            viewerView.ViewDidAppear();
        }

        private void InitializeComponent()
        {
            this.SuspendLayout();
            // 
            // ViewerWindow
            // 
            this.ClientSize = new System.Drawing.Size(284, 261);
            this.Name = "ViewerWindow";
            this.Text = "Viewer";
            this.ResumeLayout(false);

        }
#endif
    }
}
