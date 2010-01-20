package org.villane.paw.security
package jaas

import java.security.AccessControlContext
import java.security.AccessController
import java.security.Principal
import javax.security.auth.Subject

object JAASSecurityContext extends SecurityContext {
  SecurityContext.default = Some(this)

  object ThreadLocalPermissions extends ThreadLocal[Permissions] {
    override def initialValue = NoPermissions 
  }

  def principal = Subject.getSubject(AccessController.getContext) match {
    case null => None
    case subj =>
      val set = subj.getPrincipals
      if (set.isEmpty) {
        None
      } else {
        val i = set.iterator
        if (i.hasNext) Some(i.next) else None
      }
  }

  def permissions = ThreadLocalPermissions.get  

}
