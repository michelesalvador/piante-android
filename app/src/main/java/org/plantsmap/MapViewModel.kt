package org.plantsmap

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.plantsmap.model.Plant
import org.plantsmap.model.PlantRepository

class MapViewModel : ViewModel() {

    val plantRepository = PlantRepository()
    val plants = MutableStateFlow<List<Plant>>(emptyList())
    val isLoading = MutableLiveData(false)

    fun getPlants() {
        isLoading.value = true
        viewModelScope.launch {
            plants.value = plantRepository.getPlants()
            isLoading.value = false
        }
    }
}