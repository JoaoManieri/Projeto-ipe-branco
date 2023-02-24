package br.com.manieri.ipe_branco.ui.inicialization

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.com.manieri.ipe_branco.MainViewModel
import br.com.manieri.ipe_branco.R
import br.com.manieri.ipe_branco.dataBase.AppDataBase
import br.com.manieri.ipe_branco.databinding.FragmentRegisterBinding
import br.com.manieri.ipe_branco.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebase: FirebaseFirestore

    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        firebaseAuth = FirebaseAuth.getInstance()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.INVISIBLE

        binding.registerButton.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE
            binding.registerButton.isEnabled = false
            binding.emailInputText.isEnabled = false
            binding.nameInputText.isEnabled = false
            binding.passwordInputText.isEnabled = false

            firebaseAuth.createUserWithEmailAndPassword(
                binding.emailInputText.text.toString(),
                binding.passwordInputText.text.toString()
            ).addOnCompleteListener {

                val user = hashMapOf(
                    "name" to binding.nameInputText.text.toString()
                )
                val firebase = FirebaseFirestore.getInstance()

                firebase.collection("UsuariosRoot").document(firebaseAuth.uid.toString()).set(user)
                    .addOnSuccessListener {
                        GlobalScope.launch(Dispatchers.IO) {
                            AppDataBase.getInstance().userDao().addUser(
                                User(
                                    uid = firebaseAuth.uid.toString(),
                                    name = binding.nameInputText.text.toString(),
                                    userName = null
                                )
                            )
                        }

                        binding.progressBar.visibility = View.INVISIBLE
                        binding.registerButton.isEnabled = true
                        binding.emailInputText.isEnabled = true
                        binding.nameInputText.isEnabled = true
                        binding.passwordInputText.isEnabled = true

                        findNavController().navigate(R.id.action_registerFragment_to_userScreenFragment)
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Não foi possivel fazer o cadastro!", Toast.LENGTH_SHORT).show()
                        Log.w(TAG, "onViewCreated: $it", )
                    }
            }
        }
    }
}