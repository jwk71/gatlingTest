package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class CodeReuse extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  def getAllVideoGames(): ChainBuilder = {
    repeat(3){
      exec(http("Get all video games")
        .get("/videogame")
        .check(status.is(200)))
    }
  }

  def getSpecificGame(): ChainBuilder = {
    repeat(5,"counter") {
      exec(http("Get specific game with id: #{counter}")
        .get("/videogame/1")
        .check(status.in(200 to 210)))
    }
  }

  val scn = scenario("Code reuse")
    .exec(getAllVideoGames())
    .pause(5)
    .exec(getSpecificGame())
    .repeat(2){
      getAllVideoGames()
    }
    .exec(getAllVideoGames())

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
