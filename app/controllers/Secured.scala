package controllers

import play.api.mvc.Security.Authenticated
import play.api.mvc.Request
import play.api.mvc.RequestHeader
import play.api.mvc.Results
import play.api.mvc.Result
import play.api.mvc.AnyContent
import play.api.mvc.Action
import play.api.mvc.BodyParser

trait Secured {
  val SessionKey = "username"
  
  def username(request: RequestHeader) = request.session.get(SessionKey)
  
  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)
  
  def IsAuthenticated(f: => Request[AnyContent] => Result) = Authenticated(username, onUnauthorized) { user =>
    Action(request => f(request))
  }
  
  def IsAuthenticated[A](parser:BodyParser[A])(f: => Request[A] => Result) = Authenticated(username, onUnauthorized) { user =>
    Action(parser)(request => f(request))
  }
}