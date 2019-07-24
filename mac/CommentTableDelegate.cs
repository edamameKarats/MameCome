//MAC固有ロジック
using System;
using AppKit;

namespace MameComment
{
    public class CommentTableDelegate : NSTableViewDelegate
    {
        #region Constants 
        private const string CellIdentifier = "CommentCell";
        #endregion

        #region Private Variables
        private CommentTableDataSource DataSource;
        #endregion

        #region Constructors
        public CommentTableDelegate(CommentTableDataSource Datasource)
        {
            this.DataSource = Datasource;
        }
        #endregion

        #region Override Methods
        public override NSView GetViewForItem(NSTableView tableView, NSTableColumn tableColumn, nint row)
        {
            // This pattern allows you reuse existing views when they are no-longer in use.
            // If the returned view is null, you instance up a new view
            // If a non-null view is returned, you modify it enough to reflect the new data
            NSTextField view = (NSTextField)tableView.MakeView(CellIdentifier, this);
            if (view == null) //最初しか通らない
            {
                view = new NSTextField();
                view.Identifier = CellIdentifier;
                view.BackgroundColor = NSColor.Clear;
                view.Bordered = false;
                view.Selectable = true;
                view.Editable = false;
            }
            //view共通の値設定
            view.Font = ((CommentTableObject)tableView).TableFont;
            view.TextColor = ((CommentTableObject)tableView).FontColor;

            // Setup view based on the column selected
            switch (tableColumn.Identifier)
            {
                case "Timestamp":
                    view.StringValue = DataSource.Comments[(int)row].Timestamp;
                    break;
                case "IconURL":
                    //画像表示は未実装
                    //info.plistを設定してもHTTPSじゃないと取得できない上に、
                    //うまくキャッシュできないのでどんどん重くなる・・・
                    //ついでにどこかにバグが有るのか、サイズ変更してないからか、画像に何も表示されない
                    //NSUrl imageUrl = new NSUrl("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_120x44dp.png");
                    //NSData imageData = NSData.FromUrl(imageUrl);
                    //NSImageView imageView = new NSImageView();
                    //imageView.Image = new NSImage(imageData);
                    //view.AddSubview(imageView);
                    break;
                case "UserName":
                    view.StringValue = DataSource.Comments[(int)row].UserName;
                    break;
                case "CommentString":
                    view.StringValue = DataSource.Comments[(int)row].CommentString;
                    break;
            }

            return view;
        }
        #endregion
    }
}
