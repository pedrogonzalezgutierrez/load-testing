package save

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.util.Random

class SaveCustomerJPA extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("http://localhost:8090")

  // infinite random entries
  val randomFeeder: Iterator[Map[String, Any]] = Iterator.continually(Map(
    "randomInt" -> Random.nextInt(100),
    "randomStringSmall" -> Random.alphanumeric.take(10).mkString,
    "randomStringMedium" -> Random.alphanumeric.take(15).mkString,
    "randomStringLong" -> Random.alphanumeric.take(20).mkString))

  val scn: ScenarioBuilder = scenario("Save Customer using JPA")
    .feed(randomFeeder)
    .exec(http("Save Customer with Alias")
    .post("/customer/aliases")
    .header("Content-Type", "application/json")
    .body(ElFileBody("saveJPA.json"))
    .check(status.is(200)))

  // Perform a single request (user)
//  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)

  // Perform X request (users) every second, so a total of X*Y users after Y seconds
  setUp(scn.inject(constantUsersPerSec(100) during 30)).protocols(httpProtocol)
}
