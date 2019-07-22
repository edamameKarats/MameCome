// WARNING
//
// This file has been generated automatically by Visual Studio to store outlets and
// actions made in the UI designer. If it is removed, they will be lost.
// Manual changes to this file may not be handled correctly.
//
using Foundation;
using System.CodeDom.Compiler;

namespace MameCome
{
	[Register ("ViewController")]
	partial class ViewController
	{
		[Outlet]
		AppKit.NSView MainBoard { get; set; }

		[Action ("FlowButton:")]
		partial void FlowButton (AppKit.NSButton sender);
		
		void ReleaseDesignerOutlets ()
		{
			if (MainBoard != null) {
				MainBoard.Dispose ();
				MainBoard = null;
			}
		}
	}
}
