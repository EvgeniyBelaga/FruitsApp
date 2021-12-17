package com.example.fruitsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fruitsapp.databinding.ActivityMainBinding
import com.example.fruitsapp.databinding.FragmentHomeListBinding
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeListFragment : Fragment() {

    private lateinit var binding: FragmentHomeListBinding

    private lateinit var mViewModel: SharedViewModel

    private var mFruitRecyclerAdapter: FruitsRecyclerAdapter?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home_list, container, false)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_list, container, false)

        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        mViewModel.getFruits()

        displayFroutList()

        displayProgreassBar()

        displayErrorMessage()

        return  binding.root
    }


    private fun displayFroutList(){

        mViewModel.getFruitListLd()?.observe(viewLifecycleOwner, {
            if(it.size>0){
                binding.rvFruitList.setHasFixedSize(true)
                binding.rvFruitList.setLayoutManager(LinearLayoutManager(requireActivity()))
                mFruitRecyclerAdapter = FruitsRecyclerAdapter(requireActivity(), it, mViewModel)
                binding.rvFruitList.setAdapter(mFruitRecyclerAdapter)
            }
        })
    }


    private fun displayProgreassBar(){
        mViewModel.getIsLoadedLd()?.observe(viewLifecycleOwner, {
            if(it){
                binding.progressBarFruits.visibility= View.GONE
            }
            else{
                binding.progressBarFruits.visibility= View.VISIBLE
            }
        })
    }

    private fun displayErrorMessage(){
        mViewModel.getIsErrorLd()?.observe(viewLifecycleOwner, {
            if(it){
                binding.tvFruitsError.visibility= View.VISIBLE
            }
            else{
                binding.tvFruitsError.visibility= View.GONE
            }
        })
    }


    class FruitsRecyclerAdapter(activity: FragmentActivity?, fruitList: List<Fruit?>, myViewModel: SharedViewModel?) : RecyclerView.Adapter<FruitsRecyclerAdapter.FruitsViewHolder>() {

        var mActivity: FragmentActivity? = activity
        var mFruitList: List<Fruit?>? = fruitList
        var mViewModel: SharedViewModel? = myViewModel




        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitsViewHolder {
            val v: View = LayoutInflater.from(mActivity).inflate(R.layout.item_fruit, parent, false)
            return FruitsViewHolder(v)
        }

        override fun onBindViewHolder(holder: FruitsViewHolder, position: Int) {

            val fruit= mFruitList!!.get(position)

            holder.tvName.text= fruit!!.name

            if(fruit.imageUrl.length>0) {
                Picasso.get().load(fruit.imageUrl).into(holder.ivImage)
            }

            holder.itemView.setOnClickListener {
                mViewModel?.setFruitLd(fruit)
                it.findNavController().navigate(R.id.action_homeListFragment_to_detailsFragment)
            }

        }

        override fun getItemCount(): Int {
            return mFruitList!!.size
        }

        class FruitsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            lateinit var tvName: TextView
            lateinit var ivImage: ImageView

            init {
                tvName = itemView.findViewById(R.id.tv_item_name)
                ivImage = itemView.findViewById(R.id.iv_item_image)
            }
        }

    }

}