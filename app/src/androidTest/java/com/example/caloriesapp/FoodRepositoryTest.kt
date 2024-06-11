package com.example.caloriesapp.data.repository

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.caloriesapp.data.api.CalorieNinjasApi
import com.example.caloriesapp.data.model.Food
import com.example.caloriesapp.data.model.NutritionResponse
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FoodRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: CalorieNinjasApi
    private lateinit var repository: FoodRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CalorieNinjasApi::class.java)

        repository = FoodRepository(api)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test getFoods returns data`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""{
                "items": [
                    {"id": "1", "name": "Apple", "calories": 52.0, "serving_size_g": 100.0, "fat_total_g": 0.2, "protein_g": 0.3, "carbohydrates_total_g": 14.0}
                ]
            }""")

        mockWebServer.enqueue(mockResponse)

        val response = repository.getFoods("Apple")
        val foods = response.body()?.items

        Assert.assertTrue(response.isSuccessful)
        Assert.assertNotNull(foods)
        Assert.assertEquals(1, foods?.size)
        Assert.assertEquals("Apple", foods?.get(0)?.name)
    }
}
