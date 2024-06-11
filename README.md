
# Calorie Tracking App

This is a Kotlin-based Android application for tracking calories. The app allows users to input food items, get nutritional information, and track their daily calorie intake. The project uses Jetpack Compose for the UI and Hilt for dependency injection.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Project Structure](#project-structure)
3. [Key Features](#key-features)
4. [Dependencies](#dependencies)
5. [Running the Project](#running-the-project)
6. [Documentation](#documentation)

## Getting Started

Follow these instructions to get a copy of the project running on your local machine for development and testing purposes.

### Prerequisites

- Android Studio (Arctic Fox or later)
- Kotlin 1.5.0 or later
- Gradle 7.0.0 or later
- An Android device or emulator running Android 5.0 (Lollipop) or later

### Installation

1. **Clone the repository**:

    \`\`\`bash
    git clone https://github.com/yourusername/calorie-tracking-app.git
    \`\`\`

2. **Open the project in Android Studio**:

    - Open Android Studio.
    - Click on `File > Open...` and select the project directory.

3. **Sync the project with Gradle files**:

    - Click on `File > Sync Project with Gradle Files` to ensure all dependencies are downloaded.

## Project Structure

The project follows the standard Android project structure with additional organization for Jetpack Compose and Hilt.

- \`src/main/java/com/example/caloriesapp/\`
  - \`data/\`: Contains data models, repositories, and API services.
  - \`domain/\`: Contains view models and use cases.
  - \`ui/\`: Contains Composable functions for the UI.
  - \`di/\`: Contains dependency injection modules.
  - \`CalorieTrackingApp.kt\`: The application class.
  - \`MainActivity.kt\`: The main activity of the app.

## Key Features

- **Track Daily Calories**: Input food items and track daily calorie intake.
- **Auto-complete Search**: Search for food items with auto-complete suggestions.
- **Nutritional Information**: View detailed nutritional information for each food item.
- **Calorie Budget**: Monitor daily calorie consumption against a preset budget.
- **Swipe to Delete**: Remove food items from the list with a swipe gesture.

## Dependencies

The project uses several libraries to achieve its functionality:

- **Jetpack Compose**: For building the UI.
- **Hilt**: For dependency injection.
- **Retrofit**: For networking.
- **Gson**: For JSON parsing.
- **OkHttp**: For HTTP client.
- **MockWebServer**: For testing HTTP responses.

## Running the Project

1. **Build the project**:

    - Click on `Build > Make Project` or use the shortcut \`Ctrl+F9\`.

2. **Run the app**:

    - Click on `Run > Run 'app'` or use the shortcut \`Shift+F10\`.

3. **Testing**:

    - The project includes unit and instrumentation tests. To run the tests, click on `Run > Run 'All Tests'` or use the shortcut \`Ctrl+Shift+F10\`.

## Documentation

### Composable Functions

#### \`LandingScreen\`

\`\`\`kotlin
@Composable
fun LandingScreen() {
    // Implementation...
}
\`\`\`

Displays the main screen of the app including the top app bar, today's budget section, and food tracking section.

#### \`TodaysBudgetSection\`

\`\`\`kotlin
@Composable
fun TodaysBudgetSection(viewModel: FoodViewModel = hiltViewModel()) {
    // Implementation...
}
\`\`\`

Displays the user's daily calorie budget and consumed calories.

#### \`NutrientProgress\`

\`\`\`kotlin
@Composable
fun NutrientProgress(name: String, value: Int, maxValue: Int, color: Color) {
    // Implementation...
}
\`\`\`

Displays a progress bar for a specific nutrient (protein, carbs, fats).

#### \`FoodTrackingSection\`

\`\`\`kotlin
@Composable
fun FoodTrackingSection(viewModel: FoodViewModel = hiltViewModel()) {
    // Implementation...
}
\`\`\`

Displays a list of tracked food items and allows for clearing the list.

#### \`NutritionalInfo\`

\`\`\`kotlin
@Composable
fun NutritionalInfo(protein: Double, fats: Double, carbs: Double) {
    // Implementation...
}
\`\`\`

Displays the nutritional information for a food item.

#### \`FoodTrackingItem\`

\`\`\`kotlin
@Composable
fun FoodTrackingItem(
    name: String,
    calories: Double,
    maxCalories: Int,
    icon: ImageVector,
    foodInfo: Food,
    onDelete: () -> Unit
) {
    // Implementation...
}
\`\`\`

Displays a food item with swipe-to-delete functionality.

### ViewModel

#### \`FoodViewModel\`

\`\`\`kotlin
@HiltViewModel
class FoodViewModel @Inject constructor(
    private val repository: FoodRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    // Implementation...
}
\`\`\`

Manages the UI-related data in a lifecycle-conscious way. Interacts with \`FoodRepository\` to fetch and manage food data.

### Repository

#### \`FoodRepository\`

\`\`\`kotlin
@Singleton
class FoodRepository @Inject constructor(private val api: CalorieNinjasApi) {
    suspend fun getFoods(query: String): Response<NutritionResponse> {
        return api.getNutrition(query)
    }
}
\`\`\`

Handles data operations related to food items by interacting with the \`CalorieNinjasApi\`.

## Contributing

If you want to contribute to the project, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Commit your changes.
4. Push your changes to your fork.
5. Submit a pull request to the main repository.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

This README provides an overview of the Calorie Tracking App, including setup instructions, project structure, key features, and documentation for the main components.
