package com.example.caloriesapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.caloriesapp.data.api.CalorieNinjasApi
import com.example.caloriesapp.data.model.Food
import com.example.caloriesapp.data.model.NutritionResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class FoodRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: CalorieNinjasApi

    private lateinit var repository: FoodRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = FoodRepository(api)
    }

    @Test
    fun `getFoods should return success`() = runTest {
        val foodList = listOf(Food("Apple", 52f))
        val response = Response.success(NutritionResponse(foodList))

        `when`(api.getNutrition("Apple")).thenReturn(response)

        val result = repository.getFoods("Apple")
        assertTrue(result.isSuccessful)
        assertEquals(foodList, result.body()?.items)
    }

    @Test
    fun `getFoods should return error`() = runTest {
        val response = Response.error<NutritionResponse>(
            400,
            ResponseBody.create("application/json".toMediaType(), "Error")
        )

        `when`(api.getNutrition("Apple")).thenReturn(response)

        val result = repository.getFoods("Apple")
        assertFalse(result.isSuccessful)
    }
}
