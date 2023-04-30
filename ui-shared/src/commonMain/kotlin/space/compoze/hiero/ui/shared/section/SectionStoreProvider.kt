@file:Suppress("ThrowableNotThrown")

package space.compoze.hiero.ui.shared.section

import arrow.core.getOrElse
import arrow.core.raise.either
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.collection.interactor.CollectionGetByUuidUseCase
import space.compoze.hiero.domain.collectionitem.interactor.CollectionItemGetOfSectionUseCase
import space.compoze.hiero.domain.section.interactor.SectionGetByIdUseCase

@OptIn(ExperimentalMviKotlinApi::class)
class SectionStoreProvider(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val collectionGetByUuidUseCase: CollectionGetByUuidUseCase by inject()
    private val sectionGetByIdUseCase: SectionGetByIdUseCase by inject()
    private val collectionItemGetOfSectionUseCase: CollectionItemGetOfSectionUseCase by inject()

    fun create(sectionId: String): SectionStore = object : SectionStore,
        Store<SectionIntent, SectionState, Nothing> by storeFactory.create<SectionIntent, SectionAction, SectionMessage, SectionState, Nothing>(
            name = "KEK",
            initialState = SectionState.Loading,
            bootstrapper = coroutineBootstrapper {
                either {
                    val section = sectionGetByIdUseCase(sectionId).bind()
                        .getOrElse { raise(DomainError("Section not found :(")) }
                    val collection = collectionGetByUuidUseCase(section.collectionId).bind()
                        .getOrElse { raise(DomainError("Collection not found :(")) }
                    val sectionItems = collectionItemGetOfSectionUseCase(sectionId).bind()
                    dispatch(
                        SectionAction.Loaded(
                            collection = collection,
                            section = section,
                            items = sectionItems
                        )
                    )
                }.onLeft {
                    dispatch(SectionAction.LoadingError(it))
                }
            },
            executorFactory = coroutineExecutorFactory {
                onAction<SectionAction.LoadingError> {
                    dispatch(SectionMessage.Error(it.error))
                }
                onAction<SectionAction.Loaded> {
                    dispatch(
                        SectionMessage.InitSection(
                            collection = it.collection,
                            section = it.section,
                            items = it.items,
                        )
                    )
                }
            },
            reducer = { msg ->
                when (msg) {
                    is SectionMessage.Error -> SectionState.Error(msg.error)
                    is SectionMessage.InitSection -> SectionState.Content(
                        collection = msg.collection,
                        section = msg.section,
                        items = msg.items,
                    )
                }
            }
        ) {}

    private fun SectionState.withContent(block: (SectionState.Content) -> Unit) {
        if (this is SectionState.Content) {
            block(this)
        }
    }

    private fun SectionState.applyContent(block: SectionState.Content.() -> SectionState): SectionState {
        if (this is SectionState.Content) {
            return block(this)
        }
        return this
    }
}