//Windows,Mac共通ロジック
using System;
using System.IO;
using System.Text;

namespace MameComment
{
    public class LogWriter
    {
        public LogWriter()
        {
        }
        public MCSetting Mcs;
        private String EncodeString;
        private StreamWriter LogWriteStream = null;

        public void Init(MCSetting McsInput)
        {
            Mcs = McsInput;
            if (Environment.OSVersion.ToString().StartsWith("Unix", StringComparison.CurrentCulture))
            {//macOs
                EncodeString = "utf-8";
            }
            else
            {//Windows
                EncodeString = "shift-jis";
            }
            if (Mcs.LogFlg.Equals("On"))
            {
                if (LogWriteStream != null)
                {
                    try
                    {
                        LogWriteStream.Close();
                    }
                    catch (Exception e)
                    {
                        LogWriter.ErrorLog("再Openのための既存のログ出力ストリームのCloseでエラーが発生しました。\n" + e.StackTrace);
                    }
                }
                if (!Mcs.LogPath.Equals(""))
                {
                    try
                    {
                        LogWriteStream = new StreamWriter(@Mcs.LogPath, true, Encoding.GetEncoding(EncodeString));
                    }
                    catch (Exception e)
                    {
                        LogWriter.ErrorLog("ログ出力ストリームのOpenでエラーが発生しました。\n" + e.StackTrace);
                    }
                }
                else
                {
                    LogWriteStream = null;
                }
            }
            else
            {
                if (LogWriteStream != null)
                {
                    try
                    {
                        LogWriteStream.Close();

                    }
                    catch (Exception e)
                    {
                        LogWriter.ErrorLog("既存のログ出力ストリームのCloseでエラーが発生しました。\n" + e.StackTrace);
                    }
                    LogWriteStream = null;
                }
            }
        }

        public static void DebugLog(String Message)
        {
#if DEBUG
            String ClassName = new System.Diagnostics.StackFrame(1).GetMethod().ReflectedType.Name;
            Console.WriteLine(DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss") + " [" + ClassName + "] " + Message);
#endif
        }

        public static void ErrorLog(String Message)
        {
            String ClassName = new System.Diagnostics.StackFrame(1).GetMethod().ReflectedType.Name;
            Console.WriteLine(DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss") + " [" + ClassName + "] " + Message);
        }



        public void Write(String WriteString)
        {
            if (Mcs.LogFlg.Equals("On") && LogWriteStream != null)
            {
                if (LogWriteStream != null)
                {
                    try
                    {
                        LogWriteStream.WriteLine(WriteString);
                        LogWriteStream.Flush();

                    }
                    catch (Exception e)
                    {
                        LogWriter.ErrorLog("ログ出力ストリームへのWriteでエラーが発生しました。\n" + e.StackTrace);
                    }
                }
            }
/*            else
            {
                Console.WriteLine(WriteString);
            }*/
        }




        public void Close()
        {

            if (LogWriteStream != null)
            {
                try
                {
                    LogWriteStream.Flush();
                    LogWriteStream.Close();
                }
                catch (Exception e)
                {
                    LogWriter.ErrorLog("ログ出力ストリームのCloseでエラーが発生しました。\n" + e.StackTrace);
                }
            }
        }
    }
}
