package space.compoze.hiero.ui.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.structuralEqualityPolicy
import com.arkivanov.decompose.value.Value

@Composable
fun <T : Any> Value<T>.subscribeAsState(policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()): State<T> {
    val state = remember(this, policy) { mutableStateOf(value, policy) }

    DisposableEffect(this) {
        val observer: (T) -> Unit = { state.value = it }

        subscribe(observer)

        onDispose {
            unsubscribe(observer)
        }
    }

    return state
}

@Composable
fun <T : Any, R> Value<T>.select(policy: SnapshotMutationPolicy<R> = structuralEqualityPolicy(), block: (T) -> R): State<R> {

    val state = remember(this, block) { mutableStateOf(block(value), policy) }

    DisposableEffect(this) {
        val observer: (T) -> Unit = { state.value = block(it) }

        subscribe(observer)

        onDispose {
            unsubscribe(observer)
        }
    }

    return state
}