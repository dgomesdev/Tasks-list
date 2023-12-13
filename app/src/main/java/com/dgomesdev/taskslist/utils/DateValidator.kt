package com.dgomesdev.taskslist.utils

fun validateEndDate(startDate: String?, endDate: String?): Boolean {
    if (startDate != null && endDate != null) {
        return endDate.toDate().isAfter(startDate.toDate())
    }
    return true
}