package org.plantsmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.plantsmap.model.Plant
import org.plantsmap.model.PlantRepository

class MapViewModel : ViewModel() {

    val plantRepository = PlantRepository()
    val plants = MutableStateFlow<List<Plant>>(emptyList())
    val isLoading = MutableStateFlow(false)
    val selectedPlant = MutableStateFlow<Plant?>(null)
    private var arePlantsLoaded = false

    fun getPlants() {
        if (arePlantsLoaded) return
        isLoading.value = true
        viewModelScope.launch {
            try {
                plants.value = plantRepository.getPlants()
                arePlantsLoaded = true
            } finally {
                isLoading.value = false
            }
        }
    }

    fun onPlantSelected(plant: Plant?) {
        selectedPlant.value = plant
    }
}