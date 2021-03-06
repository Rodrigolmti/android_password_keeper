package br.com.rodrigolmti.dashboard.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.whenCreated
import br.com.rodrigolmti.core_android.base.BaseFragment
import br.com.rodrigolmti.core_android.extensions.exhaustive
import br.com.rodrigolmti.core_android.extensions.viewModelByFactory
import br.com.rodrigolmti.dashboard.R
import br.com.rodrigolmti.dashboard.ui.DashboardActivity
import br.com.rodrigolmti.core_android.navigation_modes.ImmersiveNavigationMode
import br.com.rodrigolmti.core_android.navigation_modes.NavigationMode
import br.com.rodrigolmti.dashboard.ui.password_generator.PasswordGeneratorViewModel
import kotlinx.android.synthetic.main.settings_fragment.*
import javax.inject.Inject

class SettingsFragment : BaseFragment(), NavigationMode by ImmersiveNavigationMode {

    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SettingsViewModel> { viewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as DashboardActivity).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dispatchViewAction(SettingsAction.Init)
        setupFields()
    }

    private fun setupFields() {
        sBiometric.setOnCheckedChangeListener { _, isChecked ->
            viewModel.dispatchViewAction(SettingsAction.BiometricChanged(isChecked))
        }
        observeChanges()
    }

    private fun observeChanges() {
        viewModel.viewState.action.observe(viewLifecycleOwner, Observer { action ->
            when(action) {
                is SettingsViewState.Action.UpdateBiometricSwitch -> {
                    sBiometric.isChecked = action.checked
                }
            }.exhaustive
        })
    }
}