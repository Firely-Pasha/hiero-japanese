//
//  CollectionScreen.swift
//  iosApp
//
//  Created by Pavel on 26.04.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import HieroUi

struct CollectionScreen : View {
    
    private let component: CollectionComponent
    
    @ObservedObject
    private var state: ObservableValue<CollectionState>
    
    init(component: CollectionComponent) {
        self.component = component
        self.state = ObservableValue(component.state)
    }

    var body: some View {
        List {
            ForEach(state.value.items.indices) {
                let item = state.value.items[$0]
                Text("Item \(item)")
                    .onTapGesture {
                        component.navigateToItemDetails()
                    }
            }
        }.navigationTitle("Collection \(state.value.collectionId)")
    }
}
