package com.example.shareappsettingswithgiraffe.ui.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shareappsettingswithgiraffe.navigation.Routes
import com.example.shareappsettingswithgiraffe.navigation.mainTopBarTabs
import java.util.Locale

@Composable
fun MainTopBar(
    topBarScreens: List<Routes>,
    currentScreen: Routes,
    onClick: (Routes) -> Unit
) {
    Surface(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth(),
    ){
        Row(
            modifier = Modifier.selectableGroup(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            topBarScreens.forEach{screen ->
                MainTopBarItem(
                    text = screen.route,
                    icon = screen.icon,
                    onClick = { onClick(screen) },
                    ifSelected = currentScreen == screen)
            }

        }

    }



}

@Composable
private fun MainTopBarItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    ifSelected: Boolean
    ){

    val color = MaterialTheme.colors.onSurface

    val durationMillis = if (ifSelected == true) 150 else 100
    val animSpec = remember {
        tween<Color>(
            durationMillis = durationMillis,
            easing = LinearEasing,
            delayMillis = 100
        )
    }
    val tabTintColor by animateColorAsState(
        targetValue = if (ifSelected == true) color
        else color.copy(alpha = 0.60f),
        animationSpec = animSpec,
        label = ""
    )

    Row(
        modifier = Modifier
            .widthIn(0.dp, 155.dp)
            .padding(10.dp)
            .animateContentSize()
            .height(70.dp)
            .selectable(
                selected = ifSelected,
                onClick = onClick,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified
                )
            )
            .clearAndSetSemantics { contentDescription = text },
            verticalAlignment = Alignment.CenterVertically
    ){
        Icon(imageVector = icon, contentDescription = text, tint = tabTintColor)
        if(ifSelected == true){
            Spacer(modifier = Modifier.width(5.dp))
            Text(text.uppercase(Locale.getDefault()), color = tabTintColor,)

        }

    }



}

@Preview(showBackground = true)
@Composable
private fun ShowMainTopBar(){
    var currentScreen: Routes by remember { mutableStateOf(Routes.Main)}

    MainTopBar(
        topBarScreens = mainTopBarTabs,
        currentScreen = currentScreen,
        onClick = {})
}
