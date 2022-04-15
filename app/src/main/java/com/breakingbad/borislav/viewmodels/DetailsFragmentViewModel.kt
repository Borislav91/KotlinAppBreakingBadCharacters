package com.breakingbad.borislav.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.breakingbad.borislav.data.CharacterEntity
import com.breakingbad.borislav.repository.BreakingBadRepository

class DetailsFragmentViewModel constructor(
    private val repo: BreakingBadRepository,
    private val character: CharacterEntity
) : ViewModel() {
    val characterLiveData: LiveData<CharacterEntity>
        get() = repo.singleCharacter

    fun getCharacterDetails() {
        repo.getCharacterDetails(character)
    }
}