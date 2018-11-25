package find

import io.gatling.core.Predef._
import io.gatling.core.feeder.SourceFeederBuilder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class FindCustomerJPA extends Simulation {

  val csvFeeder: SourceFeederBuilder[String] = csv("customerJPA.csv").random

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("http://localhost:8090")

  val scn: ScenarioBuilder = scenario("Find Customer using JPA")
    .feed(csvFeeder)
    .exec(http("Find Customer with Alias")
    .get("/customer/${id}")
    .check(status.is(200)))

  // Perform a single request (user)
//  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)

  // Perform X request (users) every second, so a total of X*Y users after Y seconds
  setUp(scn.inject(constantUsersPerSec(150) during 20)).protocols(httpProtocol)
}
