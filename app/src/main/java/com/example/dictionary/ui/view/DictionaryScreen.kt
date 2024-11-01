package com.example.dictionary.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dictionary.ui.viewModel.DictionaryScreen
import com.example.dictionary.ui.viewModel.DictionaryViewModel

@Composable
fun DictionaryScreen(modifier: Modifier, viewModel: DictionaryViewModel) {
    val uiState by viewModel.uiState.collectAsState();
    Content(modifier, uiState) { query -> viewModel.updateWord(query) }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Content(
    modifier: Modifier,
    uiState: DictionaryScreen.UiState,
    onQuery: (String) -> Unit
) {
    val query = rememberSaveable() { mutableStateOf("") }
    Scaffold(modifier, topBar = {
        TextField(
            value = query.value,
            onValueChange = {
                query.value = it
                onQuery.invoke(query.value)
            },
            placeholder = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )
    }) {
        Column(modifier.fillMaxWidth().padding(top = 40.dp)) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Error message
            if (uiState.error.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Display search results
            uiState.data?.let { results ->
                Spacer(modifier = Modifier.height(16.dp))  // Space between elements
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Word: ${results.first().word}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Space between word and meanings
                    Text(
                        text = "Meanings: ${results.first().meanings}",
                        style = MaterialTheme.typography.bodySmall
                    )

                }
            }
        }


    }

}