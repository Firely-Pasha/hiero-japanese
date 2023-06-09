package space.compoze.hiero.domain.collectionitem.interactor.notification

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import space.compoze.hiero.domain.collectionitem.CollectionItemNotification
import space.compoze.hiero.domain.section.interactor.SectionUpdateComputed
import space.compoze.hiero.domain.section.model.mutate.SectionComputedMutation

class CollectionItemNotificationListen(
    private val collectionItemNotificationGetFlow: CollectionItemNotificationGetFlow,
    private val sectionUpdateComputed: SectionUpdateComputed,
) {

    operator fun invoke(): Job {
        return collectionItemNotificationGetFlow().onEach {
            when (it) {
                is CollectionItemNotification.Changed -> {
                    if (it.prev.isSelected != it.new.isSelected) {
                        sectionUpdateComputed(
                            it.new.sectionId, SectionComputedMutation.AddSelectedCount(
                                it.new.isSelected diffWith it.prev.isSelected
                            )
                        )
                    }
                    if (it.prev.isBookmarked != it.new.isBookmarked) {
                        sectionUpdateComputed(
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