package sk.styk.martin.location.ui.locationdetailsheet

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.location.R
import sk.styk.martin.location.databinding.LocationInfoBottomSheetBinding
import javax.inject.Inject

class LocationDetailBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModel: LocationDetailViewModel

    private lateinit var binding: LocationInfoBottomSheetBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationId = arguments?.getLong(ARGUMENT_LOCATION)
                ?: throw IllegalArgumentException("locationId not found")

        viewModel.init(locationId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.location_info_bottom_sheet, container, false)
        binding.viewmodel = viewModel
        binding.setLifecycleOwner(this)

        return binding.root
    }


    companion object {

        private val TAG = LocationDetailBottomSheet::class.java.simpleName

        private const val ARGUMENT_LOCATION = "locationId"

        @JvmStatic
        fun newInstance(locationId: Long) =
                LocationDetailBottomSheet().apply {
                    arguments = Bundle().apply {
                        putLong(ARGUMENT_LOCATION, locationId)
                    }
                }
    }
}
