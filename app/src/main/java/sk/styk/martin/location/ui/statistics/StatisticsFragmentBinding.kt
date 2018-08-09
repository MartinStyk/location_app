package sk.styk.martin.location.ui.statistics

import android.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import sk.styk.martin.location.db.LocationData
import sk.styk.martin.location.util.UnitUtils
import java.util.*

object StatisticsFragmentBinding {
    @JvmStatic
    @BindingAdapter("speedData")
    fun speedData(chart: LineChart, speedData: List<LocationData>?) {
        speedData ?: return

        val dataSet = chart.data?.getDataSetByIndex(0) as LineDataSet

        dataSet.values = if (speedData.isEmpty()) {
            listOf(Entry(0f, 0f))
        } else {
            speedData.mapIndexedTo(ArrayList(speedData.size), { index, location ->
                Entry(index.toFloat(), UnitUtils.speedConvert(location.speed), null)
            })
        }
        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()
        chart.invalidate()
    }
}

