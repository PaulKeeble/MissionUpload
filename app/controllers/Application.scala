package controllers

import play.api._
import play.api.mvc._
import models.MissionStore
import java.io.File

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def missionUpload = Action(parse.multipartFormData) { implicit request =>
    request.body.file("mission").map { mission =>

      val filename = mission.filename
      try {
	      MissionStore.store(mission.ref, filename)
	      Redirect(routes.Application.index).flashing(
	          "success" -> "Mission Uploaded")
      } catch {
        case e:Exception =>  Redirect(routes.Application.index).flashing(
          "error" -> e.getMessage)
      }

    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Select a file first")
    }
  }
  
}