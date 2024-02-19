package presistence

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import io.getquill._
import io.getquill.jdbczio.Quill
import java.sql.SQLException
import zio._

case class DeviceHeartBeat(
    timestamp: Long,
    deviceID: String,
    containers: Seq[String],
    city: Option[Int],
    ip: Option[String],
    isp: Option[String],
    region: Option[String]
)

class PresistenceService(quill: Quill.Postgres[SnakeCase]) {
  import quill._
  inline def insertDeviceHeartBeat(
      dhb: DeviceHeartBeat
  ): ZIO[Any, SQLException, Long] = {
    val a = quote { query[DeviceHeartBeat].insertValue(lift(dhb)) }
    run(a)
  }
}

object PresistenceService {
  def insertDeviceHeartBeat(
      dhb: DeviceHeartBeat
  ): ZIO[PresistenceService, SQLException, Long] =
    ZIO.serviceWithZIO[PresistenceService](_.insertDeviceHeartBeat(dhb))

  val live = ZLayer.fromFunction(new PresistenceService(_))
}
