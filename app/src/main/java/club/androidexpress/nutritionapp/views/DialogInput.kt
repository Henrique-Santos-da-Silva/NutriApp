package club.androidexpress.nutritionapp.views

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import club.androidexpress.nutritionapp.R
import club.androidexpress.nutritionapp.databinding.DialogInputBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalArgumentException
import java.util.HashMap

class DialogInput : DialogFragment() {
    private var _binding: DialogInputBinding? = null
    private val binding: DialogInputBinding? get() = _binding

    lateinit var dialogListener: () -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            _binding = DialogInputBinding.inflate(LayoutInflater.from(context))
            val builder = AlertDialog.Builder(it)
            builder.setView(binding?.root)
                .setPositiveButton(R.string.save) { _, _ ->
                    binding?.let { dialogInput ->
                        with(dialogInput) {
                            save(edtValue.text.toString().toInt(), edtGoal.text.toString().toInt())
                        }
                    }

                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog?.cancel()
                }

            builder.create()
        } ?: throw IllegalArgumentException("error")
    }

    private fun save(value: Int, goal: Int) {
        val data: HashMap<String, Int> = hashMapOf("value" to value, "goal" to goal)
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        tag?.let {
            db.collection("datas").document(it).set(data).addOnSuccessListener {
                dialogListener.invoke()
            }.addOnFailureListener { e ->
                Log.d("TAG", e.message.toString(), e)
            }
        }
    }
}