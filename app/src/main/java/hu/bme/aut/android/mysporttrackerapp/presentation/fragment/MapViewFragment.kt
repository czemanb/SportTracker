package hu.bme.aut.android.mysporttrackerapp.presentation.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.mysporttrackerapp.R
import hu.bme.aut.android.mysporttrackerapp.databinding.FragmentMapviewBinding
import hu.bme.aut.android.mysporttrackerapp.model.SportEntity
import hu.bme.aut.android.mysporttrackerapp.presentation.RunViewModel
import hu.bme.aut.android.mysporttrackerapp.service.RunningService
import hu.bme.aut.android.mysporttrackerapp.utils.Const
import hu.bme.aut.android.mysporttrackerapp.utils.Const.ACTION_PAUSE_SERVICE
import hu.bme.aut.android.mysporttrackerapp.utils.Const.ACTION_START_OR_RESUME_SERVICE
import hu.bme.aut.android.mysporttrackerapp.utils.Const.ACTION_STOP_SERVICE
import hu.bme.aut.android.mysporttrackerapp.utils.Const.KEY_WEIGHT
import hu.bme.aut.android.mysporttrackerapp.utils.Const.MAP_ZOOM
import hu.bme.aut.android.mysporttrackerapp.utils.Const.LINE_COLOR
import hu.bme.aut.android.mysporttrackerapp.utils.Const.LINE_WIDTH
import hu.bme.aut.android.mysporttrackerapp.utils.UtilityHelpers
import java.util.*
import kotlin.math.round


@AndroidEntryPoint
class MapViewFragment : Fragment(R.layout.fragment_mapview) {

    private lateinit var binding: FragmentMapviewBinding
    private lateinit var sharedPref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }



    private val viewModel: RunViewModel by viewModels()

    private var isRunning = false
    private var pathPoints = mutableListOf<MutableList<LatLng>>()

    private var map: GoogleMap? = null
    private var currentTimeInMillissec = 0L


   private var weight = 100f
    @Override
    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        sharedPref = requireActivity().getSharedPreferences(Const.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weight= sharedPref.getFloat(KEY_WEIGHT,0.0f)
        binding.mapView.onCreate(savedInstanceState)
        binding.btnSwitchRun.setOnClickListener {
            switchRun()
        }
        binding.mapView.getMapAsync {
            map = it
            addlines()
        }

        binding.btnFinishRun.setOnClickListener {
            zoom()
            endRun()
        }



        initViewModelData()
    }

    private fun initViewModelData() {
        RunningService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        RunningService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestLine()
            moveCameraToUser()
        })

        RunningService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillissec = it
            val formattedTime = UtilityHelpers.WatchTimeFormatter(currentTimeInMillissec, true)
            binding.tvTimer.text = formattedTime
        })
    }

    private fun switchRun() {
        if(isRunning) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isRunning: Boolean) {
        this.isRunning = isRunning
        if(!isRunning && currentTimeInMillissec > 0L) {
            binding.btnSwitchRun.text = "Start"
            binding.btnFinishRun.visibility = View.VISIBLE
        } else if (isRunning) {
            binding.btnSwitchRun.text = "Stop"
            binding.btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun zoom() {
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints) {
            for(pos in polyline) {
                bounds.include(pos)
            }
        }
        map?.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                        bounds.build(),
                        binding.mapView.width,
                        binding.mapView.height,
                        (binding.mapView.height * 0.05f).toInt()
                )
        )
    }

    private fun endRun() {
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for (line in pathPoints) {
                distanceInMeters += UtilityHelpers.calculateRunLength(line).toInt()
            }
            val avgSpeed: Float = round((distanceInMeters / 1000f) / (currentTimeInMillissec / 1000f / 60 / 60) * 10) / 10f
            val dateTimestamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()
            val run = SportEntity(
                avgSpeedInKMH = avgSpeed,
                    distanceInMeters = distanceInMeters,
                    timeInMillis = currentTimeInMillissec,
                    caloriesBurned = caloriesBurned,
                    timestamp = dateTimestamp,
                    img = bmp)
            viewModel.insertRun(run)
            Snackbar.make(
                    requireActivity().findViewById(R.id.rootView),
                    "A futás sikeresen elmentve",
                    Snackbar.LENGTH_LONG
            ).show()
            stopRun()
        }
    }

    private fun addlines() {
        for(line in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(LINE_COLOR)
                .width(LINE_WIDTH)
                .addAll(line)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestLine() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(LINE_COLOR)
                .width(LINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), RunningService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }


    private fun stopRun() {
        binding.tvTimer.text = "00:00:00:00"
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_mapviewFragment_to_runListFragment)
    }
}