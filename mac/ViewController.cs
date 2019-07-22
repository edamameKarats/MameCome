using System;

using AppKit;
using CoreGraphics;
using CoreImage;
using Foundation;

namespace MameCome
{
    public partial class ViewController : NSViewController
    {
        private NSWindow window;

        public ViewController(NSWindow window)
        {
            this.window = window;
        }
        public ViewController(IntPtr handle) : base(handle)
        {
        }

        public override void LoadView()
        {
            this.View = new NSView(window.Frame);
            window.ContentView = this.View;
            window.IsVisible = true;
        }


        public override void ViewDidLoad()
        {

            base.ViewDidLoad();
            Console.WriteLine("ViewController start.");
            // Do any additional setup after loading the view.
            Console.WriteLine("ViewController end.");
            NSFontManager fm = new NSFontManager();
            Console.WriteLine("Available Font count is " + fm.AvailableFontFamilies);
            for (int i = 0; i < fm.AvailableFontFamilies.Length; i++)
            {
                Console.WriteLine(fm.AvailableFontFamilies[i]);
            }
            Console.WriteLine(ColorList.GetColor("AliceBlue").Components.GetValue(1));
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
    }
}
