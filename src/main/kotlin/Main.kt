import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//  data models for the fields needed from WeatherAPI JSON
data class ForecastResponse(
    val location: Location,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val country: String
)

data class Forecast(
    val forecastday: List<ForecastDay>
)


data class ForecastDay(
    val date: String,
    val day: Day,
    val hour: List<Hour>
)

data class Day(
    @Json(name = "mintemp_c") val tempMin: Double,
    @Json(name = "maxtemp_c") val tempMax: Double,
    @Json(name = "avghumidity") val humidityAvg: Double,
    @Json(name = "maxwind_kph") val windKph: Double,
)

data class Hour(
    @Json(name = "time") val time: String,
    @Json(name = "wind_kph") val windKph: Double,
    @Json(name = "wind_dir") val windDir: String
)

//  Retrofit API interface
interface WeatherApi {
    @GET("forecast.json")
    suspend fun forecast(
        @Query("key") key: String,       // the API key
        @Query("q") location: String,    // city name
        @Query("days") days: Int = 2,    // requesting 2 days (today and tomorrow)
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ): ForecastResponse
}

fun main() = runBlocking {
    //  trying to read the key from environment
    val key = System.getenv("WEATHERAPI_KEY")
    //  checking if key null
    if (key == null) {
        println("API key not set.")
        return@runBlocking
    }
    //  confirming key is not null and continuing
    println("API key set.")

    //  building retrofit instance
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // use the Moshi instance
        .build()

//    val retrofit = Retrofit.Builder()
//        .baseUrl("https://api.weatherapi.com/v1/")
//        .addConverterFactory(MoshiConverterFactory.create())
//        .build()

    val api = retrofit.create(WeatherApi::class.java)

    val cities = listOf("Chisinau", "Madrid", "Kyiv", "Amsterdam")

    // checking if API key was read
    // println("debug key = $key")

    // trying one city
    // val respTest = api.forecast(key, "Madrid", days = 2)
    // println("debug forecastday size = ${respTest.forecast.forecastday.size}")
    // println("debug tomorrow = ${respTest.forecast.forecastday.getOrNull(1)}")

    //  figuring out header date using the first city that works
    var dateHeader = "Tomorrow" // placeholder
    for (c in cities) {
        //  try/catch per attempt
            try {
                val r = api.forecast(key, c, days = 2)
                val t = r.forecast.forecastday.getOrNull(1)  // grab index 1 for tomorrow
                if (t != null) {
                    dateHeader = t.date
                    break
                }
            } catch (_: Exception) {
                // checking which city failed
                // println("debug header failed for $c")
            }
        }

    //  printing border and header
    fun line() = "+----------------+--------------------------------------------------+"
    println(line())
    println("| City           | ${dateHeader.padEnd(50)}|")
    println(line())

    //  fetching data for each city and printing
    for (city in cities) {
        try {
            val resp = api.forecast(key, city, days = 2)
            val tomorrow = resp.forecast.forecastday.getOrNull(1)

            if (tomorrow == null) {
                println("| ${resp.location.name.padEnd(15)} | ${"error: no 'tomorrow' data".padEnd(50)}|")
                println(line())
                continue
            }

            val d = tomorrow.day

            //  finding the hour with the strongest wind and fetching its direction
            var windDir = "N/A"

            //  checking if there are hours to check
            if (tomorrow.hour.isNotEmpty()) {
                //  finding first hour as a starting point
                var windiestHour = tomorrow.hour[0]
                //  going through each hour
                for (h in tomorrow.hour) {
                    if (h.windKph > windiestHour.windKph) {
                        windiestHour = h
                    }
                }

                windDir = windiestHour.windDir
            }

            // printing values
            // println("DEBUG $city raw -> min=${d.tempMin}, max=${d.tempMax}, hum=${d.humidityAvg}, wind=${d.windKph}")

            // city name only on the first line, then blank on following lines
            println("| ${resp.location.name.padEnd(15)} | ${"temp min: ${d.tempMin}°C".padEnd(50)}|")
            println("| ${"".padEnd(15)} | ${"temp max: ${d.tempMax}°C".padEnd(50)}|")
            println("| ${"".padEnd(15)} | ${"hum: ${d.humidityAvg}%".padEnd(50)}|")
            println("| ${"".padEnd(15)} | ${"wind: ${d.windKph} kph".padEnd(50)}|")
            println("| ${"".padEnd(15)} | ${"dir: $windDir".padEnd(50)}|")
            println(line())

        } catch (e: Exception) {
            println("| ${city.padEnd(15)} | ${("error: " + (e.message ?: "unknown")).padEnd(50)}|")
            println(line())
        }
    }
}
