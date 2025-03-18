package com.cericatto.rickmortyreddit.ui.common

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cericatto.rickmortyreddit.R
import com.cericatto.rickmortyreddit.data.model.RickMortyCharacter
import com.cericatto.rickmortyreddit.ui.list_screen.ListScreenAction
import com.cericatto.rickmortyreddit.ui.list_screen.ListScreenState
import com.cericatto.rickmortyreddit.ui.utils.backgroundColor
import com.cericatto.rickmortyreddit.ui.utils.contentColor
import com.cericatto.rickmortyreddit.ui.utils.isLandscapeOrientation

@Suppress("DEPRECATION")
@Composable
fun DynamicStatusBarColor() {
	val isDarkTheme = isSystemInDarkTheme()
	val window = (LocalView.current.context as Activity).window
	val statusBarColor = if (isDarkTheme) Color.DarkGray else Color.LightGray.copy(alpha = 0.5f)

	LaunchedEffect(isDarkTheme) {
		// Enable edge-to-edge mode (recommended for modern Android)
		WindowCompat.setDecorFitsSystemWindows(window, false)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
			// Android 15+: Draw background behind status bar using WindowInsets
			window.decorView.setBackgroundColor(statusBarColor.toArgb())
			WindowCompat.getInsetsController(window, window.decorView).apply {
				// Control icon appearance
				isAppearanceLightStatusBars = !isDarkTheme
			}
		} else {
			// Android 14 and below: Use the legacy approach
			window.statusBarColor = statusBarColor.toArgb()
			WindowCompat.getInsetsController(window, window.decorView).apply {
				isAppearanceLightStatusBars = !isDarkTheme
			}
		}
	}
}

@Composable
fun CharacterListItem(
	index: Int,
	item: RickMortyCharacter,
	modifier: Modifier = Modifier
) {
	val cornerShapePadding: Dp = 20.dp
	val isLandscape = isLandscapeOrientation()
	val startPadding = if (isLandscape) {
		5.dp
	} else {
		10.dp
	}
	val endPadding = if (isLandscape) {
		if (index % 2 == 0) 0.dp else 5.dp
	} else {
		10.dp
	}
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Start,
		modifier = modifier
			.padding(
				start = startPadding, end = endPadding,
				bottom = 3.dp, top = 3.dp
			)
			.shadow(
				elevation = 5.dp,
				shape = RoundedCornerShape(cornerShapePadding),
			)
			.background(
				color = backgroundColor().copy(alpha = 1f),
				shape = RoundedCornerShape(cornerShapePadding)
			)
			.padding(3.dp)
			.background(
				color = backgroundColor(),
				shape = RoundedCornerShape(cornerShapePadding)
			)
			.fillMaxWidth()
			.wrapContentHeight()
			.padding(15.dp)
	) {
		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.Start,
			modifier = Modifier.weight(6f)
				.wrapContentHeight()
		) {
			Text(
				text = "Name: ${item.name}",
				style = TextStyle(
					fontWeight = FontWeight.Bold,
					fontSize = 18.sp
				),
				color = contentColor(),
			)
			CharacterTextFieldSmall(text = "Gender: ${item.gender}")
			CharacterTextFieldSmall(text = "Status: ${item.status}")
			CharacterTextFieldSmall(text = "Species: ${item.species}")
		}
		RoundedAsyncImage(item)
	}
}

@Composable
fun RowScope.RoundedAsyncImage(
	item: RickMortyCharacter
) {
	val circleShapePadding = 100.dp
	val cropPadding = 20.dp
	AsyncImage(
		model = ImageRequest.Builder(LocalContext.current)
			.data(item.image)
			.placeholder(R.drawable.placeholder)
			.error(R.drawable.placeholder)
			.build(),
		contentDescription = "Image of character ${item.name}",
		contentScale = ContentScale.Crop,
		modifier = Modifier.weight(4f)
			.padding(start = cropPadding)
			.clip(RoundedCornerShape(circleShapePadding))
	)
}

@Composable
fun CharacterTextFieldSmall(
	text: String,
	textSize: TextUnit = 14.sp
) {
	Text(
		text = text,
		color = contentColor(),
		fontSize = textSize
	)
}

@Composable
fun ErrorSnackbar(
	onAction: (ListScreenAction) -> Unit,
	state: ListScreenState
) {
	val messageHeight by animateDpAsState(
		// The target value is determined by the performAnimation state.
		targetValue = if (state.performAnimation) 68.dp else 0.dp,
		animationSpec = tween(
			durationMillis = 600,
			easing = FastOutSlowInEasing
		),
		label = "Error Message Height Animation"
	)
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = Modifier.background(color = Color(0xFFA40019))
			.height(messageHeight)
			// Add clip modifier to ensure content is clipped during animation.
			.clip(RectangleShape)
	) {
		Spacer(modifier = Modifier.padding(top = 10.dp))
		Text(
			text = state.errorMessage ?: "No Internet!",
			style = TextStyle(
				color = Color.White,
				textAlign = TextAlign.Center,
				fontSize = 16.sp
			),
			modifier = Modifier.fillMaxWidth()
				.clickable {
					onAction(ListScreenAction.OnRetry)
				}
		)
		Spacer(modifier = Modifier.padding(bottom = 20.dp))
		HorizontalDivider(
			modifier = Modifier
				.padding(horizontal = 140.dp)
				.background(
					color = Color.White,
					shape = RoundedCornerShape(30.dp)
				)
				.height(5.dp)
		)
		Spacer(modifier = Modifier.padding(bottom = 10.dp))
	}
}

@Composable
fun EmptyListContent(
	onAction: (ListScreenAction) -> Unit
) {
	Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.fillMaxSize()
	) {
		Text(
			text = "Empty List",
			style = TextStyle(
				color = contentColor(),
				fontSize = 28.sp
			),
			modifier = Modifier.padding(bottom = 16.dp)
		)
		Text(
			text = "Click here to Try Again",
			style = TextStyle(
				color = contentColor(),
				fontSize = 16.sp
			),
			modifier = Modifier
				.clickable {
					onAction(ListScreenAction.OnRetry)
				}
				.padding(bottom = 16.dp)
		)
	}
}

@Preview(
	name = "Dark Theme Preview",
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showBackground = true
)
@Composable
fun CharacterListItemDarkPreview() {
	CharacterListItem(
		index = 0,
		item = RickMortyCharacter(),
		modifier = Modifier.height(160.dp)
	)
}

@Preview(
	name = "Light Theme Preview",
	uiMode = Configuration.UI_MODE_NIGHT_NO,
	showBackground = true
)
@Composable
fun CharacterListItemLightPreview() {
	CharacterListItem(
		index = 0,
		item = RickMortyCharacter(),
		modifier = Modifier.height(160.dp)
	)
}