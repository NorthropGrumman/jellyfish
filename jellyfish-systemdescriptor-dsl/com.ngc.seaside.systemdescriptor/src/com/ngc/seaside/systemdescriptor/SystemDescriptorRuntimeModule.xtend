package com.ngc.seaside.systemdescriptor

import com.google.inject.Binder
import org.eclipse.xtext.util.PolymorphicDispatcher.ErrorHandler
import com.google.inject.name.Names
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider
import com.ngc.seaside.systemdescriptor.errorhandling.ExceptionRethrowingErrorHandler
import org.eclipse.xtext.scoping.IScope
import com.ngc.seaside.systemdescriptor.exception.UnhandledScopingException
import com.google.inject.TypeLiteral

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class SystemDescriptorRuntimeModule extends AbstractSystemDescriptorRuntimeModule {

	def void configureErrorHandlers(Binder binder) {
		// Enable scope provider errors to bubble up and be made visible.
		binder.bind(new TypeLiteral<ErrorHandler<IScope>>(){})
			.annotatedWith(Names.named(AbstractDeclarativeScopeProvider.NAMED_ERROR_HANDLER))
			.toInstance(new ExceptionRethrowingErrorHandler<IScope>([t|new UnhandledScopingException(t.message, t)]));
	}
}
