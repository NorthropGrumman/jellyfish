package com.ngc.seaside.bootstrap.service.promptuser.api;

import java.util.function.Predicate;

/**
 * @author justan.provence@ngc.com
 */
public interface IPromptUserService {

   String prompt(String parameter, String defaultValue, Predicate<String> validator);
}
