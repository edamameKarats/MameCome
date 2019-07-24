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
	[Register ("SettingViewController")]
	partial class SettingViewController
	{
		[Outlet]
		AppKit.NSTextField ValueAccUrl { get; set; }

		[Outlet]
		AppKit.NSComboBox ValueBoardBgColorBox { get; set; }

		[Outlet]
		AppKit.NSComboBox ValueBoardFontBox { get; set; }

		[Outlet]
		AppKit.NSComboBox ValueBoardFontColorBox { get; set; }

		[Outlet]
		AppKit.NSTextField ValueBoardHeight { get; set; }

		[Outlet]
		AppKit.NSTextField ValueBoardLine { get; set; }

		[Outlet]
		AppKit.NSTextField ValueBoardWidth { get; set; }

		[Outlet]
		AppKit.NSTextField ValueBoardX { get; set; }

		[Outlet]
		AppKit.NSTextField ValueBoardY { get; set; }

		[Outlet]
		AppKit.NSButton ValueLogFlg { get; set; }

		[Outlet]
		AppKit.NSTextField ValueLogPath { get; set; }

		[Outlet]
		AppKit.NSComboBox ValueViewerBgColorBox { get; set; }

		[Outlet]
		AppKit.NSComboBox ValueViewerFontBox { get; set; }

		[Outlet]
		AppKit.NSComboBox ValueViewerFontColorBox { get; set; }

		[Outlet]
		AppKit.NSTextField ValueViewerFontSize { get; set; }

		[Outlet]
		AppKit.NSTextField ValueViewerHeight { get; set; }

		[Outlet]
		AppKit.NSTextField ValueViewerWidth { get; set; }

		[Outlet]
		AppKit.NSTextField ValueViewerX { get; set; }

		[Outlet]
		AppKit.NSTextField ValueViewerY { get; set; }

		[Action ("RegistBtn:")]
		partial void RegistBtn (Foundation.NSObject sender);

		[Action ("RegisterBtn:")]
		partial void RegisterBtn (AppKit.NSButton sender);

		[Action ("ValueCancelBtn:")]
		partial void ValueCancelBtn (Foundation.NSObject sender);

		[Action ("ValueSaveBtn:")]
		partial void ValueSaveBtn (Foundation.NSObject sender);
		
		void ReleaseDesignerOutlets ()
		{
			if (ValueAccUrl != null) {
				ValueAccUrl.Dispose ();
				ValueAccUrl = null;
			}

			if (ValueBoardBgColorBox != null) {
				ValueBoardBgColorBox.Dispose ();
				ValueBoardBgColorBox = null;
			}

			if (ValueBoardFontBox != null) {
				ValueBoardFontBox.Dispose ();
				ValueBoardFontBox = null;
			}

			if (ValueBoardFontColorBox != null) {
				ValueBoardFontColorBox.Dispose ();
				ValueBoardFontColorBox = null;
			}

			if (ValueBoardHeight != null) {
				ValueBoardHeight.Dispose ();
				ValueBoardHeight = null;
			}

			if (ValueBoardLine != null) {
				ValueBoardLine.Dispose ();
				ValueBoardLine = null;
			}

			if (ValueBoardWidth != null) {
				ValueBoardWidth.Dispose ();
				ValueBoardWidth = null;
			}

			if (ValueBoardX != null) {
				ValueBoardX.Dispose ();
				ValueBoardX = null;
			}

			if (ValueBoardY != null) {
				ValueBoardY.Dispose ();
				ValueBoardY = null;
			}

			if (ValueLogFlg != null) {
				ValueLogFlg.Dispose ();
				ValueLogFlg = null;
			}

			if (ValueLogPath != null) {
				ValueLogPath.Dispose ();
				ValueLogPath = null;
			}

			if (ValueViewerBgColorBox != null) {
				ValueViewerBgColorBox.Dispose ();
				ValueViewerBgColorBox = null;
			}

			if (ValueViewerFontBox != null) {
				ValueViewerFontBox.Dispose ();
				ValueViewerFontBox = null;
			}

			if (ValueViewerFontColorBox != null) {
				ValueViewerFontColorBox.Dispose ();
				ValueViewerFontColorBox = null;
			}

			if (ValueViewerFontSize != null) {
				ValueViewerFontSize.Dispose ();
				ValueViewerFontSize = null;
			}

			if (ValueViewerHeight != null) {
				ValueViewerHeight.Dispose ();
				ValueViewerHeight = null;
			}

			if (ValueViewerWidth != null) {
				ValueViewerWidth.Dispose ();
				ValueViewerWidth = null;
			}

			if (ValueViewerX != null) {
				ValueViewerX.Dispose ();
				ValueViewerX = null;
			}

			if (ValueViewerY != null) {
				ValueViewerY.Dispose ();
				ValueViewerY = null;
			}
		}
	}
}
