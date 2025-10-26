package com.hung.core.domain

interface ContinuousUseCase<Request, Result> {
    suspend operator fun invoke(request: Request, onResult: (Result) -> Unit)
}