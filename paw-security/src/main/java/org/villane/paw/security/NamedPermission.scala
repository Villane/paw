package org.villane.paw.security

/**
 * A named permission implies another named permission if the names equal and the classes equal.
 */
trait NamedPermission extends SimplePermission {
  def name: String

  override def equals(that: Any) = that match {
    case p: NamedPermission => p.getClass == this.getClass && p.name == this.name
    case _ => false
  }

  override def hashCode = name.hashCode
}
