package com.example.caloriesapp.domain.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.caloriesapp.data.model.Food
import com.example.caloriesapp.data.model.NutritionResponse
import com.example.caloriesapp.data.repository.FoodRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)  // Configuring Robolectric to use SDK 28
class FoodViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: FoodRepository

    @Mock
    private lateinit var context: Context

    @InjectMocks
    private lateinit var viewModel: FoodViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FoodViewModel(repository, context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()  // reset the main dispatcher to the original Main dispatcher
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test initial state`() = runBlockingTest {
        assertTrue(viewModel.foods.value.isNullOrEmpty())
        assertTrue(viewModel.suggestions.value.isEmpty())
    }

    @Test
    fun `test searchFoods updates foods and suggestions`() = runBlockingTest {
        val mockFoods = listOf(Food(name = "Apple", calories = 52.0))
        val mockResponse = Response.success(NutritionResponse(mockFoods))

        `when`(repository.getFoods("Apple")).thenReturn(mockResponse)

        viewModel.searchFoods("Apple")

        val updatedFoods = viewModel.foods.value
        val updatedSuggestions = viewModel.suggestions.value

        assertEquals(1, updatedFoods?.size)
        assertEquals("Apple", updatedFoods?.get(0)?.name)
        assertTrue(updatedSuggestions.contains("Apple"))
    }

    @Test
    fun `test clearFoods clears foods`() = runBlockingTest {
        viewModel.clearFoods()
        val updatedFoods = viewModel.foods.value
        assertTrue(updatedFoods.isNullOrEmpty())
    }

    @Test
    fun `test deleteFood removes food`() = runBlockingTest {
        val mockFood = Food(name = "Apple", calories = 52.0)
        viewModel.onQueryChanged("Apple")
        viewModel.searchFoods("Apple")
        viewModel.deleteFood(mockFood)

        val updatedFoods = viewModel.foods.value
        assertTrue(updatedFoods.isNullOrEmpty())
    }
}
