package sk.styk.martin.location.ui.map

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_maps.*
import sk.styk.martin.location.R
import sk.styk.martin.location.ui.main.LocationServiceBindableActivity
import sk.styk.martin.location.ui.main.LocationTrackingController
import sk.styk.martin.location.ui.main.startLocationTrackingWithPermissionCheck
import com.google.android.gms.maps.model.CameraPosition
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.location.databinding.FragmentMapsBinding
import sk.styk.martin.location.ui.locationdetailsheet.LocationDetailBottomSheet
import javax.inject.Inject


class MapFragment : Fragment(), OnMapReadyCallback {

    @Inject
    lateinit var viewModel: MapViewModel

    private var googleMap: GoogleMap? = null
    private lateinit var binding: FragmentMapsBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        binding.viewmodel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMap(savedInstanceState)
        setupLocationTrackingControl()
    }




    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        setupMarkerClicks()
        setupMapUpdates()
        setupViewTypeControl()
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        MapsInitializer.initialize(activity)
        map.onCreate(savedInstanceState)
        map.getMapAsync(this)
    }

    private fun setupLocationTrackingControl() {

        location_updates_status_button.setOnClickListener {
            activity?.let {
                if (it is LocationServiceBindableActivity) {
                    if (viewModel.trackingStatus.value == true) {
                        it.stopLocationTracking()
                    } else {
                        it.startLocationTrackingWithPermissionCheck()
                    }
                }
            }
        }

        viewModel.trackingStatus.observe(this, Observer {
            if (it == true) {
                Snackbar.make(binding.root, R.string.location_tracking_active, Snackbar.LENGTH_SHORT).show()
                viewModel.changeViewType(MapViewType.TRACKING)
            } else {
                Snackbar.make(binding.root, R.string.location_tracking_not_active, Snackbar.LENGTH_SHORT).show()
                viewModel.changeViewType(MapViewType.FIT_ALL)
            }
        })
    }

    private fun setupViewTypeControl() {
        map_view_type_button.setOnClickListener {
            viewModel.changeViewType()
        }

        viewModel.viewTypeData.observe(this, Observer {
            updateCameraPosition(it)
        })

        googleMap?.setOnCameraMoveStartedListener {
            if (it == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                viewModel.changeViewType(MapViewType.USER_FREE)
            }
        }
    }


    private fun setupMarkerClicks() {
        googleMap?.setOnMarkerClickListener {
            val bottomSheetFragment = LocationDetailBottomSheet.newInstance(it.title.toLong())
            bottomSheetFragment.show(requireFragmentManager(), bottomSheetFragment.tag)
            true
        }
    }

    private fun setupMapUpdates() {
        activity?.let {
            if (it is LocationTrackingController)
                it.updateLocationTrackingInterval(viewModel.locationUpdateInterval)
        }

        viewModel.locationData.observe(this, Observer {
            Log.i(TAG, it.toString())
            it ?: return@Observer

            googleMap?.clear()

            it.forEach {
                val position = LatLng(it.latitude, it.longitude)
                googleMap?.addMarker(MarkerOptions().position(position).title(it.id.toString()))
            }

            googleMap?.addPolyline(PolylineOptions()
                    .color(viewModel.lineColor)
                    .width(viewModel.lineWidth)
                    .addAll(it.map { LatLng(it.latitude, it.longitude) }))

            updateCameraPosition()
        })
    }

    private fun updateCameraPosition(mapViewType: MapViewType? = viewModel.viewTypeData.value) {

        googleMap?.let { map ->
            when (mapViewType) {
                MapViewType.TRACKING, null -> {
                    val latestLocation = viewModel.getLatestLocation() ?: return
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                            .target(latestLocation.latLng())
                            .bearing(latestLocation.bearing)
                            .zoom(25f)
                            .build()))
                }
                MapViewType.FIT_ALL -> {
                    val bounds = viewModel.getCompleteViewBounds() ?: return
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                }
                MapViewType.USER_FREE -> return
            }
        }
    }

    override fun onStart() {
        super.onStart()
        map.onStart()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        map.onPause()
        super.onPause()
    }

    override fun onStop() {
        map.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        map.onDestroy()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_delete_data -> {
            viewModel.deleteLocationData()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = MapFragment::class.java.name
    }
}