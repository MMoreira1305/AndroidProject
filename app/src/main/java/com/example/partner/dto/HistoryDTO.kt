package com.example.partner.dto

import com.example.partner.model.Info

data class HistoryDTO(
    var nameActivity: String = "",
    var dateActivity: String = "",
    var infoActivity: Info = Info(),
    var aluno: String = ""
)