//Windows,Mac共通ロジック

using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using Newtonsoft.Json;



namespace MameComment
{
    public class TwitcastingHTTPApi
    {
        public TwitcastingHTTPApi()
        {
        }
        public static Boolean Islive(String Id, String AccKey)
        {
            LogWriter.DebugLog("放送中かどうかのステータスを確認します。\nID:" + Id + ", Key:" + AccKey);
            Boolean Result = false;
            String ResContent = GetUsersFromTwitcasting(Id, AccKey);
            try
            {
                Result = Convert.ToBoolean(GetUserPropertyFromJson(ResContent, "is_live"));
            }
            catch (Exception)
            {
                Result = false;
            }
            LogWriter.DebugLog("放送ステータスは" + Result + "です。");
            return Result;
        }

        public static String GetMovieId(String Id, String AccKey)
        {
            LogWriter.DebugLog("放送IDを取得します。\nID:" + Id + ", Key:" + AccKey);
            String Result;
            String ResContent = GetUsersFromTwitcasting(Id, AccKey);
            Result = GetUserPropertyFromJson(ResContent, "last_movie_id");
            LogWriter.DebugLog("放送IDは" + Result + "です。");
            return Result;
        }

        //Returns [UTC UserId UserName UserImage Message]
        public static CommentInformation[] GetComment(String MovieId, String AccKey)
        {
            return GetComment(MovieId, "1", AccKey);
        }

        public static CommentInformation[] GetComment(String MovieId, String SliceId, String AccKey)
        {
            LogWriter.DebugLog("コメントを取得します。\nMovieID：" + MovieId + ", SliceID:" + SliceId + ", Key:" + AccKey);
            String EndPoint = "/movies/" + MovieId + "/comments?limit=50&slice_id=" + SliceId;
            String ResContent = GetFromTwitcasting(EndPoint, AccKey);
            CommentInformation[] Result = GetCommentInformationsFromJson(ResContent);
            //以下、エスケープされている文字を直す
            for (int i = 0; i < Result.Length; i++)
            {
                Result[i].Message = ConvertToString(Result[i].Message);
            }
            return Result;
        }


        private static String GetUsersFromTwitcasting(String Id, String AccKey)
        {
            LogWriter.DebugLog("ユーザー情報を取得します。ID:" + Id);
            String EndPoint = "/users/" + Id;
            String ResContent = GetFromTwitcasting(EndPoint, AccKey);
            return ResContent;
        }

        private static String GetFromTwitcasting(String EndPoint, String AccKey)
        {
            LogWriter.DebugLog("ツイキャスAPIを発行します。取得先エンドポイント:" + EndPoint);
            String StrURL = "https://apiv2.twitcasting.tv/" + EndPoint;
            HttpWebRequest HttpReq = null;
            HttpWebResponse HttpRes = null;
            Stream ResStream = null;
            StreamReader ResStreamReader = null;
            String ResContent = "";
            try
            {
                HttpReq = (HttpWebRequest)WebRequest.Create(StrURL);
                HttpReq.Method = "GET";
                HttpReq.Accept = "application/json";
                HttpReq.Headers.Add("X-Api-Version", "2.0");
                HttpReq.Headers.Add("Authorization", "Bearer " + AccKey);
                HttpRes = (HttpWebResponse)HttpReq.GetResponse();
                ResStream = HttpRes.GetResponseStream();
                ResStreamReader = new StreamReader(ResStream);
                ResContent = ResStreamReader.ReadToEnd();
            }
            catch (Exception e)
            {
                LogWriter.ErrorLog("TwitCastingのAPI発行中にエラーが発生しました。\n取得先エンドポイント：" + EndPoint + "\nエラー内容：\n" + e.StackTrace);
            }
            finally
            {
                try
                {
                    ResStreamReader.Close();
                }
                catch (Exception)
                {

                }
                try
                {
                    ResStream.Close();
                }
                catch (Exception)
                {

                }
                try
                {
                    HttpRes.Close();
                }
                catch (Exception)
                {

                }
            }
            return ResContent;
        }

        private static String GetUserPropertyFromJson(String JsonString, String PropertyToGet)
        {
            LogWriter.DebugLog("Jsonデータからユーザープロパティを取得します。取得プロパティ:" + PropertyToGet);
            String Result = "";
            try
            {
                Dictionary<String, object> ResJson = JsonConvert.DeserializeObject<Dictionary<String, object>>(JsonString);
                Dictionary<String, String> ResUser = JsonConvert.DeserializeObject<Dictionary<String, String>>(ResJson["user"].ToString());
                Result = ResUser[PropertyToGet];
                LogWriter.DebugLog("取得されたプロパティ:" + Result);
            }
            catch (Exception e)
            {
                LogWriter.ErrorLog("ユーザープロパティ情報のパース中にエラーが発生しました。\n" + e.StackTrace);
            }
            return Result;
        }

        private static CommentInformation[] GetCommentInformationsFromJson(String JsonString)
        {
            LogWriter.DebugLog("コメントデータから必要な情報を抽出します。");
            CommentInformation[] Result = null;
            try
            {
                Dictionary<String, object> ResJson = JsonConvert.DeserializeObject<Dictionary<String, object>>(JsonString);
                List<object> CommentList = JsonConvert.DeserializeObject<List<object>>(ResJson["comments"].ToString());
                Result = new CommentInformation[CommentList.Count];
                String Timestamp;
                String UserId;
                String UserName;
                String UserImage;
                String CommentId;
                String Message;
                Dictionary<String, object> CommentData;
                Dictionary<String, String> UserData;
                CommentInformation CInfo;
                for (int i = 0; i < CommentList.Count; i++)
                {
                    CommentData = JsonConvert.DeserializeObject<Dictionary<String, object>>(CommentList[i].ToString());
                    Message = CommentData["message"].ToString();
                    CommentId = CommentData["id"].ToString();
                    Timestamp = DateTimeOffset.FromUnixTimeSeconds(long.Parse(CommentData["created"].ToString())).LocalDateTime.ToString("HH:mm:ss");
                    UserData = JsonConvert.DeserializeObject<Dictionary<String, String>>(CommentData["from_user"].ToString());
                    UserId = UserData["id"];
                    UserName = UserData["name"];
                    UserImage = UserData["image"];
                    CInfo = new CommentInformation();
                    CInfo.Timestamp = Timestamp;
                    CInfo.UserId = UserId;
                    CInfo.UserName = UserName;
                    CInfo.UserImage = UserImage;
                    CInfo.Message = Message;
                    CInfo.CommentId = CommentId;
                    Result[i] = CInfo;
                    LogWriter.DebugLog(i + "番目のデータ:\n" + Result[i].Timestamp + "" + Result[i].CommentId + "" + Result[i].UserId + "" + Result[i].UserName + "" + Result[i].UserImage + "" + Result[i].Message);
                }
            }
            catch (Exception e)
            {
                LogWriter.ErrorLog("コメントの取得中にエラーが発生しました。\n" + e.StackTrace);
            }
            return Result;
        }

        private static String ConvertToString(String InputString)
        {
            String Result = "";
            //"&'<> \nはそのままでも表示できる(Windows)けど、一番下に見切れないようにする処理を入れてないのでブランクに変換する
            Result = InputString.Replace("&quot;", "\"").Replace("&amp;", "&").Replace("&#039;", "'").Replace("&lt;", "<").Replace("&gt;", ">").Replace("\n", " ");
            return Result;
        }

        public static String PostComment(String Comment, String MovieId, String AccKey)
        {
            String EndPoint = "movies/" + MovieId+"/comments" ;
            LogWriter.DebugLog("ツイキャスAPIを発行します。取得先エンドポイント:" + EndPoint);
            String StrURL = "https://apiv2.twitcasting.tv/" + EndPoint;
            HttpWebRequest HttpReq = null;
            HttpWebResponse HttpRes = null;
            String PostData = "{\"comment\": \"" + Comment + "\", \"sns\": \"none\"}";
            Byte[] PostDataBytes =System.Text.Encoding.UTF8.GetBytes(PostData);
            Stream ReqStream = null;
            Stream ResStream = null;
            StreamReader ResStreamReader = null;
            String ResContent = "";
            LogWriter.DebugLog("コメントデータ "+PostData);
            try
            {
                HttpReq = (HttpWebRequest)WebRequest.Create(StrURL);
                HttpReq.Method = "POST";
                HttpReq.Accept = "application/json";
                HttpReq.ContentType = "application/json";
                HttpReq.Headers.Add("X-Api-Version", "2.0");
                HttpReq.Headers.Add("Authorization", "Bearer " + AccKey);
                HttpReq.ContentLength =PostDataBytes.Length;
                ReqStream = HttpReq.GetRequestStream();
                ReqStream.Write(PostDataBytes, 0, PostDataBytes.Length);
                ReqStream.Close();

                HttpRes = (HttpWebResponse)HttpReq.GetResponse();
                ResStream = HttpRes.GetResponseStream();
                ResStreamReader = new StreamReader(ResStream);
                ResContent = ResStreamReader.ReadToEnd();

                LogWriter.DebugLog("送信結果 StatusCode:" +HttpRes.StatusCode + " Message:" + ResContent);

            }
            catch (Exception e)
            {
                LogWriter.ErrorLog("TwitCastingのAPI発行中にエラーが発生しました。\n取得先エンドポイント：" + StrURL + "\nエラー内容：\n" + e.Message + "\n" + e.StackTrace);
            }
            finally
            {
                try
                {
                    ResStreamReader.Close();
                }
                catch (Exception)
                {

                }
                try
                {
                    ResStream.Close();
                }
                catch (Exception)
                {

                }
                try
                {
                    HttpRes.Close();
                }
                catch (Exception)
                {

                }
            }
            return ResContent;
        }
    }
}
