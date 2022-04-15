package com.breakingbad.borislav.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.breakingbad.borislav.HomeFragmentDirections
import com.breakingbad.borislav.R
import com.breakingbad.borislav.data.CharacterEntity
import com.breakingbad.borislav.databinding.SingleCharacterBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CharactersRecyclerViewAdapter (private var dataSet: List<CharacterEntity>, private val navController: NavController) :
    RecyclerView.Adapter<CharactersRecyclerViewAdapter.ViewHolder>() {


//    inner class ViewHolder_findViewById(view: View) : RecyclerView.ViewHolder(view) {
//        val image: ImageView = view.findViewById(R.id.character_single_image)
//        val name: TextView = view.findViewById(R.id.character_single_name)
//        val card: CardView = view.findViewById(R.id.single_card)
//    }

    inner class ViewHolder(binding: SingleCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val image: ImageView = binding.characterSingleImage
            val name: TextView = binding.characterSingleName
            val card: CardView = binding.singleCard
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = dataSet[holder.absoluteAdapterPosition]

        val iconUrl = data.img
        Glide.with(holder.image.context).load(iconUrl).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.image)
        holder.name.text = data.name
        holder.card.setOnClickListener {
            val action: NavDirections = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(data)
            navController.navigate(action)
        }
    }

    fun updateData(charactersList: List<CharacterEntity>){
        dataSet = charactersList
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }
}