package br.com.manieri.ipe_branco.ui.main.Discussion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.manieri.ipe_branco.R

import br.com.manieri.ipe_branco.databinding.FragmentInitDiscossionBinding
import br.com.manieri.ipe_branco.ui.main.MainFragmentViewModel


class InitDiscussionFragment : Fragment() {

    private var _binding: FragmentInitDiscossionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DiscussionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInitDiscossionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val assuntos = arrayOf("Escolha um problema","Produto Tecnomtor", "Codigo de defeito", "Problema mecanico", "problemas eletricos")
        binding.spinnerAssunto
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, assuntos)
        binding.spinnerAssunto.adapter = adapter


        viewModel = ViewModelProvider(this)[DiscussionViewModel::class.java]

        binding.sendQuestion.setOnClickListener {
            val out = viewModel.newDiscussion(binding.editTextQuestion.text.toString(), binding.titleInputTextNewDiscussion.text.toString())
            findNavController().navigate(R.id.action_initDiscossionFragment_to_userScreenFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}