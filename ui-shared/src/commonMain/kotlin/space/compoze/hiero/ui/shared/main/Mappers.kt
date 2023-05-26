package space.compoze.hiero.ui.shared.main

import space.compoze.hiero.ui.shared.main.component.MainComponent
import space.compoze.hiero.ui.shared.main.store.MainStore

fun MainStore.State.toModel() = MainComponent.Model(
    tab = tab
)