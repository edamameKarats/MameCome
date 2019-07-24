//MAC固有ロジック
using System;
using AppKit;
using System.Collections.Generic;

namespace MameComment
{
    public class CommentTableDataSource : NSTableViewDataSource
    {
        #region Public Variables
        public List<Comment> Comments = new List<Comment>();
        #endregion

        #region Constructors
        public CommentTableDataSource()
        {
        }
        #endregion

        #region Override Methods
        public override nint GetRowCount(NSTableView tableView)
        {
            return Comments.Count;
        }
        #endregion
    }
}