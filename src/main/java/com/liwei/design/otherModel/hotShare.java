package com.liwei.design.otherModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class hotShare implements Serializable {
  @Getter
  @Setter
  private String userId;

  @Getter
  @Setter
  private String url;

  @Getter
  @Setter
  private String hot;
}
