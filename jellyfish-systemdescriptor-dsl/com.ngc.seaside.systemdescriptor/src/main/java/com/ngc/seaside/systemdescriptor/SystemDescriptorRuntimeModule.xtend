/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
