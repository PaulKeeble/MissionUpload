package models

import play.api.Play
import play.api.libs.Files.TemporaryFile
import java.io.File

case class StoreFailure(message:String)

class StoreFailedException(val message:String) extends Exception(message)

object MissionStore {
  val MissionsDir = Play.current.configuration.getString("missions.dir")

  def store(missionFile: TemporaryFile, missionFileName: String): Unit = {
    MissionsDir match {
      case None => throw new StoreFailedException("Mission directory not configured")
      case Some(dir) => {
        val newFile = new File(dir + missionFileName)
        
        if(newFile.exists) {
          throw new StoreFailedException("Mission already exists")
        } else {
	        missionFile.moveTo(newFile)
        }
      }

    }
  }
}