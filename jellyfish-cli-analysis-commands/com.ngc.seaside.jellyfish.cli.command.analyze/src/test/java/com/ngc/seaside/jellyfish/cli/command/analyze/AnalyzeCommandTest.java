package com.ngc.seaside.jellyfish.cli.command.analyze;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnalyzeCommandTest {

   private AnalyzeCommand command;

   @Mock
   private ILogService logService;

   @Mock
   private IJellyFishCommandProvider jellyFishCommandProvider;

   @Mock
   private ICommandProvider<ICommandOptions, ICommand<ICommandOptions>, ICommandOptions> commandProvider;

   @Before
   public void setup() {
      command = new AnalyzeCommand();
      command.setLogService(logService);
      command.setJellyFishCommandProvider(jellyFishCommandProvider);
      command.setCommandProvider(commandProvider);
      command.activate();
   }

   @Test
   public void testDoesRunAnalysisAndReports() {
      // TODO TH: implement this
   }
}
