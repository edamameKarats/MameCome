//Windows,Mac共通ファイル
//差分についてはCompile時のフラグで切り替えてもいいが、Code中は頭でDEFINEしてしまう
#define MACOS
//#define WINDOWS

using System;
using System.Threading;
#if WINDOWS
using System.Windows.Forms;
#endif

namespace MameComment
{
    public class GetComment
    {
#if MACOS
        public MainViewController Vc;
        public GetComment(MainViewController Vc)
        {
            this.Vc = Vc;
        }
#elif WINDOWS
        public MainWindow Vc;
        public GetComment(MainWindow Vc)
        {
            this.Vc = Vc;
        }
#endif

        public void GetMessage(CancellationToken CancelToken, String Id)
        {
            String MovieId;
            CommentInformation[] CInfo = null;
            String CurrentId = "1";

            LogWriter.DebugLog("コメント取得スレッドを開始します。");
            //TODO Debug時は以下の条件を変更
            if (TwitcastingHTTPApi.Islive(Id, Vc.Mcs.GetAccKey()))
            {
                LogWriter.DebugLog("ユーザーID:" + Id + " は現在放送中です。");
                MovieId = TwitcastingHTTPApi.GetMovieId(Id, Vc.Mcs.GetAccKey());
                //MovieIdがうまく取得でき無ければ何もしない
                if (MovieId == null || MovieId.Equals(""))
                {
                    LogWriter.DebugLog("放送IDの取得に失敗しました。");
                    //TODO ユーザーにメッセージを出してあげる？
                }
                else
                {
                    LogWriter.DebugLog("放送ID:" + MovieId + "。");
                    //メイン側のMovieIdをUpdateしてあげる
                    Vc.UpdateMovieId(MovieId);
                    //初期イメージの取得
                    LogWriter.DebugLog("すでにコメントされたメッセージを最大50件取得します。");
                    //TODO Debug時は以下をコメントアウト
                    CInfo = TwitcastingHTTPApi.GetComment(MovieId, Vc.Mcs.GetAccKey());
                    //コメントありの場合、CurrentIdを更新する
                    if (CInfo != null && CInfo.Length != 0)
                    {
                        CurrentId = CInfo[0].CommentId;
                    }

                    while (true)
                    {
                        //続いている限りはボタンを取得停止に変更する
#if MACOS
                        new Foundation.NSObject().InvokeOnMainThread(() =>
                      {
                           Vc.ChangeGetBtnView(true);
                       });
#elif WINDOWS
                        Vc.Dispatcher.BeginInvoke(
                            new Action(() =>
                            {
                                Vc.ChangeGetBtnView(true);
                            })
                        );
#endif
                        LogWriter.DebugLog("ID:" + CurrentId + "以降のメッセージを取得します。");
                        CInfo = TwitcastingHTTPApi.GetComment(MovieId, CurrentId, Vc.Mcs.GetAccKey());
                        if (CInfo != null && CInfo.Length > 0)
                        {
                            LogWriter.DebugLog(CInfo.Length + "件のメッセージを取得しました。");
                            CurrentId = CInfo[0].CommentId;
                            for (int i = CInfo.Length - 1; i >= 0; i--)
                            {
                                LogWriter.DebugLog(CInfo[i].Timestamp + " " + CInfo[i].CommentId + " " + CInfo[i].UserId + " " + CInfo[i].UserImage + " " + CInfo[i].UserName + " " + CInfo[i].Message);
                                //メッセージを流すようにリクエスト
                                //Board、Viewer、ログどれに出力するかはMainViewController側のスレッドで判定する
#if MACOS
                                new Foundation.NSObject().InvokeOnMainThread(() =>
                                {
                                    Vc.SendMessage(CInfo[i]);
                                });
#elif WINDOWS
                                Vc.Dispatcher.BeginInvoke(
                                    new Action(() =>
                                    {
                                        Vc.SendMessage(CInfo[i]);
                                    })
                                );
#endif
                                Thread.Sleep(1000 / 50);
                            }
                        }
                        else
                        {
                            Thread.Sleep(1000 / 50 * CInfo.Length);
                        }
                        Thread.Sleep(1000 - 1000 / 50 * CInfo.Length);

                        //一連のログ出力、Sleepが終わったところでスレッドのキャンセルポイントを作る

                        if (CancelToken.IsCancellationRequested == true)
                        {
                            LogWriter.DebugLog("スレッドの停止が要求されました。");
                            break;
                        }
                    }
                }

            }
            else
            {
                LogWriter.DebugLog("ユーザーID:" + Id + "は現在放送していません。");
                //TODO ユーザーにメッセージを出してあげる？

            }
            //終了時以外で何かしらの理由で止まった場合はボタンの見た目を変える
            if (Vc.EndFlg == 0)
            {
#if MACOS
                new Foundation.NSObject().InvokeOnMainThread(() =>
                {
                    Vc.ChangeGetBtnView(false);
                });
#elif WINDOWS
                Vc.Dispatcher.BeginInvoke(
                    new Action(()=>
                    {
                        Vc.ChangeGetBtnView(false);
                    })
                );
#endif
            }

        }

    }
}
