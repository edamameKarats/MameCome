//Windows,Macソース共有、ファイル名だけ変える
//Windows:SettingWindow.xaml.cs,Mac:SettingViewController
//差分についてはCompile時のフラグで切り替えてもいいが、Code中は頭でDEFINEしてしまう
#define MACOS
//#define WINDOWS
using System;
#if MACOS
using Foundation;
using AppKit;
#elif WINDOWS
using System.Drawing.Text;
using System.Windows;
using System.Windows.Media;
#endif

namespace MameComment
{
#if MACOS
    public partial class SettingViewController : NSViewController
    {
#elif WINDOWS
    public partial class SettingWindow : Window
    {
#endif
        public MCSetting Mcs;

#if MACOS
        public SettingViewController(IntPtr handle) : base(handle)
        {

        }
#elif WINDOWS
        public SettingWindow()
        {
            InitializeComponent();
            String[] colorList = ColorList.GetKey();
            for (int i = 0; i < colorList.Length; i++)
            {
                ValueBoardBgColorBox.Items.Add(colorList[i]);
                ValueBoardFontColorBox.Items.Add(colorList[i]);
                ValueViewerBgColorBox.Items.Add(colorList[i]);
                ValueViewerFontColorBox.Items.Add(colorList[i]);
            }
            InstalledFontCollection installedFontCollection = new InstalledFontCollection();
            for (int i = 0; i < installedFontCollection.Families.Length; i++)
            {
                ValueBoardFontBox.Items.Add(installedFontCollection.Families[i].Name);
                ValueViewerFontBox.Items.Add(installedFontCollection.Families[i].Name);
            }
        }
#endif

#if MACOS
        public override void ViewDidLoad()
        {
            String[] colorList = ColorList.GetKey();
            for (int i = 0; i < colorList.Length; i++)
            {
                ValueBoardBgColorBox.Add((NSString)colorList[i]);
                ValueBoardFontColorBox.Add((NSString)colorList[i]);
                ValueViewerBgColorBox.Add((NSString)colorList[i]);
                ValueViewerFontColorBox.Add((NSString)colorList[i]);
            }
            NSFontManager fm = new NSFontManager();
            for (int i = 0; i < fm.AvailableFontFamilies.Length; i++)
            {
                ValueBoardFontBox.Add((NSString)fm.AvailableFontFamilies[i]);
                ValueViewerFontBox.Add((NSString)fm.AvailableFontFamilies[i]);
            }

        }
#endif


#if MACOS
        partial void ValueSaveBtn(NSObject sender)
#elif WINDOWS
        private void ValueSaveBtn(object sender, RoutedEventArgs e)
#endif
        {
            LogWriter.DebugLog("設定を保存します。");
            //値を更新する
            InputCheck();
            Mcs.Update(CreateJsonString());
            Mcs.WriteToIni();
#if MACOS
            DismissViewController((NSViewController)Self);
#elif WINDOWS
            this.Close();
#endif
        }

#if MACOS
        partial void ValueCancelBtn(NSObject sender)
#elif WINDOWS
        private void ValueCancelBtn(object sender, RoutedEventArgs e)
#endif
        {
            LogWriter.DebugLog("キャンセルが要求されました。");
            //TODO 更新されているものがあればエラーを出してあげ処理を追記

#if MACOS
            DismissViewController((NSViewController)Self);
#elif WINDOWS
            this.Close();
#endif
        }

#if MACOS
        public override void ViewWillAppear()
#elif WINDOWS
        //Windowsだと表示後だけど気にしない
        private void ViewWillAppear(object sender, EventArgs e)
#endif
        {
#if MACOS
            base.ViewWillAppear();
#endif

            LogWriter.DebugLog("ウインドウがOpenされます。設定済みの値を読み込みます。");
            //ウインドウが開くタイミングで、設定済みのMcsから値を読み込む
#if MACOS
            ValueAccUrl.StringValue = Mcs.AccUrl;
            ValueBoardX.StringValue = Mcs.BoardX.ToString();
            ValueBoardY.StringValue = Mcs.BoardY.ToString();
            ValueBoardWidth.StringValue = Mcs.BoardWidth.ToString();
            ValueBoardHeight.StringValue = Mcs.BoardHeight.ToString();
            ValueBoardFontBox.StringValue = Mcs.BoardFont;
            ValueBoardLine.StringValue = Mcs.BoardLine.ToString();
            ValueBoardBgColorBox.StringValue = Mcs.BoardBgColor;
            ValueBoardFontColorBox.StringValue = Mcs.BoardFontColor;
            ValueViewerX.StringValue = Mcs.ViewerX.ToString();
            ValueViewerY.StringValue = Mcs.ViewerY.ToString();
            ValueViewerWidth.StringValue = Mcs.ViewerWidth.ToString();
            ValueViewerHeight.StringValue = Mcs.ViewerHeight.ToString();
            ValueViewerFontBox.StringValue = Mcs.ViewerFont;
            ValueViewerFontSize.StringValue = Mcs.ViewerFontSize.ToString();
            ValueViewerFontColorBox.StringValue = Mcs.ViewerFontColor;
            ValueViewerBgColorBox.StringValue = Mcs.ViewerBgColor;
            ValueLogPath.StringValue = Mcs.LogPath;
#elif WINDOWS
            ValueAccUrl.Text = Mcs.AccUrl;
            ValueBoardX.Text = Mcs.BoardX.ToString();
            ValueBoardY.Text = Mcs.BoardY.ToString();
            ValueBoardWidth.Text = Mcs.BoardWidth.ToString();
            ValueBoardHeight.Text = Mcs.BoardHeight.ToString();
            //            ValueBoardFont.Text = Mcs.BoardFont;
            ValueBoardFontBox.Text = Mcs.BoardFont;
            ValueBoardLine.Text = Mcs.BoardLine.ToString();
            //            ValueBoardFontColor.Text = Mcs.BoardFontColor;
            ValueBoardFontColorBox.Text = Mcs.BoardFontColor;
            //            ValueBoardBgColor.Text = Mcs.BoardBgColor;
            ValueBoardBgColorBox.Text = Mcs.BoardBgColor;
            ValueViewerX.Text = Mcs.ViewerX.ToString();
            ValueViewerY.Text = Mcs.ViewerY.ToString();
            ValueViewerWidth.Text = Mcs.ViewerWidth.ToString();
            ValueViewerHeight.Text = Mcs.ViewerHeight.ToString();
            //            ValueViewerFont.Text = Mcs.ViewerFont;
            ValueViewerFontBox.Text = Mcs.ViewerFont;
            ValueViewerFontSize.Text = Mcs.ViewerFontSize.ToString();
            //            ValueViewerFontColor.Text = Mcs.ViewerFontColor;
            ValueViewerFontColorBox.Text = Mcs.ViewerFontColor;
            //            ValueViewerBgColor.Text = Mcs.ViewerBgColor;
            ValueViewerBgColorBox.Text = Mcs.ViewerBgColor;
            ValueLogPath.Text = Mcs.DeduplicatePathWord(Mcs.LogPath);
#endif

            if (Mcs.LogFlg.Equals("On"))
            {
#if MACOS
                ValueLogFlg.State = NSCellStateValue.On;
#elif WINDOWS
                ValueLogFlg.IsChecked = true;
#endif
            }
            else
            {
#if MACOS
                ValueLogFlg.State = NSCellStateValue.Off;
#elif WINDOWS
                ValueLogFlg.IsChecked = false;
#endif
            }

        }


#if MACOS
        public override void ViewWillDisappear()
#elif WINDOWS
        private void ViewWillDisappear(object sender, System.ComponentModel.CancelEventArgs e)
#endif
        {
            //閉じるボタンを押したり、ウインドウを閉じるときの共通処理があれば
#if MACOS
            base.ViewWillDisappear();
#endif
            LogWriter.DebugLog("ウインドウを閉じます。設定は以下のように変更されました。\n" + Mcs.GetValues());

            //閉じたあと、Mcsの値を反映させたいので親のコントローラーを取得して閉じたときの関数を呼び出す
#if MACOS
            MainViewController Parent = (MainViewController)PresentingViewController;
            Parent.CloseSettingWindow();
#elif WINDOWS
            ((MainWindow)(this.Owner)).CloseSettingWindow();
#endif
        }

        private String CreateJsonString()
        {
            LogWriter.DebugLog("設定値から、JSON文字列を生成します。");
            String ResultString = "{\n";
#if MACOS
            ResultString = ResultString + "  \"AccUrl\":         \"" + ValueAccUrl.StringValue + "\",\n";
            ResultString = ResultString + "  \"BoardX\":           " + ValueBoardX.StringValue + ",\n";
            ResultString = ResultString + "  \"BoardY\":           " + ValueBoardY.StringValue + ",\n";
            ResultString = ResultString + "  \"BoardWidth\":       " + ValueBoardWidth.StringValue + ",\n";
            ResultString = ResultString + "  \"BoardHeight\":      " + ValueBoardHeight.StringValue + ",\n";
            ResultString = ResultString + "  \"BoardFont\":      \"" + ValueBoardFontBox.StringValue + "\",\n";
            ResultString = ResultString + "  \"BoardLine\":        " + ValueBoardLine.StringValue + ",\n";
            ResultString = ResultString + "  \"BoardFontColor\": \"" + ValueBoardFontColorBox.StringValue + "\",\n";
            ResultString = ResultString + "  \"BoardBgColor\":   \"" + ValueBoardBgColorBox.StringValue + "\",\n";
            ResultString = ResultString + "  \"ViewerX\":          " + ValueViewerX.StringValue + ",\n";
            ResultString = ResultString + "  \"ViewerY\":          " + ValueViewerY.StringValue + ",\n";
            ResultString = ResultString + "  \"ViewerWidth\":      " + ValueViewerWidth.StringValue + ",\n";
            ResultString = ResultString + "  \"ViewerHeight\":     " + ValueViewerHeight.StringValue + ",\n";
            ResultString = ResultString + "  \"ViewerFont\":     \"" + ValueViewerFontBox.StringValue + "\",\n";
            ResultString = ResultString + "  \"ViewerFontSize\":   " + ValueViewerFontSize.StringValue + ",\n";
            ResultString = ResultString + "  \"ViewerFontColor\":\"" + ValueViewerFontColorBox.StringValue + "\",\n";
            ResultString = ResultString + "  \"ViewerBgColor\":  \"" + ValueViewerBgColorBox.StringValue + "\",\n";
            ResultString = ResultString + "  \"LogFlg\":         \"" + ValueLogFlg.State.ToString() + "\",\n";
            ResultString = ResultString + "  \"LogPath\":        \"" + ValueLogPath.StringValue + "\"\n";
#elif WINDOWS
            ResultString = ResultString + "  \"AccUrl\":         \"" + ValueAccUrl.Text + "\",\n";
            ResultString = ResultString + "  \"BoardX\":           " + ValueBoardX.Text + ",\n";
            ResultString = ResultString + "  \"BoardY\":           " + ValueBoardY.Text + ",\n";
            ResultString = ResultString + "  \"BoardWidth\":       " + ValueBoardWidth.Text + ",\n";
            ResultString = ResultString + "  \"BoardHeight\":      " + ValueBoardHeight.Text + ",\n";
            ResultString = ResultString + "  \"BoardFont\":      \"" + ValueBoardFontBox.Text + "\",\n";
            ResultString = ResultString + "  \"BoardLine\":        " + ValueBoardLine.Text + ",\n";
            ResultString = ResultString + "  \"BoardFontColor\": \"" + ValueBoardFontColorBox.Text + "\",\n";
            ResultString = ResultString + "  \"BoardBgColor\":   \"" + ValueBoardBgColorBox.Text + "\",\n";
            ResultString = ResultString + "  \"ViewerX\":          " + ValueViewerX.Text + ",\n";
            ResultString = ResultString + "  \"ViewerY\":          " + ValueViewerY.Text + ",\n";
            ResultString = ResultString + "  \"ViewerWidth\":      " + ValueViewerWidth.Text + ",\n";
            ResultString = ResultString + "  \"ViewerHeight\":     " + ValueViewerHeight.Text + ",\n";
            ResultString = ResultString + "  \"ViewerFont\":     \"" + ValueViewerFontBox.Text + "\",\n";
            ResultString = ResultString + "  \"ViewerFontSize\":   " + ValueViewerFontSize.Text + ",\n";
            ResultString = ResultString + "  \"ViewerFontColor\":\"" + ValueViewerFontColorBox.Text + "\",\n";
            ResultString = ResultString + "  \"ViewerBgColor\":  \"" + ValueViewerBgColorBox.Text + "\",\n";
            if ((bool)ValueLogFlg.IsChecked)
            {
                ResultString = ResultString + "  \"LogFlg\":         \"On\",\n";
            }
            else
            {
                ResultString = ResultString + "  \"LogFlg\":         \"Off\",\n";
            }
            //読み込まれた文字列は\が1個なので、内部使用様に\を複製する。
            ResultString = ResultString + "  \"LogPath\":        \"" + Mcs.DuplicatePathWord(ValueLogPath.Text) + "\"\n";
#endif
            ResultString = ResultString + "}";
            LogWriter.DebugLog("生成された文字列は以下です。\n" + ResultString);
            return ResultString;
        }

        private void InputCheck()
        {
            LogWriter.DebugLog("入力値を確認し、必要であればデフォルト値を設定します。");
            int ErrFlg = 0;
            String ErrItem = "";
#if MACOS
            if (ValueAccUrl.StringValue.Equals(""))
#elif WINDOWS
            if (ValueAccUrl.Text.Equals(""))
#endif
            {
                ErrFlg += 1;
                ErrItem = ErrItem + "\n アカウントトークンURL";
            }
#if MACOS
            if (ValueBoardX.StringValue.Equals(""))
            {
                ValueBoardX.StringValue = "0.0";
            }
#elif WINDOWS
            if (ValueBoardX.Text.Equals(""))
            {
                ValueBoardX.Text = "0.0";
            }
#endif

#if MACOS
            if (ValueBoardY.StringValue.Equals(""))
            {
                ValueBoardY.StringValue = "0.0";
            }
#elif WINDOWS
            if (ValueBoardY.Text.Equals(""))
            {
                ValueBoardY.Text = "0.0";
            }
#endif

#if MACOS
            if (ValueBoardWidth.StringValue.Equals(""))
            {
                ValueBoardWidth.StringValue = "0.0";
            }
#elif WINDOWS
            if (ValueBoardWidth.Text.Equals(""))
            {
                ValueBoardWidth.Text = "0.0";
            }
#endif

#if MACOS
            if (ValueBoardHeight.StringValue.Equals(""))
            {
                ValueBoardHeight.StringValue = "0.0";
            }
#elif WINDOWS
            if (ValueBoardHeight.Text.Equals(""))
            {
                ValueBoardHeight.Text = "0.0";
            }
#endif

#if MACOS
            if (ValueBoardFontBox.StringValue.Equals(""))
            {
                ValueBoardFontBox.StringValue = "Arial";
            }
#elif WINDOWS
            if (ValueBoardFontBox.Text.Equals(""))
            {
                ValueBoardFontBox.Text = "Arial";
            }
#endif
            else
            {
                try
                {
#if MACOS
                    NSFont DefaultBoardFont = NSFont.FromFontName(ValueBoardFontBox.StringValue, 10);
                    LogWriter.DebugLog("Font is ok. Font name is " + DefaultBoardFont.FontName);
#elif WINDOWS
                    FontFamily DefaultBoardFont = new FontFamily(ValueBoardFontBox.Text);
                    LogWriter.DebugLog("Font is ok. Font name is " + DefaultBoardFont.ToString());
#endif
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
#if MACOS
                    ValueBoardFontBox.StringValue = "Arial";
#elif WINDOWS
                    ValueBoardFontBox.Text = "Arial";
#endif
                }
            }

#if MACOS
            if (ValueBoardFontColorBox.StringValue.Equals(""))
            {
                ValueBoardFontColorBox.StringValue = "White";
            }
#elif WINDOWS
            if (ValueBoardFontColorBox.Text.Equals(""))
            {
                ValueBoardFontColorBox.Text = "White";
            }
#endif
#if MACOS
            if (ValueBoardBgColorBox.StringValue.Equals(""))
            {
                ValueBoardBgColorBox.StringValue = "Green";
            }
#elif WINDOWS
            if (ValueBoardBgColorBox.Text.Equals(""))
            {
                ValueBoardBgColorBox.Text = "Green";
            }
#endif

#if MACOS
            if (ValueBoardLine.StringValue.Equals(""))
            {
                ValueBoardLine.StringValue = "1";
            }
#elif WINDOWS
            if (ValueBoardLine.Text.Equals(""))
            {
                ValueBoardLine.Text = "1";
            }
#endif

#if MACOS
            if (ValueViewerX.StringValue.Equals(""))
            {
                ValueViewerX.StringValue = "0.0";
            }
#elif WINDOWS
            if (ValueViewerX.Text.Equals(""))
            {
                ValueViewerX.Text = "0.0";
            }
#endif

#if MACOS
            if (ValueViewerY.StringValue.Equals(""))
            {
                ValueViewerY.StringValue = "0.0";
            }
#elif WINDOWS
            if (ValueViewerY.Text.Equals(""))
            {
                ValueViewerY.Text = "0.0";
            }
#endif

#if MACOS
            if (ValueViewerWidth.StringValue.Equals(""))
            {
                ValueViewerWidth.StringValue = "0.0";
            }
#elif WINDOWS
            if (ValueViewerWidth.Text.Equals(""))
            {
                ValueViewerWidth.Text = "0.0";
            }
#endif

#if MACOS
            if (ValueViewerHeight.StringValue.Equals(""))
            {
                ValueViewerHeight.StringValue = "0.0";
            }
#elif WINDOWS
            if (ValueViewerHeight.Text.Equals(""))
            {
                ValueViewerHeight.Text = "0.0";
            }
#endif

#if MACOS
            if (ValueViewerFontBox.StringValue.Equals(""))
            {
                ValueViewerFontBox.StringValue = "Arial";
            }
#elif WINDOWS
            if (ValueViewerFontBox.Text.Equals(""))
            {
                ValueViewerFontBox.Text = "Arial";
            }
#endif
            else
            {
                try
                {
#if MACOS
                    NSFont DefaultViewerFont = NSFont.FromFontName(ValueViewerFontBox.StringValue, 10);
                    LogWriter.DebugLog("Font is ok. Font name is " + DefaultViewerFont.FontName);
#elif WINDOWS
                    FontFamily DefaultViewerFont = new FontFamily(ValueViewerFontBox.Text);
                    LogWriter.DebugLog("Font is ok. Font name is " + DefaultViewerFont.ToString());
#endif
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
#if MACOS
                    ValueViewerFontBox.StringValue = "Arial";
#elif WINDOWS
                    ValueViewerFontBox.Text = "Arial";
#endif
                }
            }
#if MACOS
            if (ValueViewerFontSize.StringValue.Equals(""))
            {
                ValueViewerFontSize.StringValue = "0.0";
            }
#elif WINDOWS
            if (ValueViewerFontSize.Text.Equals(""))
            {
                ValueViewerFontSize.Text = "0.0";
            }
#endif

#if MACOS
            if (ValueViewerFontColorBox.StringValue.Equals(""))
            {
                ValueViewerFontColorBox.StringValue = "Black";
            }
#elif WINDOWS
            if (ValueViewerFontColorBox.Text.Equals(""))
            {
                ValueViewerFontColorBox.Text = "Black";
            }
#endif
#if MACOS
            if (ValueViewerBgColorBox.StringValue.Equals(""))
            {
                ValueViewerBgColorBox.StringValue = "White";
            }
#elif WINDOWS
            if (ValueViewerBgColorBox.Text.Equals(""))
            {
                ValueViewerBgColorBox.Text = "White";
            }
#endif

#if MACOS
            if (ValueLogFlg.State.ToString().Equals("On") && ValueLogPath.StringValue.Equals(""))
#elif WINDOWS
            if (ValueLogFlg.IsChecked.Equals(true) && ValueLogPath.Text.Equals(""))
#endif
            {
                ErrFlg += 1;
                ErrItem = ErrItem + "\n ログパス";
            }

            if (ErrFlg > 0)
            {
#if MACOS
                using (var alert = new NSAlert())
                {
                    alert.AlertStyle = NSAlertStyle.Warning;
                    alert.MessageText = "警告";
                    alert.InformativeText = "以下のパラメータが適切に設定されていないため、正常に動作しない可能性があります。" + ErrItem;
                    alert.RunSheetModal(null);
                }
#elif WINDOWS
                MessageBox.Show("以下のパラメータが適切に設定されていないため、正常に動作しない可能性があります。" + ErrItem,
                "警告", MessageBoxButton.OK, MessageBoxImage.Exclamation);
#endif
            }

        }

    }
}
