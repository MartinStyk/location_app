package sk.styk.martin.location.ui.map

import android.content.res.ColorStateList
import android.databinding.BindingAdapter
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import sk.styk.martin.location.R

object MapFragmentBinding {
    @JvmStatic
    @BindingAdapter("isTrackingActive")
    fun isTrackingActive(view: FloatingActionButton, isTrackingActive: Boolean?) {
        if (isTrackingActive != null && isTrackingActive) {
            view.setImageResource(R.drawable.ic_location_on)
            view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.colorPrimary))
        } else {
            view.setImageResource(R.drawable.ic_location_off)
            view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.grey_100))
        }
    }

    @JvmStatic
    @BindingAdapter("viewType")
    fun viewType(view: FloatingActionButton, viewType: MapViewType?) {
        when (viewType) {
            MapViewType.TRACKING, null -> view.setImageResource(R.drawable.ic_my_location)
            MapViewType.FIT_ALL -> view.setImageResource(R.drawable.ic_zoom_out_map)
            MapViewType.USER_FREE -> view.setImageResource(R.drawable.ic_my_location_off)
        }
    }

}

