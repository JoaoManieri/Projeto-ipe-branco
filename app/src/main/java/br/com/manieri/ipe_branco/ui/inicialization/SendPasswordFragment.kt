package br.com.manieri.ipe_branco.ui.inicialization

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.manieri.ipe_branco.R
import br.com.manieri.ipe_branco.databinding.FragmentSendPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class SendPasswordFragment : Fragment() {

    private var _binding: FragmentSendPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {
        _binding = FragmentSendPasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.sendEmail.setOnClickListener {

            firebaseAuth.sendPasswordResetEmail(binding.emailInputText.text.toString())
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(), "e-mail enviado",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_sendPasswordFragment_to_mainFragment)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(), "Falha ao enviar e-mail",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}