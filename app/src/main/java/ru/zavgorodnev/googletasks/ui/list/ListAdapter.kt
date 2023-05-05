package ru.zavgorodnev.googletasks.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.zavgorodnev.googletasks.R
import ru.zavgorodnev.googletasks.data.task.Task
import ru.zavgorodnev.googletasks.databinding.ItemTaskBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var tasks: MutableList<Task> = mutableListOf()

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemTaskBinding.bind(view)

        fun bind(task: Task) = with(binding) {
            titleTextView.text = task.title

            if (task.description.isNotBlank()) {
                descriptionTextView.visibility = View.VISIBLE
                descriptionTextView.text = task.description
            } else {
                descriptionTextView.visibility = View.GONE
            }

            isCompletedCheckBox.isChecked = task.isCompleted
            val imageRes = if (task.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
            isFavoriteImageButton.setImageResource(imageRes)

            root.setOnClickListener {
                Toast.makeText(root.context, "Открыть задачу ${task.title}", Toast.LENGTH_SHORT).show()
            }

            isCompletedCheckBox.setOnClickListener {
                task.isCompleted =isCompletedCheckBox.isChecked
            }

            isFavoriteImageButton.setOnClickListener {
                task.isFavorite = !task.isFavorite
                val image = if (task.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
                isFavoriteImageButton.setImageResource(image)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {

        return tasks.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks.toMutableList()
    }
}