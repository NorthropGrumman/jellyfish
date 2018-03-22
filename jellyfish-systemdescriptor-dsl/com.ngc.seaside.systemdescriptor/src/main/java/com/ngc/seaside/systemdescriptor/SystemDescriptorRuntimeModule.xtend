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
