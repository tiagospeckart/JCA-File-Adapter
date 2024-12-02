package com.dcx.jfoss.fra.api;

import jakarta.resource.Referenceable;

import java.io.Serializable;

public interface JCAFileAdapterConnectionFactory extends Serializable, Referenceable {
  JCAFileAdapterConnection getConnection() throws JCAFileAdapterException;
}
