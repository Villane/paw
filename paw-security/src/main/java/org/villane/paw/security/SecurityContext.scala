package org.villane.paw.security

import java.security.Principal
import java.security.AccessControlException

/**
 * SecurityContext provides a means for accessing a Principal and its Permissions.
 * 
 * Note that while the Principal is a java.security.Principal,
 *  the Permissions are not from java.security but have a much simpler implementation.
 * 
 * Important: if you want to use a custom default SecurityContext,
 *  the code that sets it must be in the org.villane.paw.security package.
 * Code in that package can change the value of SecurityContext.default
 */
object SecurityContext extends SecurityContext {

  /**
   * This principal can be optionally used when no actual principal is present,
   * but the application needs a Principal instance.
   * 
   * e.g. SecurityContext.principal getOrElse SecurityContext.UnknownPrincipal
   */
  object UnknownPrincipal extends Principal {
    val getName = "UNKNOWN"
    override def toString = "Principal[UNKNOWN]"
  }

  /**
   * Returns the current Principal or None.
   * 
   * Delegates to the default Context, returns None if no default Context is set.
   */
  def principal = default flatMap (_.principal)

  /**
   * Returns the current Principal's Permissions or NoPermissions.
   * 
   * Note: return value of NoPermissions does not indicate that there is no current Principal.
   * It is possible that the current principal indeed has no permissions.
   * 
   * Delegates to the default Context, returns NoPermissions if no default Context is set.
   */
  def permissions = default match {
    case Some(sp) => sp.permissions
    case None => NoPermissions
  }

  /** Returns the default SecurityContext or None */
  def get: Option[SecurityContext] = default

  /**
   * The default SecurityContext. To change this,
   *  you should declare the code that does the changing to be
   * in the package org.villane.paw.security or a sub-package.
   */
  protected[security] var default: Option[SecurityContext] = None

}

/**
 * Security Contexts must implement this interface.
 */
trait SecurityContext {
  /** Returns the current principal or None */ 
  def principal: Option[Principal]
  /** Returns the current principal's name or None */
  def principalName = principal map (_.getName)
  /** Returns the current principal's Permissions */
  def permissions: Permissions

  /** Same as permissions implies p */
  def has(p: Permission) = permissions implies p
  /** Same as permissions implies ps */
  def has(ps: Iterable[Permission]) = permissions implies ps

  /** Throws an exception if the required permission p is not implied */
  @throws(classOf[AccessControlException])
  def mustHave(p: Permission) = if (!has(p))
    throw new AccessControlException("Missing permission: " + p)

  /** Throws an exception if the required permissions ps are not implied */
  @throws(classOf[AccessControlException])
  def mustHave(ps: Iterable[Permission]) = if (!has(ps))
    throw new AccessControlException("Missing permissions: " + ps.mkString(", "))

}
