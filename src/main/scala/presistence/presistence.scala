package presistence

import io.getquill._
import io.getquill.jdbczio.Quill
import java.sql.SQLException
import java.sql.Timestamp
import java.time.Instant
import zio._
import io.circe._, io.circe.parser._, io.circe.syntax._, io.circe.generic.auto._
import scala.util.Try

case class DeviceHeartBeat(
  timestamp: Instant,
  deviceID: String,
  containers: Seq[String],
  city: Option[Int],
  ip: Option[String],
  isp: Option[String],
  region: Option[String]
)

implicit val decodeInstant: Decoder[Instant] = Decoder.decodeLong.emap{ long =>
  Right(Instant.ofEpochMilli(long))
}

class PresistenceService(quill: Quill.Postgres[Literal]) {
  import quill._
  def insertDeviceHeartBeat(
      dhb: DeviceHeartBeat
  ) = {
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
