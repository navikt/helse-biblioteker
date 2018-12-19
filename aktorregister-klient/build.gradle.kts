val fuelVersion = "1.15.1"
val orgJsonVersion = "20180813"
val wireMockVersion = "2.19.0"
val mockkVersion = "1.8.12.kotlin13"

dependencies {

   compile("org.slf4j:slf4j-api:1.7.25")

   compile("org.json:json:$orgJsonVersion")
   compile("com.github.kittinunf.fuel:fuel:$fuelVersion")
   compile(project(":sts-klient"))

   testCompile("io.mockk:mockk:$mockkVersion")
   testCompile("com.github.tomakehurst:wiremock:$wireMockVersion")
}
