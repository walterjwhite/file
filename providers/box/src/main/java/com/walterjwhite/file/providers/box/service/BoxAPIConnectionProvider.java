package com.walterjwhite.file.providers.box.service;

import com.box.sdk.BoxAPIConnection;
import com.walterjwhite.file.providers.box.service.property.BoxClientId;
import com.walterjwhite.file.providers.box.service.property.BoxClientSecret;
import com.walterjwhite.file.providers.box.service.property.BoxDeveloperToken;
import com.walterjwhite.property.impl.annotation.Property;
import javax.inject.Provider;

public class BoxAPIConnectionProvider implements Provider<BoxAPIConnection> {
  protected final String clientId;
  protected final String clientSecret;
  protected final String developerToken;

  public BoxAPIConnectionProvider(
      @Property(BoxClientId.class) final String clientId,
      @Property(BoxClientSecret.class) final String clientSecret,
      @Property(BoxDeveloperToken.class) final String developerToken) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.developerToken = developerToken;
  }

  @Override
  public BoxAPIConnection get() {
    return new BoxAPIConnection(clientId, clientSecret, "YOUR-AUTH-CODE");
  }
}
