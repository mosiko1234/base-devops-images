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

# Install core dependencies and Java
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        software-properties-common \
        build-essential \
        curl \
        wget \
        gpg \
        git \
        unzip && \
    # Add Adoptium repository for Java
    wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | apt-key add - && \
    echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        temurin-${JAVA_VERSION}-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install Maven
ARG MAVEN_VERSION=3.9.6
RUN wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt/ && \
    rm apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    ln -s /opt/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/local/bin/mvn

# Install Gradle
ARG GRADLE_VERSION=8.5
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip -d /opt/ gradle-${GRADLE_VERSION}-bin.zip && \
    rm gradle-${GRADLE_VERSION}-bin.zip && \
    ln -s /opt/gradle-${GRADLE_VERSION}/bin/gradle /usr/local/bin/gradle

# Create workspace directory
WORKDIR /workspace

# Set environment variables
ENV JAVA_HOME=/usr/lib/jvm/temurin-${JAVA_VERSION}-jdk-amd64 \
    MAVEN_HOME=/opt/apache-maven-${MAVEN_VERSION} \
    GRADLE_HOME=/opt/gradle-${GRADLE_VERSION} \
    PATH=${PATH}:${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${GRADLE_HOME}/bin

# Verify installations
RUN java -version && \
    javac -version && \
    mvn -version && \
    gradle -version

# Set default entrypoint
ENTRYPOINT ["/bin/bash", "-c"]
CMD ["java -version"]

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
    CMD java -version || exit 1