package com.domino.smerp.logging.provider;

public interface ActionLogEntityProvider {

    String getEntity();

    Object loadSnapshot(String entityId);
}