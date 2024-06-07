package com.example.caloriesapp.domain.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.caloriesapp.data.model.Food
import com.example.caloriesapp.data.model.NutritionResponse
import com.example.caloriesapp.data.repository.FoodRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class FoodViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: FoodRepository

    private lateinit var viewModel: FoodViewModel

    @Mock
    private lateinit var observer: Observer<List<Food>>

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = FoodViewModel(repository)
        viewModel.foods.observeForever(observer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher to the original Main dispatcher
        testDispatcher.cancel()
    }

    @Test
    fun `searchFoods should update live data on success`() = runTest {
        val foodList = listOf(Food("Apple", 52f))
        val response = Response.success(NutritionResponse(foodList))

        `when`(repository.getFoods("Apple")).thenReturn(response)

        viewModel.searchFoods("Apple")

        verify(observer).onChanged(foodList)
        assertEquals(foodList, viewModel.foods.value)
    }

    @Test
    fun `searchFoods should not update live data on error`() = runTest {
        val response = Response.error<NutritionResponse>(
            400,
            okhttp3.ResponseBody.create(null, "Error")
        )

        `when`(repository.getFoods("Apple")).thenReturn(response)

        viewModel.searchFoods("Apple")

        verify(observer, never()).onChanged(ArgumentMatchers.anyList())
        assertNull(viewModel.foods.value)
    }
}
