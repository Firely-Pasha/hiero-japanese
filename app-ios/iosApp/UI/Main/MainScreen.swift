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
    
    @State private var selection: Int32 = 0
    
    @ObservedObject
    private var childStack: ObservableValue<ChildStack<AnyObject, MainComponentChild>>
    private var activeChild: MainComponentChild { childStack.value.active.instance }
    
    private let bottomNavItems = [
        TabItem(item: 0, title: "Homes", image: "house"),
        TabItem(item: 1, title: "Access", image: "list.bullet"),
        TabItem(item: 2, title: "Calendar", image: "gear"),
    ]
    
    
    init(_ component: MainComponent) {
        self.component = component
        childStack = ObservableValue(component.childStack)
    }
    
    var body: some View {
        TabView(selection: $selection) {
            ForEach(bottomNavItems) { item in
                let child = findChildFromTab(tab: item.item)
                if (child != nil) {
                    ChildView(child: child!).tabItem {
                        Image(systemName: item.image)
                        Text(item.title)
                    }.tag(item.item)
                }
            }
        }
        .onChange(of: selection) { newValue in
            DispatchQueue.main.async {
                component.changeTab(index: newValue)
            }
        }
    }
    
    private func findChildFromTab(tab: Int32) -> MainComponentChild? {
        return childStack.value.items.first(where: {
            let instance = $0.instance
            switch tab {
            case 0:
                return instance is MainComponentChildHiragana
            case 1:
                return instance is MainComponentChildKatakana
            case 2:
                return instance is MainComponentChildSettings
            default:
                fatalError()
            }
        })?.instance
    }
    
}

struct TabItem: Identifiable {
    var id = UUID()
    var item: Int32
    var title: String
    var image: String
}

private struct ChildView: View {
    let child: MainComponentChild
    
    var body: some View {
        switch child {
        case let child as MainComponentChildHiragana:
            StackNavigator(child.component)
        case let child as MainComponentChildKatakana:
            StackNavigator(child.component)
        case let child as MainComponentChildSettings:
            StackNavigator(child.component)
        default: EmptyView()
        }
    }
}

private struct VerticalLabelStyle: LabelStyle {
    func makeBody(configuration: Configuration) -> some View {
        VStack(alignment: .center, spacing: 8) {
            configuration.icon
            configuration.title
        }
    }
}
