package com.example.SkrapApp.ui.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.SkrapApp.R
import androidx.compose.foundation.clickable

enum class BottomSheetState { Collapsed, Expanded }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onGuestClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onShowRegulamin: () -> Unit = {},
    onShowPolityka: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {}
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

    var sheetState by remember { mutableStateOf(BottomSheetState.Collapsed) }
    val sheetHeightFraction by animateFloatAsState(
        targetValue = if (sheetState == BottomSheetState.Collapsed) 0.42f else 0.5f,
        label = "sheetHeight"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Górny panel: slideshow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f - sheetHeightFraction)
        ) {
            if (1f - sheetHeightFraction > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
            }
        }

        // Separator nad dolnym panelem
        Divider(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .offset(y = (-sheetHeightFraction * 100).dp)
        )

        // Dolny panel: animowany bottom sheet
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(sheetHeightFraction)
                .align(Alignment.BottomCenter)
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
                .pointerInput(sheetState) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > 40 && sheetState == BottomSheetState.Expanded) {
                            sheetState = BottomSheetState.Collapsed
                        }
                        if (dragAmount < -40 && sheetState == BottomSheetState.Collapsed) {
                            sheetState = BottomSheetState.Expanded
                        }
                    }
                }
        ) {
            if (sheetState == BottomSheetState.Collapsed) {
                // Wszystko rozłożone responsywnie: header/przyciski u góry, notka na dole
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Grabber (handle)
                        Box(
                            Modifier
                                .padding(bottom = 24.dp)
                                .size(width = 40.dp, height = 4.dp)
                                .background(
                                    color = Color.LightGray.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                        // Header i opis
                        Text(
                            text = "Witaj w SkrapApp!",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Wybierz jak chcesz zacząć korzystać z aplikacji.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 18.dp)
                        )
                        Button(
                            onClick = { sheetState = BottomSheetState.Expanded },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp),
                            contentPadding = PaddingValues(vertical = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🔐", modifier = Modifier.padding(end = 8.dp))
                                Text("Załóż konto / Zaloguj się", fontSize = 16.sp)
                            }
                        }
                        Button(
                            onClick = onGuestClick,
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp, pressedElevation = 3.dp),
                            contentPadding = PaddingValues(vertical = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("👀", modifier = Modifier.padding(end = 8.dp))
                                Text("Przeglądaj jako gość", fontSize = 16.sp)
                            }
                        }
                    }
                    // Notka prawna na dole
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Korzystając z aplikacji, akceptujesz",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            lineHeight = 13.sp,
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Regulamin",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable { onShowRegulamin() }
                            )
                            Text(
                                text = " i ",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Politykę Prywatności",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable { onShowPolityka() }
                            )
                        }
                    }
                }
            } else {
                // Expanded: prawny i konwersyjny funnel
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Grabber (handle)
                    Box(
                        Modifier
                            .padding(top = 4.dp, bottom = 10.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(width = 40.dp, height = 4.dp)
                            .background(
                                color = Color.LightGray.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                    Text(
                        text = "Zaloguj się lub załóż konto",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Wybierz sposób logowania. To szybkie i bezpieczne.",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Button(
                        onClick = { /* TODO: obsługa logowania przez SMS */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📱", modifier = Modifier.padding(end = 8.dp))
                            Text("Przez numer telefonu", fontSize = 15.sp)
                        }
                    }
                    OutlinedButton(
                        onClick = { onGoogleLoginClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = "Google logo",
                            modifier = Modifier
                                .size(22.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "Kontynuuj z Google",
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    }
                    TextButton(
                        onClick = onGuestClick,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                    ) {
                        Text("Przeglądaj jako gość", fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Info prawne i bezpieczeństwo
                    Text(
                        text = "Kontynuując, akceptujesz Regulamin i Politykę Prywatności.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Regulamin",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { onShowRegulamin() }
                        )
                        Text(
                            text = " i ",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Politykę Prywatności",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { onShowPolityka() }
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Twoje dane są bezpieczne i wykorzystywane tylko do obsługi konta.",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = "Masz problem z logowaniem? pomoc@skrapapp.pl",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Przeciągnij panel w dół, aby wrócić",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
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
