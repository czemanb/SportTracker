package hu.bme.aut.android.mysporttrackerapp.presentation.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import hu.bme.aut.android.mysporttrackerapp.R
import hu.bme.aut.android.mysporttrackerapp.databinding.FragmentRunDetailsBinding
import hu.bme.aut.android.mysporttrackerapp.utils.UtilityHelpers
import java.text.SimpleDateFormat
import java.util.*


class RunDetailsFragment : Fragment(R.layout.fragment_run_details) {

    private val args: RunDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentRunDetailsBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val run = args.sportEntityArgs
        Glide.with(this).load(run.img).into(binding.ivRunImage)

        val distanceInKm = "Megtett kilóméter: ${run.distanceInMeters / 1000f}km"
        binding.tvDistance.text = distanceInKm



        val avgSpeed = "${run.avgSpeedInKMH}km/h"
        binding.tvAvgSpeed.text = "Átlagos sebesség: $avgSpeed"

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = run.timestamp
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        binding.tvDate.text = "Dátum: ${dateFormat.format(calendar.time)}"

        binding.tvTime.text = "Futással töltött idő: ${(UtilityHelpers.WatchTimeFormatter(run.timeInMillis))}"

        val caloriesBurned = "${run.caloriesBurned}kcl"
        binding.tvCalories.text = "Elégetett kalória: $caloriesBurned"

    }

}