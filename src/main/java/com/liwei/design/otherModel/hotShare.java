package com.liwei.design.otherModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class hotShare {
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
