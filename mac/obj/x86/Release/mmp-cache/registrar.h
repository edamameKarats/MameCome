#pragma clang diagnostic ignored "-Wdeprecated-declarations"
#pragma clang diagnostic ignored "-Wtypedef-redefinition"
#pragma clang diagnostic ignored "-Wobjc-designated-initializers"
#include <stdarg.h>
#include <objc/objc.h>
#include <objc/runtime.h>
#include <objc/message.h>
#import <Foundation/Foundation.h>
#import <AppKit/AppKit.h>
#import <QuartzCore/QuartzCore.h>
#import <CloudKit/CloudKit.h>
#import <CoreGraphics/CoreGraphics.h>

@class NSApplicationDelegate;
@protocol NSMenuValidation;
@class NSTableViewDataSource;
@class NSTableViewDelegate;
@class __monomac_internal_ActionDispatcher;
@class __MonoMac_NSAlertDidEndDispatcher;
@class NSURLSessionDataDelegate;
@class Foundation_InternalNSNotificationHandler;
@class Foundation_NSDispatcher;
@class __MonoMac_NSActionDispatcher;
@class __MonoMac_NSSynchronizationContextDispatcher;
@class __Xamarin_NSTimerActionDispatcher;
@class Foundation_NSAsyncDispatcher;
@class __MonoMac_NSAsyncActionDispatcher;
@class __MonoMac_NSAsyncSynchronizationContextDispatcher;
@class OpenTK_Platform_MacOS_MonoMacGameView;
@class __NSGestureRecognizerToken;
@class __NSClickGestureRecognizer;
@class __NSGestureRecognizerParameterlessToken;
@class __NSGestureRecognizerParametrizedToken;
@class __NSMagnificationGestureRecognizer;
@class __NSPanGestureRecognizer;
@class __NSPressGestureRecognizer;
@class __NSRotationGestureRecognizer;
@class AppKit_NSTableView__NSTableViewDelegate;
@class AppKit_NSWindow__NSWindowDelegate;
@class Foundation_NSUrlSessionHandler_WrappedNSInputStream;
@class __NSObject_Disposer;
@class Foundation_NSUrlSessionHandler_NSUrlSessionHandlerDelegate;
@class AppDelegate;
@class MainWindowController;
@class SettingWindowController;
@class SettingViewController;
@class BoardWindowController;
@class BoardViewController;
@class MameComment_MCLabel;
@class BoardCommentView;
@class ViewerWindowController;
@class ViewerViewController;
@class MameComment_CommentTableDataSource;
@class MameComment_CommentTableDelegate;
@class CommentTableObject;
@class MainViewController;

@interface NSApplicationDelegate : NSObject<NSApplicationDelegate> {
}
	-(id) init;
@end

@protocol NSMenuValidation
	@required -(BOOL) validateMenuItem:(NSMenuItem *)p0;
@end

@interface NSTableViewDataSource : NSObject<NSTableViewDataSource> {
}
	-(id) init;
@end

@interface NSTableViewDelegate : NSObject<NSTableViewDelegate> {
}
	-(id) init;
@end

@interface NSURLSessionDataDelegate : NSObject<NSURLSessionDataDelegate, NSURLSessionTaskDelegate, NSURLSessionDelegate> {
}
	-(id) init;
@end

@interface OpenTK_Platform_MacOS_MonoMacGameView : NSView {
}
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(void) drawRect:(CGRect)p0;
	-(void) lockFocus;
	-(BOOL) conformsToProtocol:(void *)p0;
	-(id) initWithFrame:(CGRect)p0;
	-(id) initWithCoder:(NSCoder *)p0;
@end

@interface __NSGestureRecognizerToken : NSObject {
}
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface __NSGestureRecognizerParameterlessToken : __NSGestureRecognizerToken {
}
	-(void) target;
@end

@interface __NSGestureRecognizerParametrizedToken : __NSGestureRecognizerToken {
}
	-(void) target:(NSGestureRecognizer *)p0;
@end

@interface AppDelegate : NSObject<NSApplicationDelegate> {
}
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(void) applicationDidFinishLaunching:(NSNotification *)p0;
	-(void) applicationWillTerminate:(NSNotification *)p0;
	-(BOOL) conformsToProtocol:(void *)p0;
	-(id) init;
@end

@interface MainWindowController : NSWindowController {
}
	@property (nonatomic, assign) NSWindow * MainWindowArea;
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(NSWindow *) MainWindowArea;
	-(void) setMainWindowArea:(NSWindow *)p0;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface SettingWindowController : NSWindowController {
}
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface SettingViewController : NSViewController {
}
	@property (nonatomic, assign) NSTextField * ValueAccUrl;
	@property (nonatomic, assign) NSComboBox * ValueBoardBgColorBox;
	@property (nonatomic, assign) NSComboBox * ValueBoardFontBox;
	@property (nonatomic, assign) NSComboBox * ValueBoardFontColorBox;
	@property (nonatomic, assign) NSTextField * ValueBoardHeight;
	@property (nonatomic, assign) NSTextField * ValueBoardLine;
	@property (nonatomic, assign) NSTextField * ValueBoardWidth;
	@property (nonatomic, assign) NSTextField * ValueBoardX;
	@property (nonatomic, assign) NSTextField * ValueBoardY;
	@property (nonatomic, assign) NSButton * ValueLogFlg;
	@property (nonatomic, assign) NSTextField * ValueLogPath;
	@property (nonatomic, assign) NSComboBox * ValueViewerBgColorBox;
	@property (nonatomic, assign) NSComboBox * ValueViewerFontBox;
	@property (nonatomic, assign) NSComboBox * ValueViewerFontColorBox;
	@property (nonatomic, assign) NSTextField * ValueViewerFontSize;
	@property (nonatomic, assign) NSTextField * ValueViewerHeight;
	@property (nonatomic, assign) NSTextField * ValueViewerWidth;
	@property (nonatomic, assign) NSTextField * ValueViewerX;
	@property (nonatomic, assign) NSTextField * ValueViewerY;
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(NSTextField *) ValueAccUrl;
	-(void) setValueAccUrl:(NSTextField *)p0;
	-(NSComboBox *) ValueBoardBgColorBox;
	-(void) setValueBoardBgColorBox:(NSComboBox *)p0;
	-(NSComboBox *) ValueBoardFontBox;
	-(void) setValueBoardFontBox:(NSComboBox *)p0;
	-(NSComboBox *) ValueBoardFontColorBox;
	-(void) setValueBoardFontColorBox:(NSComboBox *)p0;
	-(NSTextField *) ValueBoardHeight;
	-(void) setValueBoardHeight:(NSTextField *)p0;
	-(NSTextField *) ValueBoardLine;
	-(void) setValueBoardLine:(NSTextField *)p0;
	-(NSTextField *) ValueBoardWidth;
	-(void) setValueBoardWidth:(NSTextField *)p0;
	-(NSTextField *) ValueBoardX;
	-(void) setValueBoardX:(NSTextField *)p0;
	-(NSTextField *) ValueBoardY;
	-(void) setValueBoardY:(NSTextField *)p0;
	-(NSButton *) ValueLogFlg;
	-(void) setValueLogFlg:(NSButton *)p0;
	-(NSTextField *) ValueLogPath;
	-(void) setValueLogPath:(NSTextField *)p0;
	-(NSComboBox *) ValueViewerBgColorBox;
	-(void) setValueViewerBgColorBox:(NSComboBox *)p0;
	-(NSComboBox *) ValueViewerFontBox;
	-(void) setValueViewerFontBox:(NSComboBox *)p0;
	-(NSComboBox *) ValueViewerFontColorBox;
	-(void) setValueViewerFontColorBox:(NSComboBox *)p0;
	-(NSTextField *) ValueViewerFontSize;
	-(void) setValueViewerFontSize:(NSTextField *)p0;
	-(NSTextField *) ValueViewerHeight;
	-(void) setValueViewerHeight:(NSTextField *)p0;
	-(NSTextField *) ValueViewerWidth;
	-(void) setValueViewerWidth:(NSTextField *)p0;
	-(NSTextField *) ValueViewerX;
	-(void) setValueViewerX:(NSTextField *)p0;
	-(NSTextField *) ValueViewerY;
	-(void) setValueViewerY:(NSTextField *)p0;
	-(void) viewDidLoad;
	-(void) viewWillAppear;
	-(void) viewWillDisappear;
	-(void) ValueCancelBtn:(NSObject *)p0;
	-(void) ValueSaveBtn:(NSObject *)p0;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface BoardWindowController : NSWindowController {
}
	@property (nonatomic, assign) NSWindow * BoardWindowArea;
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(NSWindow *) BoardWindowArea;
	-(void) setBoardWindowArea:(NSWindow *)p0;
	-(void) windowDidLoad;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface BoardViewController : NSViewController {
}
	@property (nonatomic, assign) id CommentArea;
	@property (nonatomic, assign) NSButton * ControlAlphaBtn;
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(id) CommentArea;
	-(void) setCommentArea:(id)p0;
	-(NSButton *) ControlAlphaBtn;
	-(void) setControlAlphaBtn:(NSButton *)p0;
	-(void) viewDidLoad;
	-(void) viewDidAppear;
	-(void) ControlAlpha:(NSObject *)p0;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface MameComment_MCLabel : NSTextField {
}
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(BOOL) conformsToProtocol:(void *)p0;
	-(id) init;
@end

@interface BoardCommentView : NSView {
}
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface ViewerWindowController : NSWindowController {
}
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(void) windowDidLoad;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface ViewerViewController : NSViewController {
}
	@property (nonatomic, assign) NSScrollView * Comment;
	@property (nonatomic, assign) NSScrollView * CommentScrollView;
	@property (nonatomic, assign) id CommentTable;
	@property (nonatomic, assign) NSScrollView * Icon;
	@property (nonatomic, assign) NSTextField * SendCommentArea;
	@property (nonatomic, assign) NSButton * SendCommentBtn;
	@property (nonatomic, assign) NSScrollView * TimeStamp;
	@property (nonatomic, assign) NSScrollView * UserName;
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(NSScrollView *) Comment;
	-(void) setComment:(NSScrollView *)p0;
	-(NSScrollView *) CommentScrollView;
	-(void) setCommentScrollView:(NSScrollView *)p0;
	-(id) CommentTable;
	-(void) setCommentTable:(id)p0;
	-(NSScrollView *) Icon;
	-(void) setIcon:(NSScrollView *)p0;
	-(NSTextField *) SendCommentArea;
	-(void) setSendCommentArea:(NSTextField *)p0;
	-(NSButton *) SendCommentBtn;
	-(void) setSendCommentBtn:(NSButton *)p0;
	-(NSScrollView *) TimeStamp;
	-(void) setTimeStamp:(NSScrollView *)p0;
	-(NSScrollView *) UserName;
	-(void) setUserName:(NSScrollView *)p0;
	-(void) viewDidLoad;
	-(void) viewWillAppear;
	-(void) SendComment:(NSObject *)p0;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface MameComment_CommentTableDataSource : NSObject<NSTableViewDataSource> {
}
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(NSInteger) numberOfRowsInTableView:(NSTableView *)p0;
	-(BOOL) conformsToProtocol:(void *)p0;
	-(id) init;
@end

@interface MameComment_CommentTableDelegate : NSObject<NSTableViewDelegate> {
}
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(NSView *) tableView:(NSTableView *)p0 viewForTableColumn:(NSTableColumn *)p1 row:(NSInteger)p2;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface CommentTableObject : NSTableView {
}
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(BOOL) conformsToProtocol:(void *)p0;
@end

@interface MainViewController : NSViewController {
}
	@property (nonatomic, assign) NSButtonCell * CtrlGetBtn;
	@property (nonatomic, assign) NSTextField * IdText;
	-(void) release;
	-(id) retain;
	-(int) xamarinGetGCHandle;
	-(void) xamarinSetGCHandle: (int) gchandle;
	-(NSButtonCell *) CtrlGetBtn;
	-(void) setCtrlGetBtn:(NSButtonCell *)p0;
	-(NSTextField *) IdText;
	-(void) setIdText:(NSTextField *)p0;
	-(void) viewDidLoad;
	-(NSObject *) representedObject;
	-(void) setRepresentedObject:(NSObject *)p0;
	-(void) viewWillDisappear;
	-(void) CtrlBoard:(NSObject *)p0;
	-(void) CtrlGet:(NSObject *)p0;
	-(void) CtrlSetting:(NSObject *)p0;
	-(void) CtrlViewer:(NSObject *)p0;
	-(BOOL) conformsToProtocol:(void *)p0;
@end


