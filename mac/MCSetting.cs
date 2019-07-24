//Windows,Mac共通ロジック
//差分についてはCompile時のフラグで切り替えてもいいが、Code中は頭でDEFINEしてしまう
#define MACOS
//#define WINDOWS
using System;
using System.IO;
using Newtonsoft.Json;

namespace MameComment
{
    public class MCSetting
    {
        public Double BoardX = 0, BoardY = 0, BoardWidth = 640, BoardHeight = 320, ViewerX = 0, ViewerY = 0, ViewerWidth = 640, ViewerHeight = 320, ViewerFontSize = 12.0;
        public String AccUrl = "";
        public String BoardFont = "Arial", ViewerFont = "Arial";
        public String BoardFontColor = "White", BoardBgColor = "Green", ViewerFontColor = "Black", ViewerBgColor = "White";
        public int BoardLine = 10;
        public String LogFlg = "On";
        public String LogPath = "";
        public int BoardOpenFlg = 0;
        public int ViewerOpenFlg = 0;
#if MACOS
        private String IniPath = Environment.GetFolderPath(Environment.SpecialFolder.Personal) + "/Library/Application Support/MameComment/MameComment.ini";
#elif WINDOWS
        private String IniPath = System.Environment.CurrentDirectory + System.IO.Path.DirectorySeparatorChar + "MameComment.ini";
#endif
        public MCSetting()
        {
        }

        public String GetValues()
        {
            String ResultString = "{\n";
            ResultString = ResultString + "  \"AccUrl\":         \"" + AccUrl + "\",\n";
            ResultString = ResultString + "  \"BoardX\":           " + BoardX + ",\n";
            ResultString = ResultString + "  \"BoardY\":           " + BoardY + ",\n";
            ResultString = ResultString + "  \"BoardWidth\":       " + BoardWidth + ",\n";
            ResultString = ResultString + "  \"BoardHeight\":      " + BoardHeight + ",\n";
            ResultString = ResultString + "  \"BoardFont\":      \"" + BoardFont + "\",\n";
            ResultString = ResultString + "  \"BoardFontColor\":      \"" + BoardFontColor + "\",\n";
            ResultString = ResultString + "  \"BoardBgColor\":      \"" + BoardBgColor + "\",\n";
            ResultString = ResultString + "  \"BoardLine\":        " + BoardLine + ",\n";
            ResultString = ResultString + "  \"ViewerX\":          " + ViewerX + ",\n";
            ResultString = ResultString + "  \"ViewerY\":          " + ViewerY + ",\n";
            ResultString = ResultString + "  \"ViewerWidth\":      " + ViewerWidth + ",\n";
            ResultString = ResultString + "  \"ViewerHeight\":     " + ViewerHeight + ",\n";
            ResultString = ResultString + "  \"ViewerFont\":     \"" + ViewerFont + "\",\n";
            ResultString = ResultString + "  \"ViewerFontSize\":   " + ViewerFontSize + ",\n";
            ResultString = ResultString + "  \"ViewerFontColor\":     \"" + ViewerFontColor + "\",\n";
            ResultString = ResultString + "  \"ViewerBgColor\":     \"" + ViewerBgColor + "\",\n";
            ResultString = ResultString + "  \"LogFlg\":         \"" + LogFlg + "\",\n";
            ResultString = ResultString + "  \"LogPath\":        \"" + LogPath + "\"\n";
            ResultString = ResultString + "}";
            return ResultString;
        }

        public String GetAccKey()
        {
            String Result = "";
            if (!AccUrl.Equals(""))
            {
                int StartPoint = AccUrl.IndexOf("access_token=", StringComparison.CurrentCulture) + 13;
                int EndPoint = AccUrl.IndexOf("&", StringComparison.CurrentCulture);
                if (StartPoint != 0 && EndPoint != 0 || EndPoint > StartPoint)
                {
                    Result = AccUrl.Substring(StartPoint, EndPoint - StartPoint);
                }
            }
            return Result;
        }

        public Boolean CheckUpdate(String JsonString)
        {
            Boolean Result = false;
            try
            {
                if (JsonConvert.DeserializeObject(GetValues()).Equals(JsonConvert.DeserializeObject(JsonString)))
                {
                    Result = true;
                }
            }
            catch (Exception e)
            {
                LogWriter.ErrorLog("更新の確認処理でJSONデータへの変換中にエラーが発生しました。\nJSON文字列：\n" + JsonString + "\nスタックトレース：\n" + e.StackTrace);
            }
            return Result;
        }

        public Boolean Update(String JsonString)
        {
            try
            {
                CopyItems(JsonConvert.DeserializeObject<MCSetting>(JsonString));
                return true;
            }
            catch (Exception e)
            {
                LogWriter.ErrorLog("更新処理でJSONデータへの変換中にエラーが発生しました。\nJSON文字列：\n" + JsonString + "\nスタックトレース：\n" + e.StackTrace);
                return false;
            }
        }

        public Boolean ReadFromIni()
        {
            Boolean Result = true;
            StreamReader JsonReader = null;
            String JsonString = "";
            try
            {
                JsonReader = new StreamReader(IniPath);
                JsonString = JsonReader.ReadToEnd();
                //Windowsのドライブレターを内部的には複製する
                //Mac版では影響ないはずなのでIFDEFにはしない
                JsonString = DuplicatePathWord(JsonString);
                CopyItems(JsonConvert.DeserializeObject<MCSetting>(JsonString));
            }
            catch (FileNotFoundException)
            {
                LogWriter.ErrorLog("INIファイルが見つかりません。(" + IniPath + ")");
                Result = false;
            }
            catch (IOException)
            {
                LogWriter.ErrorLog("INIファイルの読み込み中にエラーが発生しました。(" + IniPath + ")");
                Result = false;
            }
            catch (Exception e)
            {
                LogWriter.ErrorLog("INIファイルの読み込みでJSONデータへの変換中にエラーが発生しました。\nJSON文字列：" + JsonString + "\nスタックトレース：\n" + e.StackTrace);
            }
            finally
            {
                try
                {
                    if (JsonReader != null)
                    {
                        JsonReader.Close();
                    }
                }
                catch (Exception)
                {

                }
            }
            GetAccKey();
            return Result;
        }

        public Boolean WriteToIni()
        {
            StreamWriter JsonWriter = null;
            String JsonString;
            Boolean Result = true;
            try
            {
                JsonWriter = new StreamWriter(IniPath);
                JsonString = GetValues();
                JsonWriter.Write(JsonString);
                JsonWriter.Flush();
            }
            catch (IOException)
            {
                LogWriter.ErrorLog("INIファイルへの書き込みでエラーが発生しました。(" + IniPath + ")");
                Result = false;
            }
            finally
            {
                try
                {
                    if (JsonWriter != null)
                    {
                        JsonWriter.Close();
                    }
                }
                catch (Exception)
                {

                }
            }
            return Result;
        }

        private void CopyItems(MCSetting Src)
        {
            if (Src != null)
            {
                this.AccUrl = Src.AccUrl;
                this.BoardX = Src.BoardX;
                this.BoardY = Src.BoardY;
                this.BoardWidth = Src.BoardWidth;
                this.BoardHeight = Src.BoardHeight;
                this.BoardFont = Src.BoardFont;
                this.BoardLine = Src.BoardLine;
                this.BoardFontColor = Src.BoardFontColor;
                this.BoardBgColor = Src.BoardBgColor;
                this.ViewerX = Src.ViewerX;
                this.ViewerY = Src.ViewerY;
                this.ViewerWidth = Src.ViewerWidth;
                this.ViewerHeight = Src.ViewerHeight;
                this.ViewerFont = Src.ViewerFont;
                this.ViewerFontSize = Src.ViewerFontSize;
                this.ViewerFontColor = Src.ViewerFontColor;
                this.ViewerBgColor = Src.ViewerBgColor;
                this.LogFlg = Src.LogFlg;
                this.LogPath = Src.LogPath;
            }
        }

        public String DuplicatePathWord(String SrcString)
        {
            String Result;
            Result = SrcString.Replace("\\", "\\\\");
            return Result;
        }

        public String DeduplicatePathWord(String SrcString)
        {
            String Result;
            Result = SrcString.Replace("\\\\", "\\");
            return Result;
        }
    }
}
