using System;

using AppKit;
using Foundation;

namespace MameCome
{
    public partial class ViewController : NSViewController
    {
        private MacUIControl macUIControl;

        public ViewController(IntPtr handle) : base(handle)
        {
        }

        public override void ViewDidLoad()
        {
            // Do any additional setup after loading the view.
            base.ViewDidLoad();
            macUIControl = new MacUIControl();
            macUIControl.createMainWindow(MainBoard);

        }

        public override NSObject RepresentedObject
        {
            get
            {
                return base.RepresentedObject;
            }
            set
            {
                base.RepresentedObject = value;
                // Update the view, if already loaded.
            }
        }

        partial void FlowButton(AppKit.NSButton sender){
            macUIControl.flowMessage();

        }

        public void controlWindow(){

        }
    }
}
