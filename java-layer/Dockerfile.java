# Use a base image
ARG BASE_IMAGE=adafef2e596e/base-image:latest
FROM ${BASE_IMAGE}

# Add metadata
LABEL maintainer="moshee@kayhut.com" \
      description="Java development environment"

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

# קבלת גרסת Java כמשתנה בעת הבנייה
ARG JAVA_VERSION
RUN if [ -z "$JAVA_VERSION" ]; then echo "JAVA_VERSION is not set!"; exit 1; fi

# קביעת JAVA_HOME דרך ENV (לא בתוך RUN)
ENV JAVA_HOME="/usr/lib/jvm/java-${JAVA_VERSION}-amazon-corretto"
ENV PATH="${JAVA_HOME}/bin:/opt/maven/bin:/opt/gradle/bin:${PATH}"

# יצירת משתמש לא-פריבילגי (אבטחה)
RUN groupadd -g 1000 appuser && \
    useradd -u 1000 -g appuser -m appuser && \
    mkdir -p /workspace && \
    chown -R appuser:appuser /workspace

# התקנת חבילות בסיס
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

# התקנת Amazon Corretto JDK (תומך ב-arm64 וב-x86_64)
RUN mkdir -p /etc/apt/keyrings && \
    curl --fail -L -o /etc/apt/keyrings/corretto.key https://apt.corretto.aws/corretto.key && \
    echo "deb [signed-by=/etc/apt/keyrings/corretto.key] https://apt.corretto.aws stable main" | tee /etc/apt/sources.list.d/corretto.list && \
    apt-get update && \
    if [ "$JAVA_VERSION" = "8" ]; then \
        if [ "$(uname -m)" = "aarch64" ]; then \
            CORRETTO_URL="https://corretto.aws/downloads/latest/amazon-corretto-8-aarch64-linux-jdk.tar.gz"; \
        else \
            CORRETTO_URL="https://corretto.aws/downloads/latest/amazon-corretto-8-x64-linux-jdk.tar.gz"; \
        fi && \
        wget -qO corretto-8.tar.gz "$CORRETTO_URL" && \
        mkdir -p /usr/lib/jvm/java-1.8.0-amazon-corretto && \
        tar -xzf corretto-8.tar.gz --strip-components=1 -C /usr/lib/jvm/java-1.8.0-amazon-corretto && \
        rm corretto-8.tar.gz && \
        ln -s /usr/lib/jvm/java-1.8.0-amazon-corretto/bin/java /usr/local/bin/java; \
        ENV JAVA_HOME="/usr/lib/jvm/java-1.8.0-amazon-corretto"; \
    else \
        apt-get install -y --no-install-recommends java-${JAVA_VERSION}-amazon-corretto-jdk; \
    fi && \
    rm -rf /var/lib/apt/lists/*

# התקנת Maven
ARG MAVEN_VERSION=3.9.6
ENV MAVEN_HOME=/opt/maven
RUN wget -qO- https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz > maven.tar.gz && \
    tar xzf maven.tar.gz -C /opt/ && \
    ln -s "/opt/apache-maven-${MAVEN_VERSION}" "${MAVEN_HOME}" && \
    ln -s "${MAVEN_HOME}/bin/mvn" /usr/local/bin/mvn && \
    rm maven.tar.gz

# התקנת Gradle
ARG GRADLE_VERSION=8.5
ENV GRADLE_HOME=/opt/gradle
RUN wget -qO gradle.zip https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip -q gradle.zip -d /opt && \
    ln -s "/opt/gradle-${GRADLE_VERSION}" "${GRADLE_HOME}" && \
    ln -s "${GRADLE_HOME}/bin/gradle" /usr/local/bin/gradle && \
    rm gradle.zip

# עבודה כמשתמש לא-פריבילגי
USER appuser
WORKDIR /workspace

# אימות התקנות
RUN echo "JAVA_HOME is set to: $JAVA_HOME" && \
    java -version && \
    mvn -version && \
    gradle -version

# Healthcheck רלוונטי (אם יש שירות רץ)
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s \
    CMD curl -f http://localhost:8080/health || exit 1

# Entrypoint גמיש
ENTRYPOINT ["/bin/bash", "-c", "exec \"$@\"", "--"]
CMD ["java", "-version"]
