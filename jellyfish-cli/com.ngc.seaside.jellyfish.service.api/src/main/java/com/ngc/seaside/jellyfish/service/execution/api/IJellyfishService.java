package com.ngc.seaside.jellyfish.service.execution.api;

import com.google.inject.Module;

import java.util.Collection;
import java.util.Map;

/**
 * THe
 */
public interface IJellyfishService {

   IJellyfishExecution run(String command, Collection<String> arguments, Collection<Module> modules);

   IJellyfishExecution run(String command, Map<String, String> arguments, Collection<Module> modules);
}
