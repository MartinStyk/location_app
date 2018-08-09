package sk.styk.martin.location.ui.map

enum class MapViewType {
    TRACKING,
    FIT_ALL,
    USER_FREE;

    fun change(): MapViewType = when (this) {
        TRACKING -> FIT_ALL
        FIT_ALL -> TRACKING
        USER_FREE -> TRACKING
    }
}
