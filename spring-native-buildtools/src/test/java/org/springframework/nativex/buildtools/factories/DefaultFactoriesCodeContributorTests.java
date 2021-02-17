/*
 * Copyright 2002-2021 the original author or authors.
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

package org.springframework.nativex.buildtools.factories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.core.type.classreading.TypeSystem;
import org.springframework.nativex.buildtools.BuildContext;
import org.springframework.nativex.buildtools.TypeSystemExtension;
import org.springframework.nativex.buildtools.factories.fixtures.PublicFactory;
import org.springframework.nativex.buildtools.factories.fixtures.TestFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DefaultFactoriesCodeContributor}
 *
 * @author Brian Clozel
 */
@ExtendWith(TypeSystemExtension.class)
class DefaultFactoriesCodeContributorTests {

	DefaultFactoriesCodeContributor contributor = new DefaultFactoriesCodeContributor();

	@Test
	void shouldContributeWhenPublicConstructor(TypeSystem typeSystem) {
		SpringFactory factory = SpringFactory.resolve(TestFactory.class.getName(), PublicFactory.class.getName(), typeSystem);
		assertThat(this.contributor.canContribute(factory)).isTrue();
	}

	@Test
	void shouldNotContributeWhenPublicConstructorNotAvailable(TypeSystem typeSystem) throws Exception {
		Class<?> protectedClass = getClass().getClassLoader()
			.loadClass("org.springframework.nativex.buildtools.factories.fixtures.ProtectedFactory");
		SpringFactory factory = SpringFactory.resolve(TestFactory.class.getName(),
				"org.springframework.nativex.buildtools.factories.fixtures.ProtectedFactory", typeSystem);
		assertThat(this.contributor.canContribute(factory)).isFalse();
	}

	@Test
	void shouldContributeStaticStatement(TypeSystem typeSystem) {
		CodeGenerator code = new CodeGenerator();
		SpringFactory factory = SpringFactory.resolve(TestFactory.class.getName(), PublicFactory.class.getName(), typeSystem);
		this.contributor.contribute(factory, code, Mockito.mock(BuildContext.class));
		assertThat(code.generateStaticSpringFactories().toString())
				.contains("factories.add(org.springframework.nativex.buildtools.factories.fixtures.TestFactory.class, " +
						"() -> new org.springframework.nativex.buildtools.factories.fixtures.PublicFactory());\n");
	}

	@Test
	void shouldContributeStaticStatementForInnerClass(TypeSystem typeSystem) {
		CodeGenerator code = new CodeGenerator();
		SpringFactory factory = SpringFactory.resolve(TestFactory.class.getName(), PublicFactory.InnerFactory.class.getName(), typeSystem);
		this.contributor.contribute(factory, code, Mockito.mock(BuildContext.class));
		assertThat(code.generateStaticSpringFactories().toString())
				.contains("factories.add(org.springframework.nativex.buildtools.factories.fixtures.TestFactory.class, " +
						"() -> new org.springframework.nativex.buildtools.factories.fixtures.PublicFactory.InnerFactory());\n");
	}

}