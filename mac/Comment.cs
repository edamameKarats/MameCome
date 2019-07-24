//MAC固有ロジック
using System;
namespace MameComment
{
    public class Comment
    {
        #region Computed Propoperties
        public string Timestamp { get; set; } = "";
        public string IconURL { get; set; } = "";
        public string UserName { get; set; } = "";
        public string CommentString { get; set; } = "";
        #endregion

        #region Constructors
        public Comment()
        {
        }

        public Comment(string Timestamp, string IconURL, string UserName, string CommentString)
        {
            this.Timestamp = Timestamp;
            this.IconURL = IconURL;
            this.UserName = UserName;
            this.CommentString = CommentString;
        }
        #endregion
    }
}
