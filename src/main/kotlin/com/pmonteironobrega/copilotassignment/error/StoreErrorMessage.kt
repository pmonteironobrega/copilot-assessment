package com.pmonteironobrega.copilotassignment.error

enum class StoreErrorMessage(val message: String) {
    INVALID_BRAND("O parâmetro restaurantBrand não pode ser vazio."),
    INVALID_LOCATION("O parâmetro location não pode ser vazio."),
    NO_STORE_FOUND("Nenhum registro encontrado"),
    EMPTY_REQUEST("A lista de stores não pode ser vazia."),
    INTERNAL_ERROR("Erro interno ao buscar produtos indisponíveis")
}

