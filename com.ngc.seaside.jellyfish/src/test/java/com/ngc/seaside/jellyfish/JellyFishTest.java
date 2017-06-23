package com.ngc.seaside.jellyfish;

import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.inject.Injector;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.impl.provider.JellyFishCommandProviderModule;
import com.ngc.seaside.systemdescriptor.service.api.ParsingException;

@RunWith(MockitoJUnitRunner.class)
public class JellyFishTest {

	@Before
	public void before() throws Throwable {
		String ngFwHome = Paths.get(System.getProperty("user.dir"), "build", "resources", "test").toAbsolutePath()
				.toString();
		System.setProperty("NG_FW_HOME", ngFwHome);

	}

	@Test
	public void validSDProjectParsedTest() {

		String root = Paths.get(System.getProperty("user.dir"), "build", "resources", "test", "valid-project")
				.toAbsolutePath().toString();

		JellyFish.main(new String[] { "-Droot=" + root });
	}

	@Test
	public void systemDescriptorLoadedIntoMemoryTest() {
		String root = Paths.get(System.getProperty("user.dir"), "build", "resources", "test", "valid-project")
				.toAbsolutePath().toString();
		Injector injector = JellyFish.getInjector();
		JellyFishCommandProviderModule module = injector.getInstance(JellyFishCommandProviderModule.class);
		module.run(new String[] { "-Droot=" + root });
		IJellyFishCommandOptions options = module.getCommandOptions();
		Assert.assertNotNull(options);
		Assert.assertNotNull(options.getSystemDescriptor());
	}

	@Test(expected = ParsingException.class)
	public void invalidSdProjectStructureParsed() {

		String root = Paths.get(System.getProperty("user.dir"), "build", "resources", "test", "invalid-grammar-project")
				.toAbsolutePath().toString();

		Injector injector = JellyFish.getInjector();
		JellyFishCommandProviderModule module = injector.getInstance(JellyFishCommandProviderModule.class);
		module.run(new String[] { "-Droot=" + root });
		IJellyFishCommandOptions options = module.getCommandOptions();
	}
	
	  @Test(expected = IllegalArgumentException.class)
	   public void invalidDirProjectStructureParsed() {

	      String root = Paths.get(System.getProperty("user.dir"), "build", "resources", "test", "invalid-dir-struct-project")
	            .toAbsolutePath().toString();

	      Injector injector = JellyFish.getInjector();
	      JellyFishCommandProviderModule module = injector.getInstance(JellyFishCommandProviderModule.class);
	      module.run(new String[] { "-Droot=" + root });
	      IJellyFishCommandOptions options = module.getCommandOptions();
	   }

	@After
	public void after() throws Throwable {
		System.clearProperty("NG_FW_HOME");
	}
}
