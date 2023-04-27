//
//  StackNavigator.swift
//  iosApp
//
//  Created by Pavel on 26.04.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import HieroUi

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
            getTitle: {_ in "SAD"},
            onBack: {
                component.navigateBack()
            },
            childContent: { child in
                switch child {
                case let child as StackNavigationComponentChildHiragana:
                    CollectionScreen(component: child.component)
                case let child as StackNavigationComponentChildKatakana:
                    CollectionScreen(component: child.component)
                case let child as StackNavigationComponentChildSettings: Text("SETTINGS")
                default: EmptyView()
                }

            }
        )
    }

}
