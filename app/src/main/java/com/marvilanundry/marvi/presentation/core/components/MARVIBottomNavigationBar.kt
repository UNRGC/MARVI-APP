package com.marvilanundry.marvi.presentation.core.components

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.ui.theme.CustomColors

data class NavItem(val label: String, val icon: Int, val iconFill: Int)

@Composable
fun MARVIBottomNavigationBar(pagerState: PagerState, onItemSelected: (Int) -> Unit = {}) {
    val outlineColor = MaterialTheme.colorScheme.outline
    val items = remember {
        listOf(
            NavItem("Inicio", R.drawable.ic_house, R.drawable.ic_house_fill),
            NavItem("Pedidos", R.drawable.ic_box2, R.drawable.ic_box2_fill),
            NavItem("Cotizar", R.drawable.ic_calculator, R.drawable.ic_calculator_fill),
            NavItem("Cuenta", R.drawable.ic_person, R.drawable.ic_person_fill)
        )
    }

    NavigationBar(
        modifier = Modifier.drawBehind {
            drawLine(
                color = outlineColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 1.dp.toPx()
            )
        }, containerColor = CustomColors.backgroundVariant
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(if (pagerState.targetPage == index) item.iconFill else item.icon),
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = pagerState.targetPage == index,
                onClick = { onItemSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = CustomColors.placeholderColor,
                    unselectedTextColor = CustomColors.placeholderColor,
                    indicatorColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}