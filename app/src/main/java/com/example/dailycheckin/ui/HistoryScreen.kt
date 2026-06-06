package com.example.dailycheckin.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dailycheckin.data.CheckInRecord

@Composable
fun HistoryScreen(
    records: List<CheckInRecord>,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onBack) {
                Text("返回")
            }
            Text(
                text = "打卡记录",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        if (records.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("暂无打卡记录")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                content = {
                    items(
                        items = records,
                        key = { it.id },
                    ) { record ->
                        HistoryRow(record)
                    }
                },
            )
        }
    }
}

@Composable
private fun HistoryRow(record: CheckInRecord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(record.date, style = MaterialTheme.typography.bodyLarge)
        Text(
            record.checkInTime,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
}

