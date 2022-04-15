package com.breakingbad.borislav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.breakingbad.borislav.data.CharacterEntity
import com.breakingbad.borislav.databinding.FragmentDetailsBinding
import com.breakingbad.borislav.repository.BreakingBadRepository
import com.breakingbad.borislav.viewmodels.DetailsFragmentViewModel
import com.bumptech.glide.Glide


class DetailsFragment : Fragment() {


    private lateinit var detailsFragmentViewModel: DetailsFragmentViewModel
    private lateinit var binding: FragmentDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!this::binding.isInitialized) {

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

            val character = requireArguments().get("character") as CharacterEntity

            detailsFragmentViewModel =
                DetailsFragmentViewModel(BreakingBadRepository.getRepo(), character)

            detailsFragmentViewModel.getCharacterDetails()

            detailsFragmentViewModel.characterLiveData.observe(viewLifecycleOwner, Observer {
                if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                    Glide.with(requireContext()).load(it.img).into(binding.detailsImage)
                    binding.detailsName.text = it.name

                    var occupationString = it.occupation.toString()
                    occupationString = occupationString.replace("[", "")
                    occupationString = occupationString.replace("]", "")
                    binding.detailsOccupation.text = occupationString
                    binding.detailsStatus.text = it.status
                    binding.detailsNickname.text = it.nickname
                    var appearanceString = it.appearance.toString()
                    appearanceString = appearanceString.replace("[", "")
                    appearanceString = appearanceString.replace("]", "")
                    binding.detailsAppearance.text = "Season $appearanceString"

                }
            })

        }



        return binding.root
    }

}