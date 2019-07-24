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
	[Register ("MainViewController")]
	partial class MainViewController
    {
        [Outlet]
        AppKit.NSButtonCell CtrlGetBtn { get; set; }

        [Outlet]
        AppKit.NSTextField IdText { get; set; }

        [Action("CtrlBoard:")]
        partial void CtrlBoard(Foundation.NSObject sender);

        [Action("CtrlGet:")]
        partial void CtrlGet(Foundation.NSObject sender);

        [Action("CtrlSetting:")]
        partial void CtrlSetting(Foundation.NSObject sender);

        [Action("CtrlViewer:")]
        partial void CtrlViewer(Foundation.NSObject sender);

        void ReleaseDesignerOutlets()
        {
            if (IdText != null)
            {
                IdText.Dispose();
                IdText = null;
            }

            if (CtrlGetBtn != null)
            {
                CtrlGetBtn.Dispose();
                CtrlGetBtn = null;
            }
        }
    }
}