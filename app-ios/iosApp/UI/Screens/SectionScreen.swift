//
//  SectionScreen.swift
//  iosApp
//
//  Created by Pavel on 18.06.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import HieroApp

struct SectionScreen: View {
    
    private let component: SectionComponent
    
    @ObservedObject
    private var state: ObservableValue<SectionStoreState>
    
    init(component: SectionComponent) {
        self.component = component
        self.state = ObservableValue(component.state)
    }
    
    var body: some View {
        if let content = state.value as? SectionStoreStateContent {
            SectionContent(state: content)
        } else {
            Text("ERRROR")
        }
    }
}

struct SectionContent: View {
    
    let state: SectionStoreStateContent
    
    var body: some View {
        Text(state.section.title)
    }
}
