//Windows,Macソース共有、ファイル名だけ変える
//Windows:BoardView.xaml.cs,Mac:BoardViewController
//差分についてはCompile時のフラグで切り替えてもいいが、Code中は頭でDEFINEしてしまう
#define MACOS
//#define WINDOWS
#if MACOS
using System;
using Foundation;
using AppKit;
using CoreGraphics;
using System.Collections.Generic;
#elif WINDOWS
using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
#endif

namespace MameComment
{

#if MACOS
    public partial class BoardViewController : NSViewController
#elif WINDOWS
    public partial class BoardView : UserControl
#endif
    {
        private MCSetting Mcs;
#if WINDOWS
        public BoardWindow ParentWindow;
        private const int winWindowHeight = 43;
        private const int winWindowWidth = 14;

#endif

        public MCLabel[] Mclabels;
        private const int LabelNum = 50;

#if MACOS
        public BoardViewController(IntPtr handle) : base(handle)
        {

        }
#elif WINDOWS
        public BoardView()
        {
            InitializeComponent();
        }
#endif
#if MACOS
        public override void ViewDidLoad()
        {
            base.ViewDidLoad();
            //再描画をリアルタイムに実行させるためにはレイヤー化が必要
            base.View.WantsLayer = true;
        }
#endif

#if MACOS
        public override void ViewDidAppear()
#elif WINDOWS
        public void ViewDidAppear()
#endif
        {
            //ラベルの配列を作成する(LabelNum個)
            Mclabels = new MCLabel[LabelNum];
            for (int i = 0; i < LabelNum; i++)
            {
                Mclabels[i] = new MCLabel();
#if MACOS
                Mclabels[i].TextColor = NSColor.FromCGColor(ColorList.GetColor(Mcs.BoardFontColor));
#elif WINDOWS
                Mclabels[i].Foreground = new SolidColorBrush(ColorList.GetColor(Mcs.BoardFontColor));
#endif
#if MACOS
                //MACの場合はViewにAddSubviewする
                CommentArea.AddSubview(Mclabels[i]);
#elif WINDOWS
                //Windowsの場合はForm(Control)に直接Addする
                BoardArea.Children.Add(Mclabels[i]);
#endif
            }

#if MACOS
            //コメントエリアの背景色を変更する
            CommentArea.ChangeBgColor(ColorList.GetColor(Mcs.BoardBgColor));
            //Windows版ではコメントエリアはもともと透過色なので何もしない
#endif
            //ボタンサイズなどを実態に合わせて変更する
            ResetValue();
        }

        public void SetValue(MCSetting ParentMcs)
        {
            Mcs = ParentMcs;
        }

        public void ResetValue()
        {
            LogWriter.DebugLog("画面サイズを変更します。");
#if MACOS
            //オブジェクトの大きさを変更
            CommentArea.SetFrameSize(new CoreGraphics.CGSize(Mcs.BoardWidth, Mcs.BoardHeight + MacConstructor.LayerY));
            ControlAlphaBtn.SetFrameSize(new CoreGraphics.CGSize(Mcs.BoardWidth + MacConstructor.LayerX, 21.0));
#elif WINDOWS
            ControlAlphaBtn.Width = (int)Mcs.BoardWidth - winWindowWidth -2;
#endif
            //ラベルのフォントサイズを変更
#if MACOS
            float FontSize = (float)Math.Floor(CommentArea.Frame.Height / Mcs.BoardLine * 0.8);
#elif WINDOWS
            double FontSize = Math.Floor((Mcs.BoardHeight - ControlAlphaBtn.Height - winWindowHeight) / Mcs.BoardLine * 0.8 - 4);
#endif
            for (int i = 0; i < LabelNum; i++)
            {
#if MACOS
                Mclabels[i].Font = NSFont.FromFontName(Mcs.BoardFont, FontSize);
                Mclabels[i].SetFrameSize(new CoreGraphics.CGSize(Mcs.BoardWidth, (float)Math.Floor(CommentArea.Frame.Height / Mcs.BoardLine)));
                //Labelの場合文字の上に余白があり、その補正のために+４を入れている
                Mclabels[i].SetFrame(i, Mcs.BoardLine, Mcs.BoardWidth, Mcs.BoardHeight + MacConstructor.LayerY + 4);
#elif WINDOWS
                Mclabels[i].FontFamily = new FontFamily(Mcs.BoardFont);
                Mclabels[i].FontSize = FontSize;
                Mclabels[i].SetFrame(i, Mcs.BoardLine, Mcs.BoardWidth, Mcs.BoardHeight - winWindowHeight);
#endif
            }
        }


        //外部からメッセージを流すには、この関数を使用する
        //とにかく投げてくれれば、いまアニメーションを行っていないラベルを探して流してくれる
        //実際の配置場所とかはMCLabelのほうでやってくれている
        public void DisplayBoardMessage(String Message)
        {
            for (int i = 0; i < LabelNum; i++)
            {
                //LogWriter.DebugLog(i+"番目のラベルは"+Mclabels[i].IsUsed);
                if (Mclabels[i].IsUsed == 0)
                {
                    if (Mclabels[i].MoveFrame(Message) == true)
                    {
                        break;
                    }
                }
            }
        }

#if MACOS
        partial void ControlAlpha(NSObject sender)
#elif WINDOWS
        private void ControlAlpha(object sender, RoutedEventArgs e)
#endif
        {
#if MACOS
            if (CommentArea.GetBgColor().Alpha == 0)
#elif WINDOWS
            if (ParentWindow.TransparencyKey != System.Drawing.Color.Empty)
#endif
            {
#if MACOS
                CommentArea.ChangeBgColor(ColorList.GetColor(Mcs.BoardBgColor));
#elif WINDOWS
                ParentWindow.TransparencyKey = System.Drawing.Color.Empty;
#endif
            }
            else
            {
#if MACOS
                CommentArea.ChangeBgColor(ColorList.GetColor("Transparent"));
#elif WINDOWS
                Color bgColor = ColorList.GetColor(Mcs.BoardBgColor);
                ParentWindow.TransparencyKey = System.Drawing.Color.FromArgb(bgColor.A,bgColor.R,bgColor.G,bgColor.B);
#endif
            }
        }

    }
}
