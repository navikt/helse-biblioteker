val fuelVersion = "1.15.1"
val orgJsonVersion = "20180813"
val wireMockVersion = "2.19.0"

dependencies {
   compile("org.json:json:$orgJsonVersion")
   compile("com.github.kittinunf.fuel:fuel:$fuelVersion")

   testCompile("com.github.tomakehurst:wiremock:$wireMockVersion")
}