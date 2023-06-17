//
//  MainView.swift
//  iosApp
//
//  Created by Pavel on 25.04.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import HieroApp

struct MainScreen : View {
    
    private let component: MainComponent

    @ObservedObject
    private var state: ObservableValue<MainComponentModel>
    
    @State var selection: Int = 0
        
    init(_ component: MainComponent) {
        self.component = component
        state = ObservableValue(component.state)
        selection = Int(state.value.tab)
    }
    
    var body: some View {
        TabView(selection: $selection) {
            StackNavigator(component.hiraganaTab.component)
                .tabItem {
                    TextImage(text: "ひ")
                    Text("Hiragana")
                }
                .tag(0)
            StackNavigator(component.katakanaTab.component)
                .tabItem {
                    TextImage(text: "カ")
                    Text("Katakana")
                }
                .tag(1)
            StackNavigator(component.settingsTab.component)
                .tabItem {
                    Label {
                        Text("Settings")
                    } icon: {
                        Image(systemName: "gear")
                    }
                }
                .tag(2)
        }
        .onChange(of: selection) { value in
            component.changeTab(index: Int32(selection))
        }
    }
    
}

struct TextImage : View {
    
    let text: String
    
    @Environment(\.displayScale) private var displayScale
    
    var body: some View {
        let renderer = ImageRenderer(
            content: Text(text)
                .font(.system(
                    size: 24,
                    weight: Font.Weight.regular
                ))
        )
        let _ = renderer.scale = displayScale
        if let image = renderer.uiImage {
            Image(uiImage: image)
        }
    }
    
}
