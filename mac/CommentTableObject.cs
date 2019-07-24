//MAC固有ロジック
using System;

using Foundation;
using AppKit;
using CoreGraphics;

namespace MameComment
{
	public partial class CommentTableObject : NSTableView
	{
		public CommentTableObject (IntPtr handle) : base (handle)
		{
		}
        public NSFont TableFont;
        public NSColor FontColor;

    }
}
