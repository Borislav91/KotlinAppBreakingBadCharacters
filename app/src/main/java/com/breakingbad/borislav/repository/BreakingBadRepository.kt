package com.breakingbad.borislav.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.breakingbad.borislav.data.Character
import com.breakingbad.borislav.data.CharacterEntity
import com.breakingbad.borislav.retrofit.BreakingBadInterface
import com.breakingbad.borislav.room.CharactersDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class BreakingBadRepository constructor(private val breakingBadInterface: BreakingBadInterface) {
    private val charactersLiveData = MutableLiveData<List<CharacterEntity>?>()
    val characters: LiveData<List<CharacterEntity>?>
        get() = charactersLiveData

    private val singleCharacterLiveData = MutableLiveData<CharacterEntity>()
    val singleCharacter: LiveData<CharacterEntity>
        get() = singleCharacterLiveData

    var fetched = 0

    fun getBreakingBadCharacters() {

        try {
            val call = breakingBadInterface.getCharacters()
            call.enqueue(object : Callback<List<Character>> {
                override fun onResponse(
                    call: Call<List<Character>>,
                    response: Response<List<Character>>
                ) {
                    if (response.body() != null) {
                        var characterEntityList = ArrayList<CharacterEntity>()
                        for (character in response.body()!!) {
                            val characterEntity = CharacterEntity(
                                character.char_id,
                                character.img,
                                character.nickname,
                                character.nickname,
                                character.occupation.toString(),
                                character.status,
                                character.appearance.toString()
                            )
                            characterEntityList.add(characterEntity)
                        }
                        fetched = 1
                        charactersLiveData.postValue(characterEntityList)

                    } else {
                        fetched = -1
                        charactersLiveData.postValue(null)
                    }

                }

                override fun onFailure(call: Call<List<Character>>, t: Throwable) {
                    charactersLiveData.postValue(null)
                    fetched = -1
                }
            })
        } catch (exception: Exception) {
            fetched = -1
            charactersLiveData.postValue(null)

        }
    }

    fun getCharacterDetails(character: CharacterEntity) {
        singleCharacterLiveData.postValue(character)
    }


    suspend fun saveDataToRoom(context: Context, characters: List<CharacterEntity>) {

        val db = CharactersDatabase.getDatabase(context)
        val charactersDao = db.characterDao()
        for (character in characters) {
            charactersDao.insertCharacter(character)
        }
    }


    suspend fun getDataFromRoom(context: Context) {
        val db = CharactersDatabase.getDatabase(context)
        val charactersDao = db.characterDao()
        val list = charactersDao.getAllCharacters()
        if (list.isNotEmpty()) {
            charactersLiveData.postValue(list)
        }
    }


    companion object {
        fun getRepo(): BreakingBadRepository {
            return BreakingBadRepository(BreakingBadInterface.create())
        }
    }


}