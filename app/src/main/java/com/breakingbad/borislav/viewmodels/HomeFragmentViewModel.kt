package com.breakingbad.borislav.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breakingbad.borislav.data.CharacterEntity
import com.breakingbad.borislav.repository.BreakingBadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel constructor(private val repo: BreakingBadRepository) : ViewModel() {

    val charactersLiveData: LiveData<List<CharacterEntity>?>
        get() = repo.characters

    fun makeCharactersCall() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getBreakingBadCharacters()
        }
    }

    fun saveDataToRoom(context: Context, characterEntity: List<CharacterEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repo.fetched == 1) {
                repo.saveDataToRoom(context, characterEntity)
            }
        }
    }


    fun getDataFromRoom(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repo.fetched == -1) {
                repo.getDataFromRoom(context)
            }
        }
    }


}