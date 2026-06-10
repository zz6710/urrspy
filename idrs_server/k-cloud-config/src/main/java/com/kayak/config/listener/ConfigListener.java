package com.kayak.config.listener;

@FunctionalInterface
public interface ConfigListener {

	void receiveConfig(final String config);

}
