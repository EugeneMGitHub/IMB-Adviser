package com.example.shareappsettingswithgiraffe.ui.screens.mainScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize


private val ELEMENT_HEIGHT = 60.dp

@Composable
fun <E> DropDownSpinner(
    modifier: Modifier = Modifier,
    defaultText: String = "Select...",
    selectedItem: E,
    onItemSelected: (Int, E) -> Unit,
    itemList: List<E>?,
    spacerBottomValue: Int = 0,
    spacerTopValue: Int = 0,

    ) {

    Spacer(modifier = Modifier.height(spacerTopValue.dp))

    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var isOpen by remember { mutableStateOf(false) }
    // var selectedText by remember{ mutableStateOf(selectedItem) }
    var selectedText = selectedItem
    Box(
        modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                //This value is used to assign to the DropDown the same width
                textfieldSize = coordinates.size.toSize()
            }
            .border(
                border = ButtonDefaults.outlinedButtonBorder,
                shape = RoundedCornerShape(6.dp)
            )
//            .background(MaterialTheme.colors.surface)
            .height(ELEMENT_HEIGHT)
            .clickable(
                onClick = { isOpen = true }
            ),

        contentAlignment = Alignment.CenterStart
    ) {

        if (selectedItem == null || selectedItem.toString().isEmpty()) {
            Text(
                text = defaultText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 3.dp),
            )
        }

        Text(
            text = selectedText?.toString() ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 32.dp, bottom = 3.dp),
        )


        DropdownMenu(
            modifier = Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() }),
            expanded = isOpen,
            onDismissRequest = {
                isOpen = false
            }
        ) {
            itemList?.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item.toString()) },
                    onClick = {
                        isOpen = false
                        onItemSelected(index, item)
                        selectedText = item
                    }

                )
            }
        }


        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                .size(24.dp),
        )


    }

    Spacer(modifier = Modifier.height(spacerBottomValue.dp))
}



