//
//  StackNavigator.swift
//  iosApp
//
//  Created by Pavel on 26.04.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import HieroApp

struct StackNavigator : View {
    
    private let component: StackNavigationComponent
    
    @ObservedObject
    private var childStack: ObservableValue<ChildStack<AnyObject, StackNavigationComponentChild>>
    private var activeChild: StackNavigationComponentChild { childStack.value.active.instance }

    init(_ component: StackNavigationComponent) {
        self.component = component
        childStack = ObservableValue(component.childStack)
    }
    
    var body: some View {
        StackView(
            stackValue: childStack,
            getTitle: { child in
                switch child {
                case let child as StackNavigationComponentChildCollection:
                    return "Collection"
                case let child as StackNavigationComponentChildSettings:
                    return "Settings"
                case let child as StackNavigationComponentChildSection:
                    return "Section"
                default:
                    return nil
                }
            },
            onBack: {
                component.navigateBack()
            },
            childContent: { child in
                switch child {
                case let child as StackNavigationComponentChildCollection:
                    CollectionScreen(component: child.component)
                case let child as StackNavigationComponentChildMain:
                    MainScreen(child.component)
                case let child as StackNavigationComponentChildSettings: Text("SETTINGS")
                default: EmptyView()
                }
            }
        )
    }

}
