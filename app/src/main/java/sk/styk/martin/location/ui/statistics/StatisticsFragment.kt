package sk.styk.martin.location.ui.statistics

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_statistics.*
import sk.styk.martin.location.R
import sk.styk.martin.location.databinding.FragmentStatisticsBinding
import java.util.*
import javax.inject.Inject

class StatisticsFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModel: StatisticsViewModel

    private lateinit var binding: FragmentStatisticsBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)
        binding.viewmodel = viewModel
        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpeedChart()
    }


    private fun setupSpeedChart() {
        val lineData = LineDataSet(listOf(Entry(1f, 1f)), "").apply {
            setDrawIcons(false)
            setDrawCircles(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            color = Color.BLACK
            setCircleColor(Color.BLACK)
            lineWidth = 1f
            circleRadius = 3f
            setDrawCircleHole(false)
            valueTextSize = 0f
            setDrawFilled(true)
            formLineWidth = 1f
            formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            formSize = 15f

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_accent)
                fillDrawable = drawable
            } else {
                fillColor = Color.BLACK
            }
        }

        chart_consumption.data = LineData(ArrayList<ILineDataSet>().apply { add(lineData) })
        chart_consumption.description.isEnabled = false
        chart_consumption.xAxis.isEnabled = false
        chart_consumption.axisRight.isEnabled = false
        chart_consumption.legend.isEnabled = false

        chart_consumption.axisLeft.apply {
            enableGridDashedLine(10f, 1f, 0f)
            setDrawZeroLine(false)
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            setDrawLimitLinesBehindData(true)
        }
    }

    companion object {
        private val TAG = StatisticsFragment::class.java.simpleName
    }
}
