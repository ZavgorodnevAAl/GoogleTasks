package ru.zavgorodnev.googletasks.ui.detail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.zavgorodnev.googletasks.R
import ru.zavgorodnev.googletasks.data.task.Task
import ru.zavgorodnev.googletasks.databinding.BottomSheetFragmentCreateTaskBinding
import ru.zavgorodnev.googletasks.databinding.FragmentDetailBinding
import ru.zavgorodnev.googletasks.ui.list.TasksAdapter
import ru.zavgorodnev.googletasks.utils.Navigator
import java.util.UUID

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by activityViewModels()
    private lateinit var task: Task
    private var navigator: Navigator? = null
    private val subtaskAdapter = TasksAdapter(null)
    private lateinit var taskCreationDialog: BottomSheetDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator?
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskId = requireArguments().getSerializable(ARGUMENT_ID) as UUID
        viewModel.load(taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.goBackImageButton.setOnClickListener {
            viewModel.save(task)
            navigator?.goBack()
        }

        binding.favoriteCheckBox.setOnCheckedChangeListener { btn, isChecked ->
            task.isFavorite = isChecked
            btn.setButtonDrawable(if (isChecked) R.drawable.ic_star else R.drawable.ic_star_border)
        }

        binding.deleteImageButton.setOnClickListener {
            Toast.makeText(requireContext(), "Задача удалена", Toast.LENGTH_SHORT).show()
            viewModel.delete(task)
            navigator?.goBack()
        }

        binding.addToCompletedButton.setOnClickListener {
            task.isCompleted = !task.isCompleted
            if (task.isCompleted) {
                viewModel.save(task)
                navigator?.goBack()
            } else {
                renderScreen()
            }
        }

        taskCreationDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        taskCreationDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        makeTaskCreationDialog()

        binding.addSubtaskImageButton.setOnClickListener {
            taskCreationDialog.show()
        }

        binding.subtasksRecyclerView.adapter = subtaskAdapter
        binding.subtasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.task.observe(viewLifecycleOwner) {
            if (it != null) {
                task = it
                renderScreen()
            }
        }

        viewModel.subtasks.observe(viewLifecycleOwner) {
            subtaskAdapter.setTasks(it)
        }
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
        addToCompletedButton.setText(if (task.isCompleted) R.string.mark_uncompleted else R.string.mark_completed)
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

    private fun makeTaskCreationDialog() {
        val dialogBinding = BottomSheetFragmentCreateTaskBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        dialogBinding.saveTaskButton.isEnabled = false

        dialogBinding.descriptionImageButton.setOnClickListener {
            dialogBinding.descriptionEditText.visibility = View.VISIBLE
        }
        dialogBinding.addToFavoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            dialogBinding.addToFavoriteCheckBox.setButtonDrawable(
                if (isChecked) R.drawable.ic_star else R.drawable.ic_star_border
            )
        }

        dialogBinding.titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                dialogBinding.saveTaskButton.isEnabled = s?.isNotBlank() == true
            }
        })

        dialogBinding.saveTaskButton.setOnClickListener {
            val subtask = Task(
                title = dialogBinding.titleEditText.text.toString(),
                description = dialogBinding.descriptionEditText.text.toString(),
                isFavorite = dialogBinding.addToFavoriteCheckBox.isChecked,
                isCompleted = false,
                parent = task.id
            )
            viewModel.addSubtask(subtask)
            taskCreationDialog.dismiss()
        }
        dialogBinding.titleEditText.requestFocus()

        taskCreationDialog.setOnDismissListener {
            dialogBinding.apply {
                titleEditText.text.clear()
                descriptionEditText.text.clear()
                descriptionEditText.visibility = View.GONE
                addToFavoriteCheckBox.isChecked = false
            }
        }

        taskCreationDialog.setContentView(dialogBinding.root)
    }

    companion object {

        private const val ARGUMENT_ID = "argument_id"

        fun newInstance(id: UUID): DetailFragment {
            val arguments = bundleOf(ARGUMENT_ID to id)
            val fragment = DetailFragment()
            fragment.arguments = arguments

            return fragment
        }
    }
}