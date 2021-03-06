package lila
package notification

import user.User
import socket.MetaHub

import collection.mutable
import play.api.templates.Html

final class Api(metaHub: MetaHub) {

  private val repo = mutable.Map[String, List[Notification]]()

  def add(userId: String, html: String, from: Option[String] = None) {
    val notif = Notification(userId, html, from)
    repo.update(userId, notif :: get(userId))
    val rendered = views.html.notification.view(notif.id, notif.from)(Html(notif.html))
    metaHub.addNotification(userId, rendered.toString)
  }

  def get(userId: String): List[Notification] = ~(repo get userId) 

  def remove(userId: String, id: String) {
    repo.update(userId, get(userId) filter (_.id != id))
    metaHub.removeNotification(userId, id)
  }
}
