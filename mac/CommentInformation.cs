//Windows,Mac共通ロジック
using System;
namespace MameComment
{
    public class CommentInformation
    {
        public CommentInformation()
        {
        }
        //Returns [UTC UserId UserName UserImage Message]
        public String Timestamp { get; set; }
        public String UserId { get; set; }
        public String UserName { get; set; }
        public String UserImage { get; set; }
        public String CommentId { get; set; }
        public String Message { get; set; }
    }
}
