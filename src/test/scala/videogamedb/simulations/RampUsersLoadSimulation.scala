package videogamedb.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RampUsersLoadSimulation extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  def getAllVideoGames() = {
    exec(http("Get all video games")
    .get("/videogame")
    )
  }

  def getSpecificVideoGame() = {
    exec(http("Get specific video game")
    .get("/videogame/2")
    )
  }
  val scn = scenario("Ramp users load simulation")
    .exec(getAllVideoGames())
    .pause(5)
    .exec(getSpecificVideoGame())
    .pause(5)
    .exec(getAllVideoGames())

  setUp(
    scn.inject(
      nothingFor(5),
      constantUsersPerSec(10).during(10),
      rampUsersPerSec(1).to(5).during(20)
    ).protocols(httpProtocol))
}
