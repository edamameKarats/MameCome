// WARNING
//
// This file has been generated automatically by Visual Studio to store outlets and
// actions made in the UI designer. If it is removed, they will be lost.
// Manual changes to this file may not be handled correctly.
//
using Foundation;
using System.CodeDom.Compiler;

namespace MameComment
{
	[Register ("ViewerViewController")]
	partial class ViewerViewController
	{
		[Outlet]
		AppKit.NSScrollView Comment { get; set; }

		[Outlet]
		AppKit.NSScrollView CommentScrollView { get; set; }

		[Outlet]
        CommentTableObject CommentTable { get; set; }

		[Outlet]
		AppKit.NSScrollView Icon { get; set; }

		[Outlet]
		AppKit.NSTextField SendCommentArea { get; set; }

		[Outlet]
		AppKit.NSButton SendCommentBtn { get; set; }

		[Outlet]
		AppKit.NSScrollView TimeStamp { get; set; }

		[Outlet]
		AppKit.NSScrollView UserName { get; set; }

		[Action ("SendComment:")]
		partial void SendComment (Foundation.NSObject sender);
		
		void ReleaseDesignerOutlets ()
		{
			if (Comment != null) {
				Comment.Dispose ();
				Comment = null;
			}

			if (CommentScrollView != null) {
				CommentScrollView.Dispose ();
				CommentScrollView = null;
			}

			if (CommentTable != null) {
				CommentTable.Dispose ();
				CommentTable = null;
			}

			if (Icon != null) {
				Icon.Dispose ();
				Icon = null;
			}

			if (SendCommentArea != null) {
				SendCommentArea.Dispose ();
				SendCommentArea = null;
			}

			if (SendCommentBtn != null) {
				SendCommentBtn.Dispose ();
				SendCommentBtn = null;
			}

			if (TimeStamp != null) {
				TimeStamp.Dispose ();
				TimeStamp = null;
			}

			if (UserName != null) {
				UserName.Dispose ();
				UserName = null;
			}
		}
	}
}
