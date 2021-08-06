import zio.{ExitCode, App, ZEnv, ZIO, Layer, Has, ZManaged}
import zio.console.{Console, putStrLn}
import zio.stream.ZStream
import com.github.dapperware.slack.{ realtime, SlackEnv, SlackError }
import com.github.dapperware.slack.realtime.{ SlackRealtimeClient, SlackRealtimeEnv}
import common.{Basic, default, accessToken, BasicConfig}
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import com.github.dapperware.slack.client.SlackClient
import com.github.dapperware.slack.AccessToken
import com.github.dapperware.slack.realtime.models.{ SendMessage, UserTyping }
import com.github.dapperware.slack.api.web
import sttp.client3.asynchttpclient.zio.SttpClient

object MainApp extends App:

  val slackClients: Layer[Throwable, SlackRealtimeClient & SlackClient] = 
    AsyncHttpClientZioBackend.layer() >>> (SlackRealtimeClient.live ++ SlackClient.live)
  val accessTokenAndBasicLayer: Layer[Throwable, AccessToken & Basic] = default >+> accessToken.toLayer

  val layer: Layer[Throwable, SlackEnv & SlackRealtimeEnv & Basic] =
    slackClients ++ accessTokenAndBasicLayer
      

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] = 
    (for {
      resp <- (testApi.toManaged_ <&> testRealtime).provideCustomLayer(layer)
    } yield resp).use_(ZIO.unit).exitCode

  val testRealtime: ZManaged[SlackRealtimeEnv & Basic & Console, SlackError, Unit] =
    for {
      config   <- ZManaged.service[BasicConfig]
      // Test that we can receive messages
      receiver <- realtime.connect(ZStream (SendMessage(config.channel, "Hi realtime!")))
      _        <- receiver.collectM {
                    case UserTyping(channel, user) => putStrLn(s"User $user is typing in $channel")
                    case _                         => ZIO.unit
                  }.runDrain.toManaged_
    } yield ()

  val testApi: ZIO[SlackEnv & Basic, SlackError, String] =
    for {
      config <- ZIO.service[BasicConfig]
      resp   <- web.postChatMessage(config.channel, text = "Hello Slack client!")
      _      <- web.addReactionToMessage("+1", config.channel, resp)
    } yield resp