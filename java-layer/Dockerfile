ARG BASE_IMAGE=adafef2e596e/base-image:latest
FROM ${BASE_IMAGE}

# Accept Java version as build argument
ARG JAVA_VERSION

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-openjdk-amd64

# Switch to root for installations
USER root

# Install Java and essential tools
RUN set -ex \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        openjdk-${JAVA_VERSION}-jdk-headless \
        maven \
        gradle \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    # Verify installation
    && java -version \
    && javac -version \
    && mvn -version \
    && gradle -version

# Create app directory and set permissions
RUN mkdir -p /app \
    && chown -R devops:devops /app

# Set working directory
WORKDIR /app

# Switch back to non-root user
USER devops

# Verify installation as non-root user
RUN java -version && javac -version

# Default command
CMD ["java", "-version"]