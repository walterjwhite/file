package com.walterjwhite.file.providers.box.service;

import com.box.sdk.BoxAPIConnection;
import javax.inject.Provider;

public class BoxAPIConnectionProvider implements Provider<BoxAPIConnection> {
  // TODO: configure this via properties
  protected final String DEVELOPER_TOKEN = "Ztxxp3gszZygFK3XcmFmbuFDBi1Rl4kn";
  protected final String CLIENT_ID = "9j5yl4gzuau24q72ejhse7o8s8ygm92k";
  protected final String CLIENT_SECRET = "zaQle5NPaGHPyIistg9zeUmI6iCtEVyC";

  @Override
  public BoxAPIConnection get() {
    return new BoxAPIConnection(CLIENT_ID, CLIENT_SECRET, "YOUR-AUTH-CODE");
  }
}
