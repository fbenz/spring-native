/*
 * Copyright 2019-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aot.build;

import org.springframework.aot.build.context.BuildContext;
import org.springframework.core.Ordered;
import org.springframework.nativex.AotOptions;

/**
 * A callback for contributing sources and resources to the Bootstrap source set.
 * Contributors get access to the application runtime classpath, but do not see artifacts
 * generated by other contributors.
 *
 * <p>Invoked with an {@link Ordered order} of {@code 0} by default, considering overriding {@link #getOrder()}
 * to customize this behaviour.
 * 
 * @author Brian Clozel
 * @author Sebastien Deleuze
 */
@FunctionalInterface
public interface BootstrapContributor extends Ordered {

	/**
	 * Contribute additional sources and resources to the Bootstrap source set.
	 * @param context the current build context
	 * @param aotOptions the AOT options
	 */
	void contribute(BuildContext context, AotOptions aotOptions);

	@Override
	default int getOrder() {
		return 0;
	}

	default boolean supportsAotPhase(AotPhase aotPhase) {
		return true;
	}

}