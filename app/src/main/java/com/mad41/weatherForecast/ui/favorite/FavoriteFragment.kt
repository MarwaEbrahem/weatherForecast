package com.mad41.weatherForecast.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mad41.weatherForecast.dataLayer.Resource
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
import com.mad41.weatherForecast.databinding.FragmentFavLocationsBinding

class  FavoriteFragment : Fragment() {

    private lateinit var favViewModel: FavoriteViewModel
    private lateinit var binding: FragmentFavLocationsBinding
    private lateinit var listner: favListner
    private lateinit var favAdapter :favLocAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        favViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        binding = FragmentFavLocationsBinding.inflate(layoutInflater)
        favViewModel.getFavLocationFromRoom()
        favViewModel.favLocFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            handleFavLocData(it)
        })
        listner = object : favListner{
            override fun DeleteLocation(address: String) {
                deleteFavLoc(address)
            }
            override fun saveLocationInSharedPreference(address: String,LatLong: String) {
                favViewModel.writeFavInSharedPreference(address,LatLong)
            }

            override fun writeFavInSharedPreference(fav: Boolean) {
                favViewModel.writeFavInSharedPreference(fav)
            }

            override fun getAddressFromSharedPreference(): String? {
                return favViewModel.getAddressFromSharedPreference()
            }
        }
        val root = binding.root
        return root
    }

    private fun handleFavLocData(it: Resource<List<favLocation>>?) {
        when (it) {
            is Resource.Success -> {
                if(it.data == null){
                    showMassage()
                }
                it.data?.let {
                    setupRecycleView(it)
                }
            }
            is Resource.Error -> {
                Toast.makeText(getActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupRecycleView(it: List<favLocation>){
        if(it.isEmpty()){
            binding.favList.visibility = View.GONE
            binding.favMsg.visibility = View.VISIBLE
            binding.favImg.visibility = View.VISIBLE
        }
        else {
            binding.favList.visibility = View.VISIBLE
            binding.favMsg.visibility = View.GONE
            binding.favImg.visibility = View.GONE
        }
        favAdapter = favLocAdapter(it , context , listner)
        binding.facLocations.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favAdapter
        }
    }
    fun showMassage() {
        binding.favList.visibility = View.GONE
        binding.favMsg.visibility = View.VISIBLE
        binding.favImg.visibility = View.VISIBLE
    }
    private fun deleteFavLoc(address:String){
        favViewModel.deleteFavLocationFromRoom(address)
        favViewModel.deleteLocLiveData.observe(viewLifecycleOwner, Observer {
            favViewModel.getFavLocationFromRoom()
            notifyRecycleViewWithChanage()
        })
    }
    private fun notifyRecycleViewWithChanage() {
        favViewModel.favLocFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let {
                        favAdapter.dataChange(it)
                    }
                }
            }
        })
    }
}