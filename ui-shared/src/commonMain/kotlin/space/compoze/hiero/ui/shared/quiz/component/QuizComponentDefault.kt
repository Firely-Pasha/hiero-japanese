package space.compoze.hiero.ui.shared.quiz.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import space.compoze.hiero.domain.base.AppDispatchers
import space.compoze.hiero.ui.shared.quiz.store.QuizStore
import space.compoze.hiero.ui.shared.quiz.store.QuizStoreProvider
import space.compoze.hiero.ui.shared.stacknavigation.StackNavigationComponent
import space.compoze.hiero.ui.shared.utils.inheritScope

class QuizComponentDefault(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val navigationComponent: StackNavigationComponent,
    private val items: List<String>,
) : QuizComponent, KoinComponent, ComponentContext by componentContext {

    private val dispatchers: AppDispatchers by inject()

    private val store = instanceKeeper.getStore {
        QuizStoreProvider(storeFactory).create(items)
    }

    override val state = MutableValue(store.state)

    init {
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY, dispatchers.unconfined) {
            store.states bindTo ::onStateChange
        }
    }

    private fun onStateChange(newState: QuizStore.State) {
        state.value = newState
    }

    override fun onAction(action: QuizComponent.Action) {
        when (action) {
            QuizComponent.Action.NavigateBack -> navigationComponent.navigateBack()
            QuizComponent.Action.BookmarkCurrentItem -> store.accept(QuizStore.Intent.BookmarkCurrentItem)
            QuizComponent.Action.NextItem -> store.accept(QuizStore.Intent.NextItem)
            QuizComponent.Action.SwapVariants -> store.accept(QuizStore.Intent.SwapVariants)
        }
    }

}