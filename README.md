# WeatherAPI.com Code Challenge 

This project is a coding challenge written in **Kotlin** using **Gradle** and **Retrofit**.  
It fetches tomorrow's weather forecast from [WeatherAPI.com](https://www.weatherapi.com) for:

- Chisinau
- Madrid
- Kyiv
- Amsterdam

The forecast is printed to `STDOUT` in a formatted table with the following data points:

- Minimum Temperature (°C)
- Maximum Temperature (°C)
- Humidity (%)
- Wind Speed (kph)
- Wind Direction (from the windiest hour of the day)

---

## Requirements (from challenge document)

- App must be written in Java (bonus: Kotlin)  
- Code tracked with Git and pushed to GitHub  
- Forecast for the next day for the listed cities  
- Output formatted as a table 
- Show: min temp, max temp, humidity, wind speed, wind direction 

**Bonus points implemented:**
- Written in Kotlin  
- Built with Gradle  
- Used Retrofit + Moshi  

---

## Setup & Installation

### 1. Clone the repository
```bash
git clone https://github.com/Avelore/weather-challenge.git
cd weather-challenge
```
---

### 2. Open in IntelliJ IDEA
- Go to **File -> Open**  
- Select the `weather-challenge` folder  
- IntelliJ will detect the Gradle project and sync dependencies  

### 3. Get a WeatherAPI key
- Sign up at [WeatherAPI.com](https://www.weatherapi.com)  
- Go to **Dashboard** -> Copy **API Key**  

### 4. Add the API key in IntelliJ Run Configurations
1. Open `Main.kt` and run it 
2. IntelliJ creates a run configuration (`MainKt`)  
3. Go to **Run → Edit Configurations...**  
4. Select `MainKt` 
5. Find **Environment variables** -> click `...`  
7. Add a variable:  
   ```bash
   WEATHERAPI_KEY=your_api_key_here
   ```
8. Save and run
