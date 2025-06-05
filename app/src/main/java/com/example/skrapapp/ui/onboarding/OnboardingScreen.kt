package com.example.SkrapApp.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.SkrapApp.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onGuestClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onShowRegulamin: () -> Unit = {},
    onShowPolityka: () -> Unit = {}
) {
    val slides = listOf(
        R.drawable.onboarding1,
        R.drawable.onboarding2,
        R.drawable.onboarding3
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { slides.size })
    val coroutineScope = rememberCoroutineScope()

    // Automatyczna zmiana slajdu co 4 sekundy z animacją
    LaunchedEffect(pagerState.currentPage) {
        delay(4000)
        val nextPage = (pagerState.currentPage + 1) % slides.size
        coroutineScope.launch {
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Górne 2/3 ekranu: swipeable pager z grafikami + kropki
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Image(
                    painter = painterResource(id = slides[page]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Kropki nawigacyjne na dole grafiki
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(24.dp)
                    .align(Alignment.BottomCenter)
            ) {
                repeat(slides.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (index == pagerState.currentPage) 14.dp else 8.dp)
                            .background(
                                if (index == pagerState.currentPage) MaterialTheme.colorScheme.primary
                                else Color.LightGray,
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        // Separator nad dolnym paskiem
        Divider(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        // Dolna 1/3 ekranu: przyciski i informacje, gradient + cień
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.97f),
                            MaterialTheme.colorScheme.surface
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .shadow(8.dp, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Button(
                        onClick = onGuestClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("👀", modifier = Modifier.padding(end = 8.dp))
                            Text("Przeglądaj jako gość", color = Color.Black)
                        }
                    }
                    Button(
                        onClick = onLoginClick,
                        modifier = Modifier.fillMaxWidth(),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔐", modifier = Modifier.padding(end = 8.dp))
                            Text("Załóż konto / Zaloguj się")
                        }
                    }
                }
                // Subtelny divider między przyciskami a notką
                Divider(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                    thickness = 1.dp,
                    modifier = Modifier
                        .padding(vertical = 18.dp)
                        .fillMaxWidth(0.6f)
                        .align(Alignment.CenterHorizontally)
                )
                // Dwie linijki, wyśrodkowane, z klikalnymi linkami
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Korzystając z aplikacji, akceptujesz",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Regulamin",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { onShowRegulamin() }
                        )
                        Text(
                            text = " i ",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Politykę Prywatności",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { onShowPolityka() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}
