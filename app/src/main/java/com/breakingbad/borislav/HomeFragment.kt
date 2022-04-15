package com.breakingbad.borislav

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.breakingbad.borislav.adapter.CharactersRecyclerViewAdapter
import com.breakingbad.borislav.adapter.SpinnerAdapter
import com.breakingbad.borislav.data.CharacterEntity
import com.breakingbad.borislav.databinding.FragmentHomeBinding
import com.breakingbad.borislav.repository.BreakingBadRepository
import com.breakingbad.borislav.viewmodels.HomeFragmentViewModel


class HomeFragment : Fragment() {

    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var charactersRecyclerViewAdapter: CharactersRecyclerViewAdapter
    private var charactersList = ArrayList<CharacterEntity>()
    private var searchList = ArrayList<CharacterEntity>()
    private lateinit var navController: NavController
    private var check = 1
    private lateinit var seasons: Array<String>
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!this::binding.isInitialized) {

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
            navController = findNavController()

            seasons = resources.getStringArray(R.array.seasons)

            val adapter = SpinnerAdapter(requireContext(), seasons)
            binding.spinner.adapter = adapter


            binding.spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    filterBySeason(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

            homeFragmentViewModel = HomeFragmentViewModel(BreakingBadRepository.getRepo())


            homeFragmentViewModel.makeCharactersCall()



            homeFragmentViewModel.charactersLiveData.observe(viewLifecycleOwner, Observer {
                if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                    if (it != null) {
                        charactersList = it as ArrayList<CharacterEntity>
                        if (charactersList.isNotEmpty()) {
                            homeFragmentViewModel.saveDataToRoom(requireContext(), charactersList)
                            binding.homeProgressbar.visibility = View.GONE
                            charactersRecyclerViewAdapter =
                                CharactersRecyclerViewAdapter(charactersList, navController)
                            binding.homeRecyclerview.adapter = charactersRecyclerViewAdapter
                            binding.homeRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
                            binding.noCharacter.visibility = View.GONE

                        }
                    }
                    else
                    {
                        homeFragmentViewModel.getDataFromRoom(requireContext())
                        binding.noCharacter.visibility = View.VISIBLE
                        binding.noCharacter.text = "Some problem occurred while fetching data!"
                        binding.homeProgressbar.visibility = View.GONE
                    }
                }
            })

            binding.searchIcon.setOnClickListener {
                if (check == 1) {
                    var query = binding.searchText.text.toString()
                    if (query.isNotEmpty()) {
                        check = 2
                        binding.searchIcon.setImageResource(R.drawable.ic_close)
                        searchList.clear()
                        binding.spinner.setSelection(0)
                        binding.spinner.isEnabled = false
                        performSearch(query.lowercase())
                        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                    }
                } else {

                    check = 1
                    binding.searchIcon.setImageResource(R.drawable.ic_search)
                    binding.searchText.text.clear()
                    binding.spinner.isEnabled = true
                    if (searchList.size > 0) {
                        charactersRecyclerViewAdapter.updateData(charactersList)
                        charactersRecyclerViewAdapter.notifyDataSetChanged()
                    } else {
                        binding.homeRecyclerview.visibility = View.VISIBLE
                        binding.noCharacter.visibility = View.GONE
                    }

                }
            }

        }
        return binding.root
    }




    private fun performSearch(query: String) {
        if (charactersList.size > 0) {
            for (character in charactersList) {
                var checkText = character.name.lowercase()
                if (checkText.contains(query)) {
                    searchList.add(character)
                }
            }

            if (searchList.size > 0) {
                charactersRecyclerViewAdapter.updateData(searchList)
                charactersRecyclerViewAdapter.notifyDataSetChanged()
            } else {
                binding.homeRecyclerview.visibility = View.GONE
                binding.noCharacter.visibility = View.VISIBLE
                binding.noCharacter.text = "No characters found"
            }
        }
    }

    private fun filterBySeason(season: Int) {
        if (season == 0) {
            if (searchList.size <= 0) {
                if (this::charactersRecyclerViewAdapter.isInitialized) {
                    binding.homeRecyclerview.visibility = View.VISIBLE
                    binding.noCharacter.visibility = View.GONE
                }
            }
        } else {
            if (charactersList.size > 0) {
                searchList.clear()
                for (item in charactersList) {
                    for (temp in item.appearance) {
                        if (temp.toString() == season.toString()) {
                            searchList.add(item)
                        }
                    }
                }
                if (searchList.size > 0) {

                    if (this::charactersRecyclerViewAdapter.isInitialized) {
                        charactersRecyclerViewAdapter.updateData(searchList)
                        charactersRecyclerViewAdapter.notifyDataSetChanged()
                    }
                }
                else
                {
                    binding.noCharacter.visibility = View.VISIBLE
                    binding.noCharacter.text = "No characters found"
                    binding.homeRecyclerview.visibility = View.GONE
                }

            }
        }
    }


}