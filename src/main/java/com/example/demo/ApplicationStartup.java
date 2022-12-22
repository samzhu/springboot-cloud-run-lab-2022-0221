package com.example.demo;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartup {

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {

        log.info("java.version={}", System.getProperty("java.version"));
        log.info("java.vendor={}", System.getProperty("java.vendor"));
        log.info("java.vendor.url={}", System.getProperty("java.vendor.url"));
        log.info("java.vm.name={}", System.getProperty("java.vm.name"));
        log.info("java.vm.vendor={}", System.getProperty("java.vm.vendor"));
        log.info("java.vm.version={}", System.getProperty("java.vm.version"));
        log.info("java.runtime.name={}", System.getProperty("java.runtime.name"));
        log.info("java.runtime.version={}", System.getProperty("java.runtime.version"));
        log.info("java.class.version={}", System.getProperty("java.class.version"));
    }

}
