package com.example.taskmanager.ui.notes

// TodoFragment.kt

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.adapters.RVNotesAdapter
import com.example.taskmanager.databinding.FragmentTodoBinding
import com.example.taskmanager.utils.SwipeToDelete
import com.example.taskmanager.utils.hideKeyboard
import com.example.taskmanager.viewmodel.NoteViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class TodoFragment : Fragment(R.layout.fragment_todo){
    private lateinit var binding: FragmentTodoBinding
    private val noteViewModel: NoteViewModel by activityViewModels()
    private lateinit var rvAdapter:RVNotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        exitTransition=MaterialElevationScale(false).apply {
            duration=350
        }
        enterTransition=MaterialElevationScale(true).apply {
            duration=1000
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentTodoBinding.bind(view)
        val navController=Navigation.findNavController(view)
        requireView().hideKeyboard()
        CoroutineScope(Dispatchers.Main).launch {
            delay(10)
           // activity?.window?.statusBarColor=Color.WHITE
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity?.window?.statusBarColor=Color.parseColor("#9E9D9D")
        }

        binding.noteFab.setOnClickListener{
            binding.appBarLayout.visibility=View.INVISIBLE
            navController.navigate(TodoFragmentDirections.actionNavigationTodoToNavigationSaveOrDelete())
        }

        binding.innerFab.setOnClickListener{
            binding.appBarLayout.visibility=View.INVISIBLE
            navController.navigate(TodoFragmentDirections.actionNavigationTodoToNavigationSaveOrDelete())
        }

        recyclerViewDisplay()
        swipeToDelete(binding.rvNote)

        binding.search.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.noData.isVisible=false
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
               if (toString().isNotEmpty()){
                   val text =s.toString()
                   val query = "%$text%"
                   if(query.isNotEmpty()){
                       noteViewModel.searchNote(query).observe(viewLifecycleOwner)
                       {
                          rvAdapter.submitList(it)
                       }
                   }
                   else{
                       observerDataChanges()
                   }
               }
                else{
                    observerDataChanges()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.search.setOnEditorActionListener{v, actionId, _->
            if (actionId.equals(EditorInfo.IME_ACTION_SEARCH)) {
                v.clearFocus()
                requireView().hideKeyboard()
            }
            return@setOnEditorActionListener true
        }


        binding.rvNote.setOnScrollChangeListener { _, scrollX, scrollY,_, oldScrollY ->

            when{
                scrollY>oldScrollY->{
                    binding.checkFabText.isVisible=false
                }
                scrollX==scrollY->{
                    binding.checkFabText.isVisible=true
                }
                else->{
                    binding.checkFabText.isVisible=true
                }
            }
        }



    }

    private fun swipeToDelete(rvNote: RecyclerView) {
        val swipeToDeleteCallback=object :SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.absoluteAdapterPosition
                val note=rvAdapter.currentList[position]
                var actionButtonTapped=false
                noteViewModel.deleteNote(note)
                binding.search.apply {
                    hideKeyboard()
                    clearFocus()
                }
                if (binding.search.text.toString().isEmpty()){
                    observerDataChanges()
                }
                val snackBar=Snackbar.make(
                    requireView(),
                    "Note Deleted",Snackbar.LENGTH_LONG
                ).addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                    }

                    override fun onShown(transientBottomBar: Snackbar?) {
                        transientBottomBar?.setAction("UNDO"){
                            noteViewModel.saveNote(note)
                            actionButtonTapped=true
                            binding.noData.isVisible=false
                        }
                        super.onShown(transientBottomBar)

                    }
                }).apply {
                    animationMode=Snackbar.ANIMATION_MODE_FADE
                    setAnchorView(R.id.note_fab)
                }
                snackBar.setActionTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.yellowOrange
                    )
                )
                snackBar.show()
            }

        }

        val itemTouchHelper=ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvNote)

    }

    private fun observerDataChanges() {
        noteViewModel.getAllNotes().observe(viewLifecycleOwner){list->
            binding.noData.isVisible=list.isEmpty()
            rvAdapter.submitList(list)
        }
    }

    private fun recyclerViewDisplay() {
       when(resources.configuration.orientation){
           Configuration.ORIENTATION_PORTRAIT->setUpRecyclerView(2)
           Configuration.ORIENTATION_LANDSCAPE->setUpRecyclerView(3)
       }
    }

    private fun setUpRecyclerView(spanCount: Int) {

        binding.rvNote.apply {
            layoutManager=StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
           rvAdapter= RVNotesAdapter()
            rvAdapter.stateRestorationPolicy=
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter=rvAdapter
            postponeEnterTransition(300L,TimeUnit.MICROSECONDS)
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
        observerDataChanges()

    }
}

