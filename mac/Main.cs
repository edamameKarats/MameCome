//MAC固有ロジック
using System;
using AppKit;
using Plugin.Clipboard;

namespace MameComment
{
    static class MainClass
    {
        static void Main(string[] args)
        {
            System.AppDomain.CurrentDomain.UnhandledException += new UnhandledExceptionEventHandler(UserExceptionHandler);
            NSApplication.Init();
            NSApplication.Main(args);
        }

        static void UserExceptionHandler(object sender, UnhandledExceptionEventArgs e){

            using (var alert = new NSAlert())
            {
                alert.AlertStyle = NSAlertStyle.Informational;
                alert.MessageText = "予期せぬエラー";
                alert.AddButton("終了");
                alert.AddButton("例外をコピーして終了");
                alert.InformativeText = ("以下の例外が発生し、プログラムが異常終了しました。\n" + ((Exception)e.ExceptionObject).Message + "\n" + ((Exception)e.ExceptionObject).StackTrace);
                nint response=alert.RunSheetModal(null);
                if (response==(int)NSAlertButtonReturn.Second)
                {
                    CrossClipboard.Current.SetText(((Exception)e.ExceptionObject).Message + "\n" + ((Exception)e.ExceptionObject).StackTrace);
                }

            }
        }
    }
}
