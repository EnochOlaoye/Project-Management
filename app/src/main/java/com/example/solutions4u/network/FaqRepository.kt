package com.example.solutions4u.network

sealed class FaqResult {
    data class Success(
        val faqs: List<Faq> = emptyList(),
        val faq: Faq? = null
    ) : FaqResult()
    data class Error(val message: String) : FaqResult()
}

class FaqRepository {
    private val api = RetrofitClient.apiService

    suspend fun getFaqs(): FaqResult {
        return try {
            val response = api.getFaqs()
            if (response.isSuccessful) {
                FaqResult.Success(faqs = response.body()?.faqs ?: emptyList())
            } else {
                FaqResult.Error("Failed to load FAQs")
            }
        } catch (e: Exception) {
            FaqResult.Error("Could not connect to server. Is it running?")
        }
    }

    suspend fun addFaq(question: String, answer: String): FaqResult {
        return try {
            val response = api.addFaq(FaqRequest(question, answer))
            if (response.isSuccessful) {
                FaqResult.Success(faq = response.body()?.faq)
            } else {
                FaqResult.Error("Failed to add FAQ")
            }
        } catch (e: Exception) {
            FaqResult.Error("Could not connect to server. Is it running?")
        }
    }

    suspend fun updateFaq(id: Int, question: String, answer: String): FaqResult {
        return try {
            val response = api.updateFaq(id, FaqRequest(question, answer))
            if (response.isSuccessful) {
                FaqResult.Success(faq = response.body()?.faq)
            } else {
                FaqResult.Error("Failed to update FAQ")
            }
        } catch (e: Exception) {
            FaqResult.Error("Could not connect to server. Is it running?")
        }
    }

    suspend fun deleteFaq(id: Int): FaqResult {
        return try {
            val response = api.deleteFaq(id)
            if (response.isSuccessful) {
                FaqResult.Success()
            } else {
                FaqResult.Error("Failed to delete FAQ")
            }
        } catch (e: Exception) {
            FaqResult.Error("Could not connect to server. Is it running?")
        }
    }
}