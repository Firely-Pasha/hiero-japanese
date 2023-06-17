import SwiftUI
import HieroApp

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate

    @Environment(\.scenePhase)
    var scenePhase: ScenePhase

    private var rootHolder: RootHolder { appDelegate.getRootHolder() }
    
    @State var selection = 0;

    var body: some Scene {
        WindowGroup {
            RootNavigation(rootHolder.root.navigator)
        }
    }
    
}

class AppDelegate: NSObject, UIApplicationDelegate {
    private var rootHolder: RootHolder?
    
    fileprivate func getRootHolder() -> RootHolder {
        if (rootHolder == nil) {
            rootHolder = RootHolder()
        }
        return rootHolder!
    }
}

private class RootHolder : ObservableObject {
    let lifecycle: LifecycleRegistry
    let root: ApplicationComponent

    init() {
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        AppInitializerKt.doInitApplication()
        root = ApplicationDefaultComponent(
            componentContext: DefaultComponentContext(lifecycle: lifecycle),
            storeFactory: DefaultStoreFactory()
        )
        LifecycleRegistryExtKt.create(lifecycle)
    }

    deinit {
        // Destroy the root component before it is deallocated
        LifecycleRegistryExtKt.destroy(lifecycle)
    }
}

