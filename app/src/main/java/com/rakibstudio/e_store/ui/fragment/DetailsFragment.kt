package com.rakibstudio.e_store.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rakibstudio.e_store.R
import com.rakibstudio.e_store.adapter.ReviewAdapter
import com.rakibstudio.e_store.data.model.ProductModel
import com.rakibstudio.e_store.databinding.FragmentProductDetailsBinding
import com.bumptech.glide.Glide

class DetailsFragment : Fragment(R.layout.fragment_product_details) {

    private val TAG = "DetailsFragment"

    //Binding
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    //Sent args
    private val args: DetailsFragmentArgs by navArgs()
    private lateinit var selectedProduct: ProductModel

    //Adapter
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedProduct = args.productDetails

        Log.i(TAG, "onViewCreated: $selectedProduct")

        if (selectedProduct.productReviews != null)
            binding.detailsContent.emptyReviewText.visibility = View.GONE

        if (selectedProduct.productStateNew)
            binding.detailsContent.productState.exploreItemNewTag.visibility = View.VISIBLE
        else
            binding.detailsContent.productState.exploreItemNewTag.visibility = View.INVISIBLE

        //For the image, price & cart button
        binding.apply {
            Glide.with(binding.root)
                .load(selectedProduct.productImage)
                .centerCrop()
                .into(productDetailsImage)
            totalPriceText.text = "$" + selectedProduct.productPrice.toString()
            backArrowButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        //For the details and reviews item
        binding.detailsContent.apply {
            productTitleHeadLine.text = selectedProduct.productTitle
            productDescription.text = selectedProduct.productDescription
            productWriteReview.setOnClickListener {
                val writeReviewSheet = WriteReviewFragment()
                writeReviewSheet.show(parentFragmentManager, "BottomSheet")
            }
        }

        //init the reviews
        initReviewRatingRecycler()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun initReviewRatingRecycler() {
        reviewAdapter = ReviewAdapter()
        reviewAdapter.differ.submitList(selectedProduct.productReviews)
        binding.detailsContent.reviewsRecycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = reviewAdapter
            hasFixedSize()
        }
    }
}