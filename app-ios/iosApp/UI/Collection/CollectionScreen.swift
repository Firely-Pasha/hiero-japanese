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
                        SectionItem(section: item)
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

struct SectionItem: View {
    
    let section: DomainSectionModel
    
    var body: some View {
        VStack(spacing: 12) {
            HStack {
                Text(section.title)
                    .fontWeight(.bold)
                Spacer()
                Image(systemName: "chevron.forward")
            }
            HStack(spacing: 12) {
                HStack {
                    Image(systemName: "rectangle.portrait")
                        .imageScale(.small)
                    Text("\(section.itemsCount)")
                }
                Spacer()
                HStack {
                    HStack {
                        Image(systemName: "graduationcap")
                            .imageScale(.small)
                        Text("\(section.itemsCount)")
                    }
                }
                HStack {
                    HStack {
                        Image(systemName: "bookmark")
                            .imageScale(.small)
                        Text("\(section.itemsCount)")
                    }
                }
            }
        }
    }
}

struct SectionItem_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            List {
                SectionItem(
                    section: DomainSectionModel(
                        id: "SECTION_1",
                        collectionId: "WKK",
                        title: "Section 1",
                        selectedCount: 14,
                        bookmarkedCount: 5,
                        span: 3,
                        itemsCount: 23
                    )
                )
            }
        }
    }
}
