package br.com.manieri.ipe_branco.ui.main.Discussion

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.manieri.ipe_branco.R
import br.com.manieri.ipe_branco.databinding.FragmentQuestionBinding
import br.com.manieri.ipe_branco.model.structure.Discussion
import br.com.manieri.ipe_branco.ui.adapters.DiscutionAdapter
import br.com.manieri.ipe_branco.util.Constants.Companion.QUESTION
import br.com.manieri.ipe_branco.util.Constants.Companion.RESPONSE

class DiscussionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainLayoutManager: RecyclerView.LayoutManager
    private lateinit var mainAdapter: RecyclerView.Adapter<DiscutionAdapter.ViewHolder>

    companion object {
        fun newInstance() = DiscussionFragment()
    }

    private lateinit var viewModel: DiscussionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this)[DiscussionViewModel::class.java]

        mainLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.questionsAndResponses.layoutManager = mainLayoutManager

        viewModel.createDiscussion()

        viewModel.getDataDiscussion.observe(viewLifecycleOwner){
            mainAdapter = DiscutionAdapter(it)
            binding.questionsAndResponses.adapter = mainAdapter
        }

        binding.backScreenImageButon.setOnClickListener {
            findNavController().navigate(R.id.action_discussionFragment_to_userScreenFragment)
        }

        binding.responseButton.setOnClickListener {
            findNavController().navigate(R.id.action_userScreenFragment_to_discussionFragment)
        }

    }


}