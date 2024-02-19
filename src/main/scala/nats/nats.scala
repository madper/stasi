package nats

import zio._
import io.nats.client._
import io.nats.client.api.ConsumerConfiguration

import java.io.EOFException

import io.nats.client.impl.NatsMessage
import java.time.Duration
import java.nio.charset.StandardCharsets

class NatsService(connection: Connection) {
  def subscribe(topic: String): Task[Subscription] = {
    ZIO.attemptBlocking(connection.subscribe(topic))
  }
  def publish(message: Message): Task[Unit] = {
    ZIO.attemptBlocking(connection.publish(message))
  }
  def getMsg(sub: Subscription): Task[Message] = {
    ZIO.attemptBlocking(sub.nextMessage(Duration.ofDays(100)))
  }
  def close(): Task[Unit] = {
    ZIO.attemptBlocking(connection.close())
  }
}

object NatsService {
  def apply(host: String, port: Int): NatsService =
    new NatsService(Nats.connect(s"nats://$host:$port"))

  def subscribe(topic: String): ZIO[NatsService, Throwable, Subscription] = {
    ZIO.serviceWithZIO[NatsService](_.subscribe(topic))
  }
  def publish(subject: String, data: String): ZIO[NatsService, Throwable, Unit] = {
    val message = NatsMessage.builder().
      subject(subject).
      data(data, StandardCharsets.UTF_8).
      build()
    ZIO.serviceWithZIO[NatsService](_.publish(message))
  }
  def getMsg(sub: Subscription): ZIO[NatsService, Throwable, Message] = {
    ZIO.serviceWithZIO[NatsService](_.getMsg(sub))
  }
  def close(): ZIO[NatsService, Throwable, Unit] = {
    ZIO.serviceWithZIO[NatsService](_.close())
  }

  def layer(host: String, port: Int): ZLayer[Any, Throwable, NatsService] = {
    ZLayer.succeed(NatsService(host, port))
  }
}
