package com.example.taskmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.navArgument
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ItemTodoBinding
import com.example.taskmanager.model.Note
import com.example.taskmanager.ui.notes.SaveOrDeleteFragmentDirections
import com.example.taskmanager.ui.notes.TodoFragment
import com.example.taskmanager.ui.notes.TodoFragmentArgs
import com.example.taskmanager.ui.notes.TodoFragmentDirections
import com.example.taskmanager.utils.hideKeyboard

import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import org.commonmark.node.SoftLineBreak

class RVNotesAdapter:ListAdapter<Note,RVNotesAdapter.NotesViewHolder>(DiffUtilCallback()){
   inner class NotesViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
         private val contentBinding=ItemTodoBinding.bind(itemView)
         val title:MaterialTextView=contentBinding.noteItemTitle
         val content:TextView=contentBinding.noteContentItem
         val date:MaterialTextView=contentBinding.noteDate
         val parent:MaterialCardView=contentBinding.noteItemLayoutParent
         val markwon=Markwon.builder(itemView.context)
             .usePlugin(StrikethroughPlugin.create())
             .usePlugin(TaskListPlugin.create(itemView.context))
             .usePlugin(object :AbstractMarkwonPlugin(){
                 override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                     super.configureVisitor(builder)
                     builder.on(SoftLineBreak::class.java){
                         visitor,_->visitor.forceNewLine()
                     }
                 }
             }).build()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todo,parent,false
        ))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
       getItem(position).let{note->
           holder.apply {
               parent.transitionName="recyclerView_${note.id}"
               title.text=note.title
               markwon.setMarkdown(content,note.content)
               date.text=note.date
               parent.setCardBackgroundColor(note.color)

               itemView.setOnClickListener {
                   var action=TodoFragmentDirections.Companion.actionNavigationTodoToNavigationSaveOrDelete(note)
                   val extras= FragmentNavigatorExtras(parent to "recyclerView_${note.id}")
                   it.hideKeyboard()
                   Navigation.findNavController(it).navigate(action,extras)


               }
               content.setOnClickListener {
                   var action=TodoFragmentDirections.Companion.actionNavigationTodoToNavigationSaveOrDelete(note)
                   val extras= FragmentNavigatorExtras(parent to "recyclerView_${note.id}")
                   it.hideKeyboard()
                   Navigation.findNavController(it).navigate(action,extras)
               }

           }

       }
    }
}
