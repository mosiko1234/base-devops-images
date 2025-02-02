# Use a base image
ARG BASE_IMAGE=adafef2e596e/base-image:latest
FROM ${BASE_IMAGE}

# Add metadata
LABEL maintainer="moshee@kayhut.com" \
      description="Java development environment"

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

ARG JAVA_VERSION
RUN if [ -z "$JAVA_VERSION" ]; then echo "JAVA_VERSION is not set!"; exit 1; fi

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        software-properties-common \
        build-essential \
        curl \
        wget \
        gpg \
        git \
        unzip \
        ca-certificates && \
    rm -rf /var/lib/apt/lists/*

RUN mkdir -p /etc/apt/keyrings && \
    curl --fail -L -o /etc/apt/keyrings/corretto.key https://apt.corretto.aws/corretto.key && \
    echo "deb [signed-by=/etc/apt/keyrings/corretto.key] https://apt.corretto.aws stable main" | tee /etc/apt/sources.list.d/corretto.list && \
    apt-get update && \
    if [ "$JAVA_VERSION" = "8" ]; then \
        apt-get install -y --no-install-recommends java-1.8.0-amazon-corretto; \
    else \
        apt-get install -y --no-install-recommends java-${JAVA_VERSION}-amazon-corretto-jdk; \
    fi && \
    rm -rf /var/lib/apt/lists/*



RUN if [ "$JAVA_VERSION" = "8" ]; then \
        export JAVA_HOME=/usr/lib/jvm/java-1.8.0-amazon-corretto; \
    else \
        export JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-amazon-corretto; \
    fi && \
    echo "export JAVA_HOME=${JAVA_HOME}" >> /etc/profile && \
    echo "export JAVA_HOME=${JAVA_HOME}" >> /root/.bashrc && \
    echo "export JAVA_HOME=${JAVA_HOME}" >> /etc/environment

ENV JAVA_HOME="/usr/lib/jvm/java-${JAVA_VERSION}-amazon-corretto"
ENV PATH="${JAVA_HOME}/bin:${PATH}"




ARG MAVEN_VERSION=3.9.6
ENV MAVEN_HOME=/opt/maven
RUN wget -qO- https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xz -C /opt/ && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME} && \
    ln -s ${MAVEN_HOME}/bin/mvn /usr/local/bin/mvn


ARG GRADLE_VERSION=8.5
ENV GRADLE_HOME=/opt/gradle
RUN wget -qO gradle.zip https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip -q gradle.zip -d /opt && \
    rm gradle.zip && \
    ln -s /opt/gradle-${GRADLE_VERSION} ${GRADLE_HOME} && \
    ln -s ${GRADLE_HOME}/bin/gradle /usr/local/bin/gradle


WORKDIR /workspace


ENV PATH=${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${GRADLE_HOME}/bin:${PATH}


RUN echo "Java version:" && java -version && \
    echo "Maven version:" && mvn -version && \
    echo "Gradle version:" && gradle -version


ENTRYPOINT ["/bin/bash", "-c", "exec \"$@\"", "--"]
CMD ["java", "-version"]

HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
    CMD java -version || exit 1
