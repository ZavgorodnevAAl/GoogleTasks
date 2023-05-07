package ru.zavgorodnev.googletasks.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import ru.zavgorodnev.googletasks.R
import ru.zavgorodnev.googletasks.data.task.Task
import ru.zavgorodnev.googletasks.databinding.BottomSheetFragmentCreateTaskBinding
import ru.zavgorodnev.googletasks.databinding.FragmentMainBinding
import ru.zavgorodnev.googletasks.databinding.PageSelectorDialogBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var taskCreationDialog: BottomSheetDialog
    private lateinit var pageSelectorDialog: BottomSheetDialog
    private val tabTitles = listOf<String>("Избранные", "Все задачи", "Выполненные")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        taskCreationDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        taskCreationDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        makeTaskCreationDialog()

        pageSelectorDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        pageSelectorDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        binding.createItemFab.setOnClickListener{
            taskCreationDialog.show()
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            makePageSelectorDialog()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = ViewPagerAdapter(this)
        binding.categoryViewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(binding.categoryTabLayout, binding.categoryViewPager2) { tab, pos ->
            tab.text = tabTitles[pos]
        }.attach()
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
            val task = Task(
                title = dialogBinding.titleEditText.text.toString(),
                description = dialogBinding.descriptionEditText.text.toString(),
                isFavorite = dialogBinding.addToFavoriteCheckBox.isChecked,
                isCompleted = false,
                parent = null
            )
            viewModel.onAddTaskButtonPressed(task)
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

    private fun makePageSelectorDialog() {
        val dialogBinding = PageSelectorDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        pageSelectorDialog.setContentView(dialogBinding.root)

        val buttons = listOf(
            dialogBinding.favoriteButton,
            dialogBinding.allTasksButton,
            dialogBinding.completedButton
        )

        buttons.forEachIndexed { index, button ->
            if (index == binding.categoryTabLayout.selectedTabPosition)
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
            else
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
        }

        dialogBinding.favoriteButton.setOnClickListener {
            val tab = binding.categoryTabLayout.getTabAt(0)
            tab?.select()
            pageSelectorDialog.dismiss()
        }
        dialogBinding.allTasksButton.setOnClickListener {
            val tab = binding.categoryTabLayout.getTabAt(1)
            tab?.select()
            pageSelectorDialog.dismiss()

        }
        dialogBinding.completedButton.setOnClickListener {
            val tab = binding.categoryTabLayout.getTabAt(2)
            tab?.select()
            pageSelectorDialog.dismiss()
        }

        pageSelectorDialog.show()
    }

}