import zio._
import zio.stream._
import zio.Console._
import presistence._
import io.getquill._
import io.getquill.jdbczio.Quill
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import nats.NatsService
import presistence.PresistenceService

object Stasi extends ZIOAppDefault {
  def run = natsStream
    .provide(
      NatsService.layer("100.93.3.29", 4222),
      PresistenceService.live,
      Quill.Postgres.fromNamingStrategy(Literal),
      Quill.DataSource.fromPrefix("TSConfig")
    )
    .exitCode

  val natsStream = {
    for {
      subHeartBeat <- NatsService.subscribe("HEARTBEAT.>")
      subBootUp <- NatsService.subscribe("BOOTUP.>")
      _ <- ZIO.logInfo("start dealing with messages")
      s <- ZStream
        .fromZIO(NatsService.getMsg(subHeartBeat))
        .forever
        .map(msg =>
          val body = new String(msg.getData())
          decode[DeviceHeartBeat](body)
        )
        .mapZIOParUnordered(4)(_ match
          case Left(error) =>
            ZIO.logError(s"failed to encode json ${error.toString()}")
          case Right(dhb) => PresistenceService.insertDeviceHeartBeat(dhb)
        )
        .runDrain
        .catchAllCause(ZIO.logInfoCause(_))
    } yield (s)
  }
}
