package club.androidexpress.nutritionapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import club.androidexpress.nutritionapp.databinding.ActivityMainBinding
import club.androidexpress.nutritionapp.views.DialogInput
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        refresh()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> refresh()
            else -> DialogInput().apply {
                dialogListener = {
                    refresh()
                }
                show(supportFragmentManager, item.title.toString())
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun refresh() {
        binding.pbMain.visibility = View.VISIBLE

        FirebaseFirestore.getInstance().collection("datas").get().addOnSuccessListener { result ->
            for (documentSnapshot in result) {
                val value = documentSnapshot.data["value"] as Long
                val goal = documentSnapshot.data["goal"] as Long

                val percent: Float = value.toFloat() * 100 / goal.toFloat()
                val text = "$value / $goal"

                when (documentSnapshot.id) {
                    getString(R.string.calories) -> run {
                        with(binding) {
                            txtScoreCalories.text = text
                            progress1.setValue(percent.toInt())
                        }
                    }

                    getString(R.string.carbohydrates) -> run {
                        with(binding) {
                            txtScoreCarbohydrates.text = text
                            progress2.setValue(percent.toInt())
                        }
                    }

                    getString(R.string.proteins) -> run {
                        with(binding) {
                            txtScoreProteins.text = text
                            progress3.setValue(percent.toInt())
                        }
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Log.d("TAG", exception.message.toString(), exception)
        }.addOnCompleteListener {
            binding.pbMain.visibility = View.GONE
        }
    }
}