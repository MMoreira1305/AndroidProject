package com.example.partner.dto

import com.example.partner.model.Info

data class HistoryDTO(
    var nameActivity: String = "",
    var date: String = "",
    var info: Info = Info(),
    var aluno: String = ""
)