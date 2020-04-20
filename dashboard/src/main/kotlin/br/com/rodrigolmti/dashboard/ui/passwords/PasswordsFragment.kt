package br.com.rodrigolmti.dashboard.ui.passwords

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.rodrigolmti.core_android.base.BaseFragment
import br.com.rodrigolmti.core_android.extensions.exhaustive
import br.com.rodrigolmti.core_android.navigation_modes.ImmersiveNavigationMode
import br.com.rodrigolmti.core_android.navigation_modes.NavigationMode
import br.com.rodrigolmti.dashboard.R
import br.com.rodrigolmti.dashboard.domain.model.SavedPasswordModel
import br.com.rodrigolmti.dashboard.ui.DashboardActivity
import br.com.rodrigolmti.dashboard.ui.passwords.PasswordsViewState.State.*
import br.com.rodrigolmti.uikit.hide
import br.com.rodrigolmti.uikit.show
import kotlinx.android.synthetic.main.password_generator_fragment.lottie
import kotlinx.android.synthetic.main.password_generator_fragment.recyclerView
import kotlinx.android.synthetic.main.passwords_fragment.*

class PasswordsFragment : BaseFragment(), NavigationMode by ImmersiveNavigationMode {

    private val viewModel by lazy { getViewModel(PasswordsViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.passwords_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as DashboardActivity).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dispatchViewAction(PasswordsAction.Init)
        setupFields()
    }

    private fun setupFields() {
        fBtnAdd.setOnClickListener {
            PasswordsFragmentDirections.actionPasswordsToPassword().also { navDirection ->
                findNavController().navigate(navDirection)
            }
        }
        setupRecyclerView()
        observeChanges()
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            val dividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                LinearLayoutManager.VERTICAL
            )
            ContextCompat.getDrawable(requireContext(), R.drawable.item_divisor)?.let { drawable ->
                dividerItemDecoration.setDrawable(drawable)
            }
            recyclerView.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun observeChanges() {
        viewModel.viewState.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                IDLE -> {
                    toIdleState()
                }
                LOADING -> {
                    toLoadingState()
                }
                ERROR -> {
                    toErrorState()
                }
                EMPTY_LIST -> {
                    toEmptyState()
                }
            }.exhaustive
        })
        viewModel.viewState.action.observe(viewLifecycleOwner, Observer { action ->
            when (action) {
                is PasswordsViewState.Action.ShowSavedPasswordList -> {
                    setupAdapter(action.passwords)
                }
                is PasswordsViewState.Action.GetSavedPasswordsError -> {

                }
            }.exhaustive
        })
    }

    private fun setupAdapter(passwords: List<SavedPasswordModel>) {
        recyclerView.apply {
            adapter = SavedPasswordsAdapter(
                password = passwords,
                onItemClick = {
                    PasswordsFragmentDirections.actionPasswordsToPassword(
                        savedPasswordModel = it
                    ).also { navDirection ->
                        findNavController().navigate(navDirection)
                    }
                },
                onCopyLoginClick = { model ->

                },
                onCopyPasswordClick = { model ->

                })
        }
    }

    private fun toIdleState() {
        lottie.hide()
        recyclerView.show()
        imgVoid.hide()
        tvVoid.hide()
    }

    private fun toLoadingState() {
        lottie.show()
        recyclerView.hide()
        imgVoid.hide()
        tvVoid.hide()
    }

    private fun toErrorState() {
        lottie.hide()
        recyclerView.hide()
        imgVoid.hide()
        tvVoid.hide()
    }

    private fun toEmptyState() {
        lottie.hide()
        recyclerView.hide()
        imgVoid.show()
        tvVoid.show()
    }
}