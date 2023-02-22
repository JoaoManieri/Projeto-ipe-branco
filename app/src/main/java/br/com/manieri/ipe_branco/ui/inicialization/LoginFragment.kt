package br.com.manieri.ipe_branco.ui.inicialization

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import br.com.manieri.ipe_branco.MainViewModel
import br.com.manieri.ipe_branco.R
import br.com.manieri.ipe_branco.dataBase.AppDataBase
import br.com.manieri.ipe_branco.databinding.FragmentLoginBinding
import br.com.manieri.ipe_branco.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var navController: NavController

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.loginButton.setOnClickListener {

            val email = binding.emailInputText.text
            val passowd = binding.passwordInputText.text
            var userName = MutableLiveData<String>()
            val user = firebaseAuth.currentUser?.uid

            //GlobalScope.launch(Dispatchers.IO) {
                firebaseAuth.signInWithEmailAndPassword(email.toString(), passowd.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            ////////////////////////////////////////////////////// ADD BANCO DE DADOS
                            getNameFireBase(firebaseAuth.uid.toString(), firestore)
                            findNavController().navigate(R.id.action_mainFragment_to_userScreenFragment)
                        } else {
                            Toast.makeText(requireContext(), "Senha ou usuario errado", Toast.LENGTH_SHORT).show()
                        }
                    }
            //}
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_registerFragment)
        }

        binding.recoverPassword.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_sendPasswordFragment)
        }

    }

    private fun getNameFireBase(userToken: String, firestore: FirebaseFirestore) {
        firestore.collection("UsuariosRoot").document(userToken).addSnapshotListener { value, error ->
            AppDataBase.getInstance().userDao().addUser(
                User(
                    uid = firebaseAuth.uid.toString(),
                    name = value?.getString("name").toString(),
                    userName = null
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}



//Log.w(TAG, "onViewCreated: ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", )
//firestore.collection("UsuariosRoot").document(
//user.toString()
//).addSnapshotListener { value, error ->
//    Log.w(TAG, "onViewCreated: ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ", )
//    userName.postValue(value?.getString("name").toString())
//}

//userName.observe(viewLifecycleOwner){
//
//}