package models

import play.api.Play

object Users {
  val AdminPass = Play.current.configuration.getString("admin.password")
  
  def authenticate(username:String,password:String):Option[String] = {
    Some(password) match {
      case AdminPass => if("admin" == username) Some("admin") else None
      case _ => None
    }
  }
}