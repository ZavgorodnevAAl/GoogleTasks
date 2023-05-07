package ru.zavgorodnev.googletasks.ui.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.zavgorodnev.googletasks.R
import ru.zavgorodnev.googletasks.data.task.Task
import ru.zavgorodnev.googletasks.databinding.FragmentDetailBinding
import java.util.UUID

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskId = requireArguments().getSerializable(ARGUMENT_ID) as UUID
        //load task
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.goBackImageButton.setOnClickListener {
            //save task
            //go back
        }

        binding.favoriteCheckBox.setOnCheckedChangeListener { btn, isChecked ->
            task.isFavorite = isChecked
            btn.setButtonDrawable(if (isChecked) R.drawable.ic_star else R.drawable.ic_star_border)
        }

        binding.deleteImageButton.setOnClickListener {
            Toast.makeText(requireContext(), "Задача удалена", Toast.LENGTH_SHORT).show()
            //go back
        }

        binding.addToCompletedButton.setOnClickListener {
            task.isCompleted = !task.isCompleted
            if (task.isCompleted) {
                //save task
                //go back
            } else {
                renderScreen()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.taskTitleEditText.addTextChangedListener(titleTextWatcher)
        binding.descriptionText.addTextChangedListener(descriptionTextWatcher)
    }

    override fun onStop() {
        super.onStop()
        binding.taskTitleEditText.removeTextChangedListener(titleTextWatcher)
        binding.descriptionText.removeTextChangedListener(descriptionTextWatcher)
    }

    private fun renderScreen() = binding.run {
        taskTitleEditText.setText(task.title)
        descriptionText.setText(task.description)
        favoriteCheckBox.apply {
            isChecked = task.isFavorite
            setButtonDrawable(if (isChecked) R.drawable.ic_star else R.drawable.ic_star_border)
        }
        descriptionText.setText(if (task.isCompleted) R.string.mark_uncompleted else R.string.mark_completed)
    }

    private val titleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) { task.title = s.toString() }
    }

    private val descriptionTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) { task.description = s.toString() }
    }

    companion object {

        private const val ARGUMENT_ID = "argument_id"

        fun newInstance(id: UUID): DetailFragment {
            val arguments = bundleOf()
            ARGUMENT_ID to id
            val fragment = DetailFragment()
            fragment.arguments = arguments

            return fragment
        }
    }
}