# Use base image
ARG BASE_IMAGE
FROM ${BASE_IMAGE}

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

# Accept Java version as an argument
ARG JAVA_VERSION

# Install Java dynamically
RUN apt-get update && \
    apt-get install -y --no-install-recommends openjdk-${JAVA_VERSION}-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set default JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

# Verify installation
RUN java -version

# Set default entrypoint
ENTRYPOINT ["/bin/bash", "-c"]
CMD ["java -version"]
