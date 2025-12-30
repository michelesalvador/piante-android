package org.plantsmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.plantsmap.model.AppRepository
import org.plantsmap.model.Credentials
import org.plantsmap.model.Plant

class AppViewModel(private val appRepository: AppRepository) : ViewModel() {

    val plants = MutableStateFlow<List<Plant>>(emptyList())
    val isLoading = MutableStateFlow(false)
    val selectedPlant = MutableStateFlow<Plant?>(null)
    val user = appRepository.user.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000),
        initialValue = null
    )
    val toastMessage = MutableStateFlow<String?>(null)
    val navigateBack = MutableStateFlow(false)
    private var arePlantsLoaded = false

    fun getPlants() {
        if (arePlantsLoaded) return
        isLoading.value = true
        viewModelScope.launch {
            appRepository.getPlants()
                .onSuccess {
                    plants.value = it
                    arePlantsLoaded = true
                }.onFailure {
                    toastMessage.value = it.message
                }.also {
                    isLoading.value = false
                }
        }
    }

    fun onPlantSelected(plant: Plant?) {
        selectedPlant.value = plant
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            appRepository.login(Credentials(email, password))
                .onSuccess {
                    navigateBack.value = true
                }.onFailure {
                    toastMessage.value = it.message
                }
        }
    }

    fun onNavigationComplete() {
        navigateBack.value = false
    }

    fun logout() {
        viewModelScope.launch {
            user.value?.token?.let { token ->
                appRepository.logout(token)
                    .onSuccess {
                        toastMessage.value = it
                    }.onFailure {
                        toastMessage.value = it.message
                    }
            }
        }
    }

    fun onToastShown() {
        toastMessage.value = null
    }
}