package com.example.system.ui.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.system.data.model.Recipe
import com.example.system.ingredientsDB.Ingredient
import com.example.system.ui.component.ForceLandscapeOrientation
import com.example.system.ui.component.LeftScreen
import com.example.system.ui.viewmodel.RecipeViewModel

@Composable
fun EditRecipeScreen(
    navController: NavHostController,
    viewModel: RecipeViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getRecipes()
    }

    val recipes = viewModel.recipeUiState.collectAsState().value

    ForceLandscapeOrientation()
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        LeftScreen(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.LightGray),
            navController = navController
        )

        CenterRecipeEditScreen(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.White),
            recipes = recipes,
            onButtonClick = { name, ingredients, description ->
                viewModel.postRecipe(ingredients, name, description)
            }
        )
    }
}

@Composable
fun CenterRecipeEditScreen(
    modifier: Modifier = Modifier,
    recipes: List<Recipe>,
    onButtonClick: (String, String, String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 화면 상단 제목
        Text(
            text = "레시피 수정",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 레시피 목록
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(recipes) { (name, ingredients, description) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 음식 이름 (수정 가능)
                        val editableName = remember { mutableStateOf(name) }
                        TextField(
                            value = editableName.value,
                            onValueChange = { editableName.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .padding(4.dp),
                            placeholder = { Text("음식 이름을 입력하세요") }
                        )
                        val ingredientsString = ingredients.joinToString(", ") { it.name }

                        val editableIngredients = remember { mutableStateOf(ingredientsString) }
                        val editableDescription = remember { mutableStateOf(description) }
                        // 재료와 요리법 (높이 동기화)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // 재료 입력

                            BoxWithConstraints(
                                modifier = Modifier.weight(1f)
                            ) {
                                val maxHeight = constraints.maxHeight
                                TextField(
                                    value = editableIngredients.value,
                                    onValueChange = { editableIngredients.value = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = maxHeight.dp)
                                        .background(Color.White, RoundedCornerShape(8.dp))
                                        .padding(4.dp),
                                    placeholder = { Text("재료를 입력하세요") }
                                )
                            }

                            // 요리법 입력
                            BoxWithConstraints(
                                modifier = Modifier.weight(1f)
                            ) {
                                val maxHeight = constraints.maxHeight
                                TextField(
                                    value = editableDescription.value,
                                    onValueChange = { editableDescription.value = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = maxHeight.dp)
                                        .background(Color.White, RoundedCornerShape(8.dp))
                                        .padding(4.dp),
                                    placeholder = { Text("조리법을 입력하세요") }
                                )
                            }
                        }
                        // 버튼 영역
                        Button(
                            onClick = {
                                onButtonClick(
                                    editableName.value,
                                    editableIngredients.value,
                                    editableDescription.value
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = RoundedCornerShape(8.dp) // 모서리 둥근 사각형
                        ) {
                            Text(text = "레시피 수정하기", fontSize = 16.sp)
                        }
                    }
                }
            }

        }


    }
}

// 프리뷰를 위한 함수
@Composable
fun RecipeEditPreviewContent() {
    val navController = rememberNavController()
    EditRecipeScreen(navController = navController)
}

// 프리뷰
@Preview(showBackground = true, widthDp = 600, heightDp = 400)
@Composable
fun PreviewRecipeEditScreen() {
    CenterRecipeEditScreen(
        recipes = listOf(
            Recipe(
                name = "김치찌개",
                ingredients = listOf(
                    Ingredient(1, "김치"),
                    Ingredient(2, "물"),
                    Ingredient(3, "돼지고기", 1),
                    Ingredient(4, "양파", 1)
                ),
                description = "김치 볶기, 물 끓이기, 김치 넣기"
            ),
            Recipe(
                name = "된장찌개",
                ingredients = listOf(
                    Ingredient(1, "된장", 1),
                    Ingredient(2, "감자", 1),
                    Ingredient(3, "양파", 1)
                ),
                description = "된장 풀기, 감자 끓이기, 양파 볶기"
            )
        ),
        onButtonClick = { _, _, _ -> }
    )
}
