package controllers

import models._
import javax.inject.{Inject, Singleton}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.cache._
import play.api.routing.{ JavaScriptReverseRouter, Router, JavaScriptReverseRoute }

/** Application controller, handles authentication */
@Singleton
class Application @Inject() (router: Router) extends Controller with Security {

  /** Serves the index page, see views/index.scala.html */
  def index = Action {
    Ok(views.html.index())
  }

  /**
   * Retrieves all routes via reflection.
   * http://stackoverflow.com/questions/12012703/less-verbose-way-of-generating-play-2s-javascript-router
   */
  val routeCache: Array[JavaScriptReverseRoute] = {
    val jsRoutesClass = classOf[routes.javascript]
    val controllers = jsRoutesClass.getFields.map(_.get(null))
    //    controllers.flatMap { controller =>
    //      controller.getClass.getDeclaredMethods.map { action =>
    //        action.invoke(controller).asInstanceOf[JavaScriptReverseRoute]
    //      }
    //    }
    //http://stackoverflow.com/questions/30519754/classcastexception-when-asinstanceof-in-for-comprehension
    val routeArray = for (
      controller <- controllers;
      method <- controller.getClass.getDeclaredMethods if method.getReturnType == classOf[JavaScriptReverseRoute]
    ) yield method.invoke(controller).asInstanceOf[play.api.routing.JavaScriptReverseRoute]
    routeArray
  }

  /**
   * Returns the JavaScript router that the client can use for "type-safe" routes.
   * Uses browser caching; set duration (in seconds) according to your release cycle.
   * @param varName The name of the global variable, defaults to `jsRoutes`
   */
  def jsRoutes(varName: String = "jsRoutes") = Cached(_ => "jsRoutes") {
    Action { implicit request =>
      Ok(JavaScriptReverseRouter(varName)(routeCache: _*)).as(JAVASCRIPT)
    }
  }

  /** Used for obtaining the email and password from the HTTP login request */
  case class LoginCredentials(email: String, password: String)

  /** JSON reader for [[LoginCredentials]]. */
  implicit val LoginCredentialsFromJson = (
    (__ \ "email").read[String](minLength[String](5)) ~
      (__ \ "password").read[String](minLength[String](2))
    )((email, password) => LoginCredentials(email, password))

  /**
   * Log-in a user. Expects the credentials in the body in JSON format.
   *
   * Set the cookie [[AuthTokenCookieKey]] to have AngularJS set the X-XSRF-TOKEN in the HTTP
   * header.
   *
   * @return The token needed for subsequent requests
   */
  def login() = Action(parse.json) { implicit request =>
    request.body.validate[LoginCredentials].fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      credentials => {
        // TODO Check credentials, log user in, return correct token
        User.findByEmailAndPassword(credentials.email, credentials.password).fold {
          BadRequest(Json.obj("status" -> "KO", "message" -> "User not registered"))
        } { user =>
          /*
           * For this demo, return a dummy token. A real application would require the following,
           * as per the AngularJS documentation:
           *
           * > The token must be unique for each user and must be verifiable by the server (to
           * > prevent the JavaScript from making up its own tokens). We recommend that the token is
           * > a digest of your site's authentication cookie with a salt) for added security.
           *
           */
          val token = java.util.UUID.randomUUID.toString
          Cache.set(token, user.id.get)
          Ok(Json.obj("token" -> token))
            .withCookies(Cookie(AuthTokenCookieKey, token, None, httpOnly = false))
        }
      }
    )
  }

  /**
   * Log-out a user. Invalidates the authentication token.
   *
   * Discard the cookie [[AuthTokenCookieKey]] to have AngularJS no longer set the
   * X-XSRF-TOKEN in HTTP header.
   */
  def logout() = HasToken(parse.empty) { token => userId => implicit request =>
    Cache.remove(token)
    Ok.discardingCookies(DiscardingCookie(name = AuthTokenCookieKey))
  }

}
