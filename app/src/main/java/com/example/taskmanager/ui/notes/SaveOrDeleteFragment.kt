package com.example.taskmanager.ui.notes

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.example.taskmanager.database.NoteDatabase
import com.example.taskmanager.databinding.BottomSheetDialogTodoBinding
import com.example.taskmanager.databinding.FragmentSaveOrDeleteBinding
import com.example.taskmanager.model.Note
import com.example.taskmanager.repository.NoteRepository
import com.example.taskmanager.utils.hideKeyboard
import com.example.taskmanager.viewmodel.NoteViewModel
import com.example.taskmanager.viewmodel.NoteViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class SaveOrDeleteFragment : Fragment(R.layout.fragment_save_or_delete) {

    private lateinit var navController: NavController
    private lateinit var contentBinding: FragmentSaveOrDeleteBinding
    var note: Note? = null
    private var color = -1
    private lateinit var result: String
    private val noteViewModel: NoteViewModel by activityViewModels()
    private val currentDate = SimpleDateFormat.getDateInstance().format(Date())
    private val job = CoroutineScope(Dispatchers.Main)
    private val args: SaveOrDeleteFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostFragment
            scrimColor = Color.TRANSPARENT
            duration = 300L
        }
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentBinding= FragmentSaveOrDeleteBinding.bind(view)
        navController=Navigation.findNavController(view)
        val activity= activity as MainActivity

        ViewCompat.setTransitionName(
            contentBinding.noteContentFragmentParent,
            "recyclerView_${args.note?.id}"
        )

        contentBinding.backButton.setOnClickListener {
            requireView().hideKeyboard()
            navController.popBackStack()
        }


            contentBinding.saveNotes.setOnClickListener {
                saveNote()
            }

        try {
            contentBinding.etNoteContent.setOnFocusChangeListener{_,hasFocus->
                if (hasFocus){
                    contentBinding.bottomBar.visibility=View.VISIBLE
                    contentBinding.etNoteContent.setStylesBar(contentBinding.styleBar)
                }else{
                    contentBinding.bottomBar.visibility=View.GONE
                }

            }
        }catch (e:Throwable){
            Log.d("TAG",e.stackTrace.toString())
        }

        contentBinding.fabColorPick.setOnClickListener {
            val bottomSheetDialog=BottomSheetDialog(
                requireContext(),
                R.style.BottomSheetDialogTheme
            )
            val bottomSheetView:View=layoutInflater.inflate(
                R.layout.bottom_sheet_dialog_todo,
                null
            )
            with(bottomSheetDialog){
                setContentView(bottomSheetView)
                show()
            }

            val bottomSheetBinding=BottomSheetDialogTodoBinding.bind(bottomSheetView)
            bottomSheetBinding.apply {
                colorPicker.apply {
                    setSelectedColor(color)
                    setOnColorSelectedListener { value->
                        color=value
                        contentBinding.apply {
                            noteContentFragmentParent.setBackgroundColor(color)
                            toolBarFragmentNoteContent.setBackgroundColor(color)
                            bottomBar.setBackgroundColor(color)
                            activity.window.statusBarColor=color
                        }
                        bottomSheetBinding.bottomSheetParent.setCardBackgroundColor(color)
                    }
                }
                bottomSheetParent.setCardBackgroundColor(color)
            }
            bottomSheetView.post {
                bottomSheetDialog.behavior.state=BottomSheetBehavior.STATE_EXPANDED
            }
        }
        setupNotes()
    }

    private fun setupNotes() {
        val note = args.note
        val title=contentBinding.etTitle
        val content=contentBinding.etNoteContent
        val lastEdited=contentBinding.lastEditedDate

        if (note==null){
            contentBinding.lastEditedDate.text=getString(R.string.edited_on,SimpleDateFormat.getDateInstance().format(Date()))
        }
        if (note!=null){
            title.setText(note.title)
            content.renderMD(note.content)
            lastEdited.text=getString(R.string.edited_on,note.date)
            color=note.color
            contentBinding.apply {
                job.launch {
                    delay(10)
                    noteContentFragmentParent.setBackgroundColor(color)
                }
                toolBarFragmentNoteContent.setBackgroundColor(color)
                bottomBar.setBackgroundColor(color)
            }
            activity?.window?.statusBarColor=note.color
        }
    }

    private fun saveNote() {
        if (contentBinding.etNoteContent.text.toString().isEmpty() || contentBinding.etTitle.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Something is Empty", Toast.LENGTH_SHORT).show()
        } else {
            note = args.note
            when (note) {
                null -> {
                    noteViewModel.saveNote(
                        Note(
                            0,
                            contentBinding.etTitle.text.toString(),
                            contentBinding.etNoteContent.getMD(),
                            currentDate,
                            color
                        )
                    )
                    result = "Note Saved"
                    setFragmentResult(
                        "key",
                        bundleOf("bundleKey" to result)
                    )
                    navController.navigate(SaveOrDeleteFragmentDirections.actionNavigationSaveOrDeleteToNavigationTodo())
                }
                else -> {
                    // update notes
                    updateNote()
                    navController.popBackStack()
                }
            }
        }
    }

    private fun updateNote() {
       if (note!=null){
           noteViewModel.updateNote(
               Note(
                   note!!.id,
                   contentBinding.etTitle.text.toString(),
                   contentBinding.etNoteContent.getMD(),
                   currentDate,
                   color
               )
           )
        }
    }
}