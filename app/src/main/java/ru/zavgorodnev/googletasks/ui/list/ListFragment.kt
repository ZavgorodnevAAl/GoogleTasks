package ru.zavgorodnev.googletasks.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.zavgorodnev.googletasks.data.task.Task
import ru.zavgorodnev.googletasks.databinding.FragmentListBinding
import java.util.UUID

class ListFragment : Fragment(), TaskItemListener {

    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by activityViewModels()
    private val adapter = TasksAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.tasksRecyclerView.adapter = adapter
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (requireArguments().getString(ARGUMENTS_KEY) as String) {
            FAVORITES_TYPE -> viewModel.favoriteTasks.observe(viewLifecycleOwner) {
                adapter.setTasks(it)
            }
            ALL_TYPE -> viewModel.tasks.observe(viewLifecycleOwner) {
                adapter.setTasks(it)
            }
            COMPLETED_TYPE -> viewModel.completedTasks.observe(viewLifecycleOwner) {
                adapter.setTasks(it)
            }
        }

    }

    companion object {

        const val FAVORITES_TYPE = "favorite_type"
        const val ALL_TYPE = "all_type"
        const val COMPLETED_TYPE = "completed_type"
        private const val ARGUMENTS_KEY = "arguments_key"
        fun newInstance(type: String): ListFragment {
            val arguments = bundleOf(
                ARGUMENTS_KEY to type
            )
            val fragment = ListFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onClickTask(taskId: UUID) {
        Toast.makeText(requireContext(), "Launch task screen", Toast.LENGTH_SHORT).show()
    }

    override fun onFavoriteButtonPressed(task: Task) {
        viewModel.isFavoriteButtonPressed(task)
    }

    override fun onCompletedButtonPressed(task: Task) {
        viewModel.isCompletedButtonPressed(task)
    }
}