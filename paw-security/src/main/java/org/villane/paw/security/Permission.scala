package org.villane.paw.security

/**
 * A Permission has a single important property: it can imply other Permissions.
 *
 * The design is inspired by java.security.Permission but greatly simplified.
 *
 * A Permission implementation should usually imply itself
 *  (or an equal object).
 *
 * Application code can require a Permission to be implied
 *  before allowing the execution of other code to continue.
 */
trait Permission {
  /**
   * Whether anoter Permission is implied by this Permission.
   *
   * Implication should be reflexive, but not necessarily symmetric or transitive.
   */
  def implies(that: Permission): Boolean
}

/**
 * A Permission subtype that simplifies the implementation of
 *  simple self-implying Permissions.
 *
 * Here, implication is strictly based on equality. 
 */
trait SimplePermission extends Permission {
  final def implies(that: Permission) = this == that
}

/**
 * Permissions is a collection of Permissions, and is a Permission itself.
 * It is not a traditional collection in that the
 * elements inside a Permissions collection are not necessarily visible.
 * 
 * It should only be interesting whether the collection as a whole
 *  can imply other permissions.  
 *
 * Permissions adds a convenience method that allows it to imply
 *  a list of Permissions.
 * Implying a list of permissions means that
 *  every permission in the list is implied.
 *
 * An empty list of permissions should be implied by all implementations.
 * Similarly, NoPermissions should be implied by all implementations.
 * 
 * Permissions can be nested.
 *
 * TODO maybe extend immutable.Set[Permission] after learning about extending 2.8 collections
 */
sealed abstract trait Permissions extends Permission {
  def implies(ps: Iterable[Permission]): Boolean = (ps.isEmpty) || (ps forall implies)
}

/**
 * AllPermissions always implies other permissions, with no exceptions.
 */
final object AllPermissions extends Permissions {
  def implies(p: Permission) = true
  override def implies(ps: Iterable[Permission]) = true
}

/**
 * NoPermissions is a convenience object to be used in cases where no permissions are provided or required.
 *
 * It doesn't imply any other permissions, except itself or an empty list.
 *
 * NoPermissions should be implied by any Permissions collection.
 *
 * This means that requiring NoPermissions has the same effect as not requiring any permission.
 * Similarly, requiring an empty list of permissions is the same as not requiring permissions.
 * And this also applies to a list containing only NoPermissions. 
 *
 * Note that a single Permission doesn't necessarily imply NoPermissions.
 * The reason for this is to simplify Permission implementations --
 *  it would be bothersome to make a special case for NoPermissions for every Permission implementation. 
 */
final object NoPermissions extends Permissions with SimplePermission

/**
 * A simple permission collection initialized from an immutable set.
 * 
 * This implementation may have less than optimal performance in case of a large number of permissions.
 * 
 * TODO provide optimized implementations or means of extending.
 */
final class PermissionSet(perms: collection.immutable.Set[Permission]) extends Permissions {
  def implies(p: Permission) = (p == NoPermissions) || (perms exists (_ implies p))
}
