package com.marvilanundry.marvi.presentation.auth.home.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.auth.home.HomeUiState
import com.marvilanundry.marvi.presentation.auth.home.HomeViewModel
import com.marvilanundry.marvi.presentation.auth.home.components.ClearableTextField
import com.marvilanundry.marvi.presentation.auth.home.components.ColumnScrollable
import com.marvilanundry.marvi.presentation.auth.home.components.Container
import com.marvilanundry.marvi.presentation.auth.home.components.SectionHeader
import com.marvilanundry.marvi.presentation.core.components.MARVIButton
import com.marvilanundry.marvi.ui.theme.CustomColors
import kotlinx.coroutines.launch

data class Status(val icon: Int, val title: String, val message: String)

@Composable
fun FollowScreen(homeViewModel: HomeViewModel, homeViewModelState: HomeUiState) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val statusItems = listOf(
        Status(
            R.drawable.ic_shop_window,
            stringResource(id = R.string.marvi_follow_status_one),
            stringResource(id = R.string.marvi_follow_status_one_message)
        ), Status(
            R.drawable.ic_box2_heart,
            stringResource(id = R.string.marvi_follow_status_two),
            stringResource(id = R.string.marvi_follow_status_two_message)
        ), Status(
            R.drawable.ic_bag_check,
            stringResource(id = R.string.marvi_follow_status_three),
            stringResource(id = R.string.marvi_follow_status_three_message)
        )
    )

    ColumnScrollable(
        scrollState = scrollState, paddingValues = PaddingValues(24.dp, 24.dp, 24.dp, 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.height(32.dp)
        )
        Container {
            SectionHeader(
                iconRes = R.drawable.ic_card_checklist,
                title = stringResource(id = R.string.marvi_follow_title)
            )
            ClearableTextField(
                label = stringResource(id = R.string.marvi_follow_order),
                value = homeViewModelState.orderInput,
                onValueChange = { homeViewModel.onOrderChange(it) },
                placeholder = stringResource(id = R.string.marvi_follow_order_placeholder),
                keyboardType = KeyboardType.Number,
                keyboardActions = KeyboardActions(
                    onDone = {
                        homeViewModel.followOrder()
                    })
            )
            MARVIButton(
                text = stringResource(id = R.string.marvi_follow_button),
                modifier = Modifier.fillMaxWidth(),
                enabled = homeViewModelState.isFollowEnabled,
                message = stringResource(id = R.string.marvi_follow_button_message),
                onClick = {
                    homeViewModel.followOrder()
                })
        }
        Text(
            text = stringResource(id = R.string.marvi_follow_status_title),
            textAlign = TextAlign.Center
        )
        val currentStatusIndex =
            statusItems.indexOfFirst { it.title == homeViewModelState.followedOrder?.estado }
        statusItems.forEachIndexed { index, item ->
            val ready = index <= currentStatusIndex && currentStatusIndex != -1

            if (ready) {
                LocalFocusManager.current.clearFocus()
                coroutineScope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start)
            ) {
                Icon(
                    painter = if (ready) painterResource(id = R.drawable.ic_check_circle_fill) else painterResource(
                        id = R.drawable.ic_circle
                    ),
                    contentDescription = null,
                    tint = if (ready) MaterialTheme.colorScheme.primary else CustomColors.placeholderColor
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.small,
                        )
                        .background(
                            color = CustomColors.backgroundVariant,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start)
                ) {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp),
                        tint = if (ready) MaterialTheme.colorScheme.primary else CustomColors.placeholderColor
                    )
                    Column {
                        Text(
                            text = item.title,
                            color = if (ready) MaterialTheme.colorScheme.primary else CustomColors.placeholderColor
                        )
                        Text(
                            text = item.message,
                            color = if (ready) CustomColors.textColor else CustomColors.placeholderColor,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}