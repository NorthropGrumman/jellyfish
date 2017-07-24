package com.ngc.blocs.service.log.impl.common.log4jlogservice.module;

import com.google.inject.AbstractModule;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.log.impl.common.log4jlogservice.Log4jLogService;

public class Log4jLogServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ILogService.class).to(Log4jLogService.class);
   }
}
