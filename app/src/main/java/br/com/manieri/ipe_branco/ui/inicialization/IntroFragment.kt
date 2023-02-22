package br.com.manieri.ipe_branco.ui.inicialization

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.manieri.ipe_branco.R
import br.com.manieri.ipe_branco.dataBase.AppDataBase
import br.com.manieri.ipe_branco.databinding.FragmentIntroBinding


class IntroFragment : Fragment() {

    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        val view = binding.root

//        binding.linkDevs.setOnClickListener {
//            // Inflate the layout for this fragment
//            val url = "https://www.linkedin.com/in/jo%C3%A3o-victor-manieri-019328177/"
//            val i = Intent(Intent.ACTION_VIEW)
//            i.data = Uri.parse(url)
//            startActivity(i)
//        }
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                val saida = AppDataBase.getInstance().userDao().getToken().isNullOrEmpty()
                Log.w("TAG", "run: ${saida}", )
                if (!saida
                ) findNavController().navigate(R.id.action_introFragment_to_userScreenFragment)
                else findNavController().navigate(R.id.action_introFragment_to_mainFragment)
            }
        }, 2000)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}