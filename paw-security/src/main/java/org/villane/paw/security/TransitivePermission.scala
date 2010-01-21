package org.villane.paw.security

/**
 * A transitive permission can imply other permissions transitively.
 */
trait TransitivePermission extends Permission {
  /** Override this method to change transitively implied Permissions. */ 
  def impliedPermissions: Iterable[Permission] = Nil

  /** Override this method to change directly implied Permissions. */
  def impliesDirectly(that: Permission) = this == that 

  final def implies(that: Permission) =
    impliesDirectly(that) || (impliedPermissions exists (_.implies(that)))
}
