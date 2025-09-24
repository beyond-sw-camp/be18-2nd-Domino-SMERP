package com.domino.smerp.bom.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BomChangedEvent {
  private final Long changedItemId;
}
