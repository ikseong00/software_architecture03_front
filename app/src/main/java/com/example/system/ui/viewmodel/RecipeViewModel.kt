package com.example.system.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.system.data.model.Recipe
import com.example.system.data.repository.RecipeRepository
import com.example.system.ingredientsDB.Ingredient
import com.example.system.data.repository.IngredientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository
) : ViewModel() {

//    private val recipeRepository = RecipeRepository()
//    private val ingredientRepository = IngredientRepository()


    var recipeUiState = MutableStateFlow<List<Recipe>>(emptyList())


    fun getRecipes() {
        viewModelScope.launch {
//            _isLoading.value = true
            try {
                val recipes = recipeRepository.getRecipes()
                recipeUiState.value = recipes
                Log.d("RecipeViewModel", "Error fetching recipes: $recipes")
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching recipes: ${e.message}")
            }
        }
    }

    fun postRecipe(
        ingredients: String,
        name: String,
        description: String
    ) {
        viewModelScope.launch {
            val ingredientList = ingredients.split(",")
                .map {
                    Ingredient(name = it.trim())
                }.toList()

            recipeRepository.registerRecipe(ingredientList, name, description)
        }
    }

    suspend fun modifyRecipe(
        ingredients: List<Ingredient>,
        id: Int,
        name: String,
        description: String
    ) {
        viewModelScope.launch {
            recipeRepository.putRecipe(ingredients, id, name, description)
        }
    }
}