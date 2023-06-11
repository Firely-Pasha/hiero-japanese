import SwiftUI
import HieroApp

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate

    @Environment(\.scenePhase)
    var scenePhase: ScenePhase

    private var rootHolder: RootHolder { appDelegate.getRootHolder() }

    var body: some Scene {
        WindowGroup {
            StackNavigator(rootHolder.root.navigator)
                .onChange(of: scenePhase) { newPhase in
                    switch newPhase {
                    case .background: LifecycleRegistryExtKt.stop(rootHolder.lifecycle)
                    case .inactive: LifecycleRegistryExtKt.pause(rootHolder.lifecycle)
                    case .active: LifecycleRegistryExtKt.resume(rootHolder.lifecycle)
                    @unknown default: break
                    }
                }
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
