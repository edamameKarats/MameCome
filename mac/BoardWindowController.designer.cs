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
	[Register ("BoardWindowController")]
	partial class BoardWindowController
	{
		[Outlet]
		AppKit.NSWindow BoardWindowArea { get; set; }
		
		void ReleaseDesignerOutlets ()
		{
			if (BoardWindowArea != null) {
				BoardWindowArea.Dispose ();
				BoardWindowArea = null;
			}
		}
	}

}
