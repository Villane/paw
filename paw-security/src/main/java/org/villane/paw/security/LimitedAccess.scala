package org.villane.paw.security

/**
 * Can be implemented by business objects that need permission based filtering in the service layer.
 */
trait LimitedAccess {
  /** Lists all the Permissions required to access the object */
  def requiredPermissions: List[Permission]
}

/**
 * Apply to a collection of LimitedAccess objects to filter out the objects for which permissions are lacking.
 * 
 * e.g. xs filter LimitedAccessFilter
 */
object LimitedAccessFilter extends (LimitedAccess => Boolean) {
  def apply(obj: LimitedAccess) = SecurityContext has obj.requiredPermissions
}
