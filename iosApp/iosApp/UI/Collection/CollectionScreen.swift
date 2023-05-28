//
//  CollectionScreen.swift
//  iosApp
//
//  Created by Pavel on 26.04.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import HieroApp

struct CollectionScreen : View {
    
    private let component: CollectionComponent
    
    @ObservedObject
    private var state: ObservableValue<CollectionStoreState>
    
    init(component: CollectionComponent) {
        self.component = component
        self.state = ObservableValue(component.state)
    }

    var body: some View {
        Group {
//            if let loading = state.value as? CollectionStoreStateLoading {
//                VStack{}
//            } else if let content = state.value as? CollectionStoreStateContent {
//                List {
//                    ForEach(content.items.indices) {
//                        let item = content.items[$0]
//                        Text("Item \(item)")
//                            .onTapGesture {
//                                component.navigateToItemDetails()
//                            }
//                    }
//                }
//            } else {
//                Text("ERROR")
//            }
        }.navigationTitle("Collection")
    }
}
