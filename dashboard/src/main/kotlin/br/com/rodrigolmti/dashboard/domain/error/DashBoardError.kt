package br.com.rodrigolmti.dashboard.domain.error

sealed class DashBoardError {
    object GeneratePasswordError : DashBoardError()
}