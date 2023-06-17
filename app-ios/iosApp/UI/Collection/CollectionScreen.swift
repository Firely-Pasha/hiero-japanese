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
//                VStack {
//
//                }
            if let content = state.value as? CollectionStoreStateContent {
                List {
                    ForEach(content.sections.indices) {
                        let item = content.sections[$0]
                        Text("Item \(item)")
                            .onTapGesture {
                                component.navigateToSection(sectionId: item.id)
                            }
                    }
                }
            } else if let error = state.value as? CollectionStoreStateError {
                Text("\(error.error.message ?? "")")
            }
        }.navigationTitle("Collection")
        
    }
}
