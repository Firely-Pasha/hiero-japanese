package space.compoze.hiero.domain.collectionitem.interactor.notification

import arrow.core.raise.either
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import space.compoze.hiero.domain.collectionitem.CollectionItemNotification
import space.compoze.hiero.domain.collectionitem.CollectionItemNotifier
import space.compoze.hiero.domain.section.interactor.SectionUpdateComputedUseCase
import space.compoze.hiero.domain.section.model.mutate.SectionComputedMutation

class CollectionItemNotificationListenUseCase(
    private val collectionItemNotificationGetFlowUseCase: CollectionItemNotificationGetFlowUseCase,
    private val sectionUpdateComputedUseCase: SectionUpdateComputedUseCase,
) {

    operator fun invoke(): Job {
        return collectionItemNotificationGetFlowUseCase().onEach {
            when (it) {
                is CollectionItemNotification.Changed -> {
                    if (it.prev.isSelected != it.new.isSelected) {
                        sectionUpdateComputedUseCase(
                            it.new.sectionId, SectionComputedMutation.AddSelectedCount(
                                it.new.isSelected diffWith it.prev.isSelected
                            )
                        )
                    }
                    if (it.prev.isBookmarked != it.new.isBookmarked) {
                        sectionUpdateComputedUseCase(
                            it.new.sectionId, SectionComputedMutation.AddBookmarkedCount(
                                it.new.isBookmarked diffWith it.prev.isBookmarked
                            )
                        )
                    }
                }
            }
        }.launchIn(GlobalScope)
    }

}

private infix fun Boolean.diffWith(other: Boolean) = when {
    this && !other -> 1L
    !this && other -> -1L
    else -> 0L
}