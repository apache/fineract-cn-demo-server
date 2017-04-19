package io.mifos.dev;

import io.mifos.provisioner.api.v1.domain.Application;

class ApplicationBuilder {

  private ApplicationBuilder() {
    super();
  }

  static Application create(final String name, final String uri) {
    final Application application = new Application();
    application.setName(name);
    application.setHomepage(uri);
    application.setVendor("Apache Fineract");
    return application;
  }
}
