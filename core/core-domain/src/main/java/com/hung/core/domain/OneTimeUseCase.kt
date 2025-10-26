package com.hung.core.domain

interface OneTimeUseCase<Request, Result> {
    suspend operator fun invoke(request: Request): Result
}