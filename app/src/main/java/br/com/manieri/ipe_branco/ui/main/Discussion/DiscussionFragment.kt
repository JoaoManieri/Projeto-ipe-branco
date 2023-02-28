package br.com.manieri.ipe_branco.ui.main.Discussion

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.manieri.ipe_branco.R
import br.com.manieri.ipe_branco.databinding.FragmentQuestionBinding
import br.com.manieri.ipe_branco.databinding.ResponseBottonSheetBinding
import br.com.manieri.ipe_branco.ui.adapters.DiscutionAdapter
import br.com.manieri.ipe_branco.util.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog

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

        createAdapter()

        crateList()


        binding.backScreenImageButon.setOnClickListener {
            findNavController().navigate(R.id.action_discussionFragment_to_userScreenFragment)
        }

        binding.menuImageButon.setOnClickListener {
            showPopup(binding.menuImageButon)
        }

        binding.responseButton.setOnClickListener {
            showButtonDialogResponse(mainAdapter)
        }


    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.menu_discussion)


        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.option_1 -> {
                    Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                }
                R.id.option_2 -> {
                    Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                }
                R.id.option_3 -> {
                    Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                }
            }
            true
        })

        popup.show()
    }

    fun crateList() {
        viewModel.createDiscussion()
        viewModel.getDataDiscussion.observe(viewLifecycleOwner) {

            it.forEach {
                Log.w("Depuração leituras", "onActivityCreated: ${it.body_question}")

            }
            mainAdapter = DiscutionAdapter(it, viewModel)
            binding.questionsAndResponses.adapter = mainAdapter
        }
    }


    fun createAdapter() {
        mainLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.questionsAndResponses.layoutManager = mainLayoutManager
    }

    private fun showButtonDialogResponse(mainAdapter: RecyclerView.Adapter<DiscutionAdapter.ViewHolder>) {

        val dialog = BottomSheetDialog(requireContext(), R.style.BottonSheetDialog).apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
        val sheetBinding: ResponseBottonSheetBinding =
            ResponseBottonSheetBinding.inflate(layoutInflater, null, false)

        val sendResponse = sheetBinding.buttonSendResponse
        sendResponse.setOnClickListener {

            //função add comentarios
            viewModel.newDiscussion(
                sheetBinding.responseInputText.text.toString(), "noTitle-responseObject",
                Constants.RESPONSE
            )

            mainAdapter.notifyDataSetChanged()

            dialog.dismiss()
        }

        dialog.setContentView(sheetBinding.root)
        dialog.show()

    }

    override fun onDestroy() {
        super.onDestroy()
        findNavController().navigate(R.id.action_discussionFragment_to_userScreenFragment)
    }


}