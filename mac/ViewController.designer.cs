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
		AppKit.NSTextField Comment1 { get; set; }
		
		void ReleaseDesignerOutlets ()
		{
			if (Comment1 != null) {
				Comment1.Dispose ();
				Comment1 = null;
			}
		}
	}
}
