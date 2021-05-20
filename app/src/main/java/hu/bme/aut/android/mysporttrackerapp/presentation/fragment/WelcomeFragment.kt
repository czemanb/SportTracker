package hu.bme.aut.android.mysporttrackerapp.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.mysporttrackerapp.R
import hu.bme.aut.android.mysporttrackerapp.databinding.FragmentWelcomeBinding
import hu.bme.aut.android.mysporttrackerapp.utils.Const.KEY_FIRST_TIME
import hu.bme.aut.android.mysporttrackerapp.utils.Const.KEY_NAME
import hu.bme.aut.android.mysporttrackerapp.utils.Const.KEY_WEIGHT
import hu.bme.aut.android.mysporttrackerapp.utils.Const.SHARED_PREFERENCES_NAME


class WelcomeFragment : Fragment(R.layout.fragment_welcome) {


    private lateinit var binding: FragmentWelcomeBinding
    private lateinit var sharedPref : SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    @Override
    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        sharedPref = requireActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isFirstOpen = sharedPref.getBoolean(KEY_FIRST_TIME, true)

        if(!isFirstOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.welcomeFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_welcomeFragment_to_homeFragment,
                savedInstanceState,
                navOptions
            )
        }

        binding.btnLogin.setOnClickListener {
            val name = binding.etName.text.toString()
            val weight = binding.etWeight.text.toString()

            if(name.isEmpty() || weight.isEmpty()) {
                Snackbar.make(requireView(), "Kérlek add meg az adataid!", Snackbar.LENGTH_SHORT).show()
            }else {
                sharedPref!!.edit().putString(KEY_NAME, name).apply()
                sharedPref!!.edit().putFloat(KEY_WEIGHT, weight.toFloat()).apply()
                sharedPref!!.edit().putBoolean(KEY_FIRST_TIME, false).apply()
                findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
            }
        }
    }

}