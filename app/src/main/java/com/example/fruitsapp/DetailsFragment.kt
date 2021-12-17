package com.example.fruitsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.fruitsapp.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    private lateinit var mViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_details, container, false)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_details, container, false)

        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        displayFruitDetails()

        initilizeBackButtonClickListener()

        return  binding.root
    }


    private fun displayFruitDetails(){

        mViewModel.getFruitLd()?.observe(viewLifecycleOwner,{
            binding.tvDetailsName.text= it?.name
            binding.tvDetailsDescription.text= it?.description
            binding.tvDetailsPrice.text= "Price: "+it?.price
            Picasso.get().load(it?.imageUrl).into(binding.ivDetailsImage)
        })
    }

    private fun initilizeBackButtonClickListener(){

        binding.ivDetailsBack.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailsFragment_to_homeListFragment)
        }
    }
}