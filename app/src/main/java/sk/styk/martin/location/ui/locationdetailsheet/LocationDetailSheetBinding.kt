package sk.styk.martin.location.ui.locationdetailsheet

import android.databinding.BindingAdapter
import android.location.Location
import android.text.format.DateFormat
import sk.styk.martin.location.view.KeyValueUnitItem

object LocationDetailSheetBinding {

    @JvmStatic
    @BindingAdapter("coordinate")
    fun setCoordinate(view: KeyValueUnitItem, coordinate: Double?) {
        if (coordinate == null)
            view.setValue("")
        else
            view.setValue(Location.convert(coordinate, Location.FORMAT_SECONDS))
    }

    @JvmStatic
    @BindingAdapter("time")
    fun setTime(view: KeyValueUnitItem, time: Long?) {
        if (time == null)
            view.setValue("")
        else
            view.setValue(DateFormat.getTimeFormat(view.context).format(time))
    }

}

