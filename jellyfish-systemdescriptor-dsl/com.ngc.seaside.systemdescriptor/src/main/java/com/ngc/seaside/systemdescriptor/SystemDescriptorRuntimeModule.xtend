/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor

import com.google.inject.Binder
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import com.ngc.seaside.systemdescriptor.scoping.ExceptionRethrowingScopeProviderErrrorHandler
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider
import org.eclipse.xtext.util.PolymorphicDispatcher.ErrorHandler
import org.eclipse.xtext.util.IResourceScopeCache

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class SystemDescriptorRuntimeModule extends AbstractSystemDescriptorRuntimeModule {

	def void configureErrorHandlers(Binder binder) {
		// Enable scope provider errors to bubble up and be made visible.
		binder.bind(new TypeLiteral<ErrorHandler<IScope>>(){})
			.annotatedWith(Names.named(AbstractDeclarativeScopeProvider.NAMED_ERROR_HANDLER))
			.to(ExceptionRethrowingScopeProviderErrrorHandler)
	}
	
	def void configureCaches(Binder binder) {
		// Disable caching of scopes.  This is a workaround to the bug regarding
		// issues where valid imports cannot be resolved.
		binder.bind(IResourceScopeCache)
			.toInstance(IResourceScopeCache.NullImpl.INSTANCE);
	}
}
