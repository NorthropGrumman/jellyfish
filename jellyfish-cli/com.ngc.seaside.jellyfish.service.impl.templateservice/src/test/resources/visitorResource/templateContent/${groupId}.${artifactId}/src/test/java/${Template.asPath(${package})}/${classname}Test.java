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
package ${package};

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ${classname}Test {

    private ILogService logService;
    private ${classname} fixture;

    @Before
    public void setUp() {
        logService = new PrintStreamLogService();
        fixture = new ${classname}();
        fixture.setLogService(logService);
        fixture.activate();
    }

    @After
    public void tearDown() {
        fixture.deactivate();
        fixture.removeLogService(logService);
    }

    @Test
    public void testNameIsCorrect() {
        assertEquals("${commandName}", fixture.getName());
    }

    @Test
    public void testUsageIsCorrect() {
        IUsage usage = fixture.getUsage();

        assertTrue("The usage description must not be null.", usage.getDescription() != null);
        assertFalse("The usage description must not be empty.", usage.getDescription().trim().isEmpty());

        // Ensure all required parameters are listed correctly.
        assertEquals("Number of required parameters is incorrect.", 5, usage.getRequiredParameters().size());
        assertEquals("Number of all parameters is incorrect.", 5, usage.getAllParameters().size());
    }

    @Test(expected = CommandException.class)
    public void testRunThrowsExceptionWithNoOptions() {
        IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
        IParameterCollection collection = mock(IParameterCollection.class);

        when(options.getParameters()).thenReturn(collection);
        fixture.run(options);

        verify(options, times(1)).getParameters();
    }
}
