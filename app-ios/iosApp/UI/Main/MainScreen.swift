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
                    Text("Hiragana")
                }
                .tag(0)
            StackNavigator(component.katakanaTab.component)
                .tabItem {
                    Text("Katakana")
                }
                .tag(1)
            StackNavigator(component.settingsTab.component)
                .tabItem {
                    Text("Settings")
                }
                .tag(2)
        }
        .onChange(of: selection) { value in
            component.changeTab(index: Int32(selection))
        }
    }
    
}
