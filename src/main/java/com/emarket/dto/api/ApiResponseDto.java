package com.emarket.dto.api;

public record ApiResponseDto<T>(
        String mensaje,
        T data
) {
}
