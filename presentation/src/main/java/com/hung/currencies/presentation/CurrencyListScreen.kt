package com.hung.currencies.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hung.currencies.presentation.model.CurrencyFilterPresentationModel
import com.hung.currencies.presentation.model.CurrencyInfoPresentationModel

@Composable
fun CurrencyListScreen() {
    val viewModel: CurrencyListViewModel = hiltViewModel<CurrencyListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    DisposableEffect(Unit) {
        viewModel.onEnter()
        onDispose {
            viewModel.onExit()
        }
    }
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            CurrencySearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                query = state.searchQuery,
                onQueryChange = viewModel::onSearchQueryAction,
                onSearch = viewModel::onSearchQueryAction,
                onDismiss = {
                    viewModel.onSearchQueryAction("")
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        },
        bottomBar = {
            CurrencyActionSection(
                onClearClick = viewModel::onClearAction,
                onInsertClick = viewModel::onInsertAction
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            item {
                CurrencyFilterSection(
                    selectedFilter = state.filter,
                    onFilterSelect = {
                        viewModel.onFilterAction(it)
                    }
                )
            }
            if (state.currencyList.isEmpty()) {
                item {
                    EmptySection(modifier = Modifier.fillParentMaxSize(), isSearching = state.searchQuery.isNotEmpty())
                }
            } else {
                itemsIndexed(items = state.currencyList, key = { _, currency -> currency.id }) { index, currency ->
                    CurrencyInfoItem(currency = currency)
                    if (index != state.currencyList.lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(start = 44.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyInfoItem(currency: CurrencyInfoPresentationModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color.White)
            .padding(12.dp)
    ) {
        Text(
            modifier = Modifier
                .size(24.dp)
                .background(Color.Black, CircleShape),
            text = currency.name.take(1).uppercase(),
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = currency.name,
        )
        Spacer(Modifier.weight(1f))
        if (currency is CurrencyInfoPresentationModel.Crypto) {
            Text(
                text = currency.symbol,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = "${currency.name}'s next indicator",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencySearchBar(
    query: String,
    onDismiss: () -> Unit,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val leadingIcon = if (isFocused) {
        @Composable {
            IconButton(
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back icon"
                )
            }
        }
    } else {
        null
    }
    val trailingIcon = if (isFocused) {
        @Composable {
            IconButton(
                onClick = { onQueryChange("") }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear icon"
                )
            }
        }
    } else {
        null
    }
    SearchBar(
        modifier = modifier,
        state = rememberSearchBarState(),
        inputField = {
            InputField(
                interactionSource = interactionSource,
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                expanded = true,
                onExpandedChange = {},
                placeholder = {
                    Text(text = stringResource(R.string.currency_list_search_hint))
                },
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon
            )
        },
    )
}

@Composable
private fun CurrencyFilterSection(
    modifier: Modifier = Modifier,
    selectedFilter: CurrencyFilterPresentationModel = CurrencyFilterPresentationModel.ALL,
    onFilterSelect: (CurrencyFilterPresentationModel) -> Unit,
) {
    val filters = remember {
        listOf(
            CurrencyFilterPresentationModel.ALL,
            CurrencyFilterPresentationModel.FIAT,
            CurrencyFilterPresentationModel.CRYPTO
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        filters.forEach { filter ->
            CurrencyFilterChip(
                selected = filter == selectedFilter,
                filterText = filter.name,
                onFilterSelect = { onFilterSelect(filter) }
            )
        }
    }
}

@Composable
private fun CurrencyFilterChip(
    selected: Boolean,
    filterText: String,
    modifier: Modifier = Modifier,
    onFilterSelect: () -> Unit
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onFilterSelect,
        label = {
            Text(text = filterText)
        },
    )
}

@Composable
private fun CurrencyActionSection(
    modifier: Modifier = Modifier,
    onClearClick: () -> Unit = {},
    onInsertClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ClearButton(modifier = Modifier.weight(1f), onClearClick = onClearClick)
        InsertButton(modifier = Modifier.weight(1f), onInsertClick = onInsertClick)
    }
}

@Composable
private fun ClearButton(modifier: Modifier = Modifier, onClearClick: () -> Unit) {
    OutlinedButton(
        onClick = onClearClick,
        modifier = modifier
    ) {
        Text(text = stringResource(R.string.currency_list_clear_btn_label))
    }
}

@Composable
private fun InsertButton(modifier: Modifier = Modifier, onInsertClick: () -> Unit) {
    OutlinedButton(
        onClick = onInsertClick,
        modifier = modifier
    ) {
        Text(text = stringResource(R.string.currency_list_insert_btn_label))
    }
}

@Composable
private fun EmptySection(isSearching: Boolean, modifier: Modifier = Modifier) {
    val emptyText = if (isSearching) {
        stringResource(R.string.currency_list_search_empty_msg)
    } else {
        stringResource(R.string.currency_list_empty_msg)
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_empty),
            contentDescription = "Empty image",
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Text(text = emptyText)
    }
}