package com.example.system.ui.ingredient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.system.ingredientsDB.toLocalDate
import com.example.system.ui.component.ForceLandscapeOrientation
import com.example.system.ui.component.HorizontalButton
import com.example.system.ui.component.LeftScreen
import com.example.system.ui.viewmodel.IngredientViewModel

@Composable
fun NewIngredientScreen(
    navController: androidx.navigation.NavHostController,
    ingredientViewModel: IngredientViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        ingredientViewModel.getIngredients()
    }

    ForceLandscapeOrientation() // 가로 모드 강제 설정
    Row(
        modifier = androidx.compose.ui.Modifier.fillMaxSize()
    ) {
        // 좌측 화면 - LeftScreen 재사용
        LeftScreen(
            modifier = androidx.compose.ui.Modifier
                .wrapContentSize()
                .fillMaxHeight()
                .background(Color.LightGray),
            navController = navController
        )

        // 가운데 화면
        CenterIngredientScreen(
            modifier = androidx.compose.ui.Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.White),
            navController = navController
        )
    }
}

@Composable
fun CenterIngredientScreen(
    modifier: Modifier = androidx.compose.ui.Modifier,
    navController: androidx.navigation.NavHostController,
    ingredientViewModel: IngredientViewModel = hiltViewModel()
) {
    val ingredients by ingredientViewModel.ingredientList.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // DB에서 가져온 데이터 부분
        Box(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
        ) {
            Column(
                modifier = androidx.compose.ui.Modifier.fillMaxSize()
            ) {
                // 데이터 항목 제목 (고정 부분)
                Row(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "식재료",
                        modifier = androidx.compose.ui.Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "현재 수량(g)",
                        modifier = androidx.compose.ui.Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "유통기한",
                        modifier = androidx.compose.ui.Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                }

                // 데이터 목록 (스크롤 가능 부분)
                LazyColumn(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    // 예제 데이터
                    items(ingredients) { ingredient ->
                        Row(
                            modifier = androidx.compose.ui.Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = ingredient.name,
                                modifier = androidx.compose.ui.Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = ingredient.quantity.toString(),
                                modifier = androidx.compose.ui.Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = toLocalDate(ingredient.expirationDate)
                                    .toString()
                                    .replace("-", "."),
                                modifier = androidx.compose.ui.Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // 버튼 및 냉장고 용량 부분
        Row(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .height(80.dp) // 버튼 높이와 동일하게 설정
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val cornerRadius = 16.dp // 버튼 모서리 둥근 정도
            val textSize = 16.sp // 텍스트 크기 설정

            // 첫 번째 버튼 - 식재료 등록
            HorizontalButton(text = "식재료\n등록") {
                navController.navigate("addIngredient")
            }

            // 두 번째 버튼 - 식재료 꺼내기
            HorizontalButton(text = "식재료\n꺼내기") {
                navController.navigate("takeOutIngredient")
            }

            // 세 번째 버튼 - 유통기한 정보 입력/수정
            HorizontalButton(text = "유통기한 정보\n입력/수정") {
                navController.navigate("ingredientExpirationDate")
            }

            // 네 번째 텍스트 - 냉장고 용량
            HorizontalButton(text = "냉장고 용량\n 50%"){}
        }
    }
}


// 프리뷰
@Preview(showBackground = true, widthDp = 600, heightDp = 400)
@Composable
fun PreviewIngredientScreen() {
    NewIngredientScreen(navController = rememberNavController())
}
