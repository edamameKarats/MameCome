//Windows,Mac共通ロジックとはいえ、ほぼI/Fのみの共有
//差分についてはCompile時のフラグで切り替えてもいいが、Code中は頭でDEFINEしてしまう
#define MACOS
//#define WINDOWS

using System;
using System.Threading;
#if MACOS
using AppKit;
using CoreAnimation;
#elif WINDOWS
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media.Animation;
using System.Windows.Threading;
#endif

namespace MameComment
{
#if MACOS
    public class MCLabel : NSTextField
#elif WINDOWS
    public class MCLabel : Label
#endif
    {
        private double BoardWidth;//Start point
        public int IsUsed;
#if WINDOWS
        Storyboard sb;
        DoubleAnimationUsingKeyFrames dAnim;
        LinearDoubleKeyFrame frame;
#endif

#if MACOS
        public MCLabel(IntPtr handle) : base(handle)
        {
        }
#endif

        public MCLabel()
        {
#if MACOS
            this.Editable = false;
            this.Bordered = false;
            this.BackgroundColor = NSColor.FromRgba(1,1,1,0);
            this.StringValue = "Label";
#elif WINDOWS
            //WPFの場合、最初から色は透過色
            Content = "Label";
#endif
        }

        public void SetFrame(int Num, int LineNum, double BoardWidth, double BoardHeight)
        {
#if MACOS
            this.StringValue = " ";
            //アニメーションのキャンセルがうまくいかないので、別のアニメーションで上書き
            //終了時のファンクションなども全て生きているので、そのままでIsUsedは０になる
            NSAnimationContext.BeginGrouping();
            //0秒では無理
            NSAnimationContext.CurrentContext.Duration = 0.0000000000001;
            ((MCLabel)Animator).SetFrameOrigin(new CoreGraphics.CGPoint(BoardWidth, BoardHeight - BoardHeight / LineNum * (Num % LineNum + 1)));
            NSAnimationContext.EndGrouping();
            this.BoardWidth = BoardWidth;
            this.SizeToFit();
#elif WINDOWS
            this.Content = "  ";
            //実は同期的に変更していないみたいなので、ラベルの値の変更を待ってから
            Dispatcher.Invoke(new Action(() =>
            {
            }), DispatcherPriority.Loaded);
            this.SetValue(Canvas.LeftProperty, -this.ActualWidth - 1);
            this.SetValue(Canvas.TopProperty, ((BoardHeight - 23) / LineNum * (Num % LineNum)) + 23);
            this.BoardWidth = BoardWidth;
            LogWriter.DebugLog("Label"+Num+"の座標:" + this.GetValue(Canvas.LeftProperty) + ","+ this.GetValue(Canvas.TopProperty));
#endif
        }

        public Boolean MoveFrame(String Message)
        {
            //呼び出されたときに実行中だった場合は何もしない
            if (IsUsed == 1)
            {
                LogWriter.DebugLog("既に実行中のため、アニメーション要求を拒否します。");
                return false;
            }
            LogWriter.DebugLog("アニメーションを実行します　" + Message);

            //呼び出された瞬間、変な場所にいるかも知れないので改めて初期位置に再配置
#if MACOS
            this.SetFrameOrigin(new CoreGraphics.CGPoint(BoardWidth, this.Frame.Y));
#elif WINDOWS
            this.SetValue(Canvas.LeftProperty, BoardWidth);
#endif
            IsUsed = 1;
#if MACOS
            this.StringValue = Message;
            this.SizeToFit();
#elif WINDOWS
            this.Content = Message;
            this.Width = Double.NaN;
            this.Height = Double.NaN;
            //実は同期的に変更していないみたいなので、ラベルの値の変更を待ってからアニメーションを実行する
            Dispatcher.Invoke(new Action(() =>
            {
                this.Width = this.ActualWidth;
            }), DispatcherPriority.Loaded);
#endif

#if MACOS
            //AnimationはMacではAnimationContextを使用する
            NSAnimationContext.BeginGrouping();
            //動く時間を変える
            NSAnimationContext.CurrentContext.Duration = 5.0;
            //動きの設定を変える
            NSAnimationContext.CurrentContext.TimingFunction = CAMediaTimingFunction.FromName(CAMediaTimingFunction.Linear);
            NSAnimationContext.CurrentContext.CompletionHandler += () =>
            {
                //アニメーションが終わったときの処理を記述
                LogWriter.DebugLog("アニメーションを終了します。");
                this.SetFrameOrigin(new CoreGraphics.CGPoint(BoardWidth, this.Frame.Y));
                IsUsed = 0;
            };
            nfloat Origin = 0 - this.Frame.Width;
            //MCLabel Animator = (MCLabel)this.Animator;
            ((MCLabel)Animator).SetFrameOrigin(new CoreGraphics.CGPoint(Origin, this.Frame.Y));
            NSAnimationContext.EndGrouping();
            //ここで非同期にアニメーションが開始されるはず
#elif WINDOWS
            //AnimationはWPFではStoryBoardを使う
            //前の設定が残っていたりすると面倒なので、再作成する
            sb = new Storyboard { RepeatBehavior = new RepeatBehavior(1) };
            dAnim = new DoubleAnimationUsingKeyFrames();
            frame = new LinearDoubleKeyFrame(-this.ActualWidth, TimeSpan.FromSeconds(5));
            sb.Children.Add(dAnim);
            dAnim.KeyFrames.Add(frame);
            Storyboard.SetTargetProperty(dAnim, new PropertyPath("(Canvas.Left)"));
            sb.Completed += (s, e) =>
            {
                //StoryBoardから削除しないとプロパティを変更できなくなる
                sb.Remove(this);
                this.SetValue(Canvas.LeftProperty, BoardWidth);
                IsUsed = 0;
                LogWriter.DebugLog("アニメーションを終了します。" + this.GetValue(Canvas.LeftProperty));
            };
            //ここで非同期にアニメーションが開始されるはず
            sb.Begin(this, true);
#endif
            return true;
        }
    }
}
