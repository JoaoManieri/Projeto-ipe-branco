package br.com.manieri.ipe_branco.ui.main

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.manieri.ipe_branco.R
import br.com.manieri.ipe_branco.dataBase.AppDataBase
import br.com.manieri.ipe_branco.databinding.FragmentMainBinding
import br.com.manieri.ipe_branco.model.structure.Discussion
import br.com.manieri.ipe_branco.model.structure.Question
import br.com.manieri.ipe_branco.model.structure.TopSearches
import br.com.manieri.ipe_branco.ui.adapters.HorizontalAdapter
import br.com.manieri.ipe_branco.ui.adapters.QuestionsAdapter
import br.com.manieri.ipe_branco.ui.main.Discussion.DiscussionViewModel
import kotlin.math.log


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mainLayoutManager: RecyclerView.LayoutManager
    private lateinit var horizontalAdapter: RecyclerView.Adapter<HorizontalAdapter.ViewHolder>
    private lateinit var mainAdapter: RecyclerView.Adapter<QuestionsAdapter.ViewHolder>

    private lateinit var viewModelDiscussion: DiscussionViewModel
    private lateinit var viewModel: MainFragmentViewModel

    var selectCard = MutableLiveData<Discussion>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val list = arrayListOf<TopSearches>(
            TopSearches(0, "P1900"),
            TopSearches(1, "Ventoinha"),
            TopSearches(2, "Rasther"),
            TopSearches(3, "Scanner"),
            TopSearches(4, "Cebolão")
        )

        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.topSearchesReciclerView.layoutManager = layoutManager

        horizontalAdapter = HorizontalAdapter(list)
        binding.topSearchesReciclerView.adapter = horizontalAdapter

        viewModelDiscussion = ViewModelProvider(this)[DiscussionViewModel::class.java]
        viewModel = ViewModelProvider(this)[MainFragmentViewModel::class.java]

        var mAdapter = QuestionsAdapter(arrayListOf(), selectCard)
        mainAdapter = mAdapter
        binding.allNotices.adapter = mainAdapter

        viewModel.ObserverListQuestion.observe(viewLifecycleOwner) {
            Log.w(TAG, "onViewCreated: Entrou")
            mAdapter.update(it)
        }

        viewModel.getQuestionList()

        mainLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.allNotices.layoutManager = mainLayoutManager

        binding.logoutButton.setOnClickListener {
            AppDataBase.getInstance().userDao().clearData()
            findNavController().navigate(R.id.action_userScreenFragment_to_mainFragment)
        }

        binding.nameUserLabel.text = "Olá, ${AppDataBase.getInstance().userDao().getName()}!"

        selectCard.observe(viewLifecycleOwner) {
            DiscussionViewModel.discussion = it
            Log.w(TAG, "onViewCreated: Depois do card selecionado", )
            findNavController().navigate(R.id.action_userScreenFragment_to_discussionFragment)
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_userScreenFragment_to_initDiscossionFragment)
        }

    }

}