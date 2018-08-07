package sk.styk.martin.location.ui.locationdetailsheet

import android.databinding.BindingAdapter
import android.location.Location
import android.text.format.DateFormat
import android.widget.TextView

object LocationDetailSheetBinding {

    @JvmStatic
    @BindingAdapter("coordinate")
    fun setCoordinate(view: TextView, coordinate: Double?) {
        if (coordinate == null)
            view.text = ""
        else
            view.text = Location.convert(coordinate, Location.FORMAT_SECONDS)
    }

    @JvmStatic
    @BindingAdapter("time")
    fun setTime(view: TextView, time: Long?) {
        if (time == null)
            view.text = ""
        else
            view.text = DateFormat.getDateFormat(view.context).format(time)
    }

    @JvmStatic
    @BindingAdapter("android:text")
    fun setDoubleText(view: TextView, value: Double?) {
        if (value == null)
            view.text = ""
        else
            view.text = value.toString()
    }
}

