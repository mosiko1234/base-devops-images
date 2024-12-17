# Use a base image
ARG BASE_IMAGE=adafef2e596e/base-image:latest
FROM ${BASE_IMAGE}

# Add metadata
LABEL maintainer="moshee@kayhut.com" \
      description="Java development environment"

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

# Accept Java version as an argument
ARG JAVA_VERSION

# Install core dependencies
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
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install Amazon Corretto JDK
RUN mkdir -p /etc/apt/keyrings && \
    curl -L -o /etc/apt/keyrings/corretto.key https://apt.corretto.aws/corretto.key && \
    echo "deb [signed-by=/etc/apt/keyrings/corretto.key] https://apt.corretto.aws stable main" > /etc/apt/sources.list.d/corretto.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        java-${JAVA_VERSION}-amazon-corretto-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-amazon-corretto

# Install Maven
ARG MAVEN_VERSION=3.9.6
ENV MAVEN_HOME=/opt/maven
RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xz -C /opt/ && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME} && \
    ln -s ${MAVEN_HOME}/bin/mvn /usr/local/bin/mvn

# Install Gradle
ARG GRADLE_VERSION=8.5
ENV GRADLE_HOME=/opt/gradle
RUN curl -fsSL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle.zip && \
    unzip -q gradle.zip -d /opt && \
    rm gradle.zip && \
    ln -s /opt/gradle-${GRADLE_VERSION} ${GRADLE_HOME} && \
    ln -s ${GRADLE_HOME}/bin/gradle /usr/local/bin/gradle

# Create workspace directory
WORKDIR /workspace

# Update PATH
ENV PATH=${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${GRADLE_HOME}/bin:${PATH}

# Verify installations
RUN echo "Java version:" && java -version && \
    echo "Maven version:" && mvn -version && \
    echo "Gradle version:" && gradle -version

# Set default entrypoint
ENTRYPOINT ["/bin/bash", "-c"]
CMD ["java -version"]

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
    CMD java -version || exit 1