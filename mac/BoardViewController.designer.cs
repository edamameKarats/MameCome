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
	[Register ("BoardViewController")]
	partial class BoardViewController
	{
		[Outlet]
		MameComment.BoardCommentView CommentArea { get; set; }

		[Outlet]
		AppKit.NSButton ControlAlphaBtn { get; set; }

		[Action ("ControlAlpha:")]
		partial void ControlAlpha (Foundation.NSObject sender);
		
		void ReleaseDesignerOutlets ()
		{
			if (CommentArea != null) {
				CommentArea.Dispose ();
				CommentArea = null;
			}

			if (ControlAlphaBtn != null) {
				ControlAlphaBtn.Dispose ();
				ControlAlphaBtn = null;
			}
		}
	}
}
