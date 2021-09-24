package common

import zio.{Has, ZIO, Layer}
import com.github.dapperware.slack.Token
import zio.config.ReadError
import zio.config.typesafe.TypesafeConfig
import zio.config.ConfigDescriptor
import ConfigDescriptor._

type Basic = Has[BasicConfig]

val default: Layer[ReadError [String], Basic] =
    TypesafeConfig.fromDefaultLoader(nested("basic") { BasicConfig.descriptor })

val accessToken: ZIO[Basic, Nothing, Token] =
    ZIO.service[BasicConfig].map(c => Token(c.token))

case class BasicConfig(token: String, channel: String)

object BasicConfig {
  implicit val descriptor: ConfigDescriptor[BasicConfig] =
    ConfigDescriptor
      .string("token")
      .zip(ConfigDescriptor.string("channel"))(
        (app: (String, String)) => BasicConfig(app._1, app._2),
        b => Some((b.token, b.channel))
      )
}
