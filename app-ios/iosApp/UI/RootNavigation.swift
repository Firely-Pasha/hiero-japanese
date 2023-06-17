//
//  RootNavigation.swift
//  iosApp
//
//  Created by Pavel on 15.06.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import HieroApp


struct RootNavigation : View {
    
    private let component: StackNavigationComponent
    
    @ObservedObject
    private var childStack: ObservableValue<ChildStack<AnyObject, StackNavigationComponentChild>>
    private var root: StackNavigationComponentChild { childStack.value.items[0].instance }
    private var nextItems: [ViewSpec] {
        childStack.value.items[1..<childStack.value.items.count]
            .map { $0.instance }
            .map {
                if let nav = $0 as? StackNavigationComponentChildSection {
                    return ViewSpec.section
                }
                return ViewSpec.unknown
            }
    }
    private var activeChild: StackNavigationComponentChild { childStack.value.active.instance }
    
    init(_ component: StackNavigationComponent) {
        self.component = component
        childStack = ObservableValue(component.childStack)
    }

    
    var body : some View {
        NavigationStack(path: Binding(
            get: {nextItems},
            set: {items, _ in
                if (items.count < nextItems.count) {
                    component.navigateBack()
                }
            }
        )) {
            MainScreen((root as! StackNavigationComponentChildMain).component)
                .navigationDestination(for: ViewSpec.self) { spec in
                    if (spec == ViewSpec.section) {
                        Text("SECTION")
                    } else {
                        EmptyView()
                    }
                }
        }

    }
    
}

enum ViewSpec: Equatable, Hashable {
    case section
    case unknown
}
