package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.MissionStore
import java.io.File
import models.Users

object Application extends Controller with Secured {
  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    ) verifying ("Invalid username or password", result => result match {
      case (username, password) => Users.authenticate(username, password).isDefined
    })
  )

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }
  
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => {
        val username = user._1
        Redirect(routes.Application.index).withSession(SessionKey -> username)
      }
    )
  }
  
  def index = IsAuthenticated { implicit request =>
    Ok(views.html.index())
  }

  def missionUpload = IsAuthenticated(parse.multipartFormData) { implicit request =>
    request.body.file("mission").map { mission =>

      val filename = mission.filename
      try {
        MissionStore.store(mission.ref, filename)
        Redirect(routes.Application.index).flashing(
          "success" -> "Mission Uploaded")
      } catch {
        case e: Exception => Redirect(routes.Application.index).flashing(
          "error" -> e.getMessage)
      }

    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Select a file first")
    }
  }

}