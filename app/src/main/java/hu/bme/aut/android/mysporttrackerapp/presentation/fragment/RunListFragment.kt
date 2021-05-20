package hu.bme.aut.android.mysporttrackerapp.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.mysporttrackerapp.R
import hu.bme.aut.android.mysporttrackerapp.adapter.RunAdapter
import hu.bme.aut.android.mysporttrackerapp.databinding.FragmentRunlistBinding
import hu.bme.aut.android.mysporttrackerapp.model.SportEntity
import hu.bme.aut.android.mysporttrackerapp.presentation.RunViewModel
import hu.bme.aut.android.mysporttrackerapp.presentation.SortedRunType

@AndroidEntryPoint
class RunListFragment : Fragment(R.layout.fragment_runlist), RunAdapter.RunItemClickListener {


    private val viewModel: RunViewModel by viewModels()

    private lateinit var binding: FragmentRunlistBinding

    private lateinit var runAdapter: RunAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunlistBinding.inflate(layoutInflater, container, false)


        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        when (viewModel.sortedRunType) {
            SortedRunType.DATE -> binding.spFilter.setSelection(0)
            SortedRunType.DISTANCE -> binding.spFilter.setSelection(1)
        }

        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when (pos) {
                    0 -> viewModel.sortRuns(SortedRunType.DATE)
                    1 -> viewModel.sortRuns(SortedRunType.DISTANCE)
                }
            }
        }

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })



        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_mapviewFragment)
        }
    }

    private fun setupRecyclerView(){
        runAdapter = RunAdapter()
        runAdapter.itemClickListener = this
        binding.rvRuns.apply {
            adapter = runAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onItemClick(run: SportEntity) {
        Log.e("sas","sas")
        //findNavController().navigate(R.id.action_runListFragment_to_runDetailsFragment)
        val action = RunListFragmentDirections.actionRunListFragmentToRunDetailsFragment(run)
        findNavController().navigate(action)
    }

    override fun onItemLongClick(position: Int, view: View, run: SportEntity): Boolean {
        Log.e("sas","sas")
        val popup = PopupMenu(activity?.applicationContext, view)
        popup.inflate(R.menu.run_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    viewModel.delete(run)
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
        popup.show()
        return false
    }


}