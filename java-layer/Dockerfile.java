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

# התקנת Amazon Corretto JDK בהתאם לגרסה
RUN mkdir -p /etc/apt/keyrings && \
    curl --fail -L -o /etc/apt/keyrings/corretto.key https://apt.corretto.aws/corretto.key && \
    echo "deb [signed-by=/etc/apt/keyrings/corretto.key] https://apt.corretto.aws stable main" | tee /etc/apt/sources.list.d/corretto.list && \
    apt-get update && \
    if [ "$JAVA_VERSION" = "8" ]; then \
        wget -qO corretto-8.tar.gz "https://corretto.aws/downloads/latest/amazon-corretto-8-x64-linux-jdk.tar.gz" && \
        mkdir -p /usr/lib/jvm/java-1.8.0-amazon-corretto && \
        tar -xzf corretto-8.tar.gz --strip-components=1 -C /usr/lib/jvm/java-1.8.0-amazon-corretto && \
        rm corretto-8.tar.gz && \
        ln -s /usr/lib/jvm/java-1.8.0-amazon-corretto/bin/java /usr/local/bin/java; \
        export JAVA_HOME=/usr/lib/jvm/java-1.8.0-amazon-corretto; \
    else \
        apt-get install -y --no-install-recommends java-${JAVA_VERSION}-amazon-corretto-jdk && \
        ln -s /usr/lib/jvm/java-${JAVA_VERSION}-amazon-corretto/bin/java /usr/local/bin/java; \
        export JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-amazon-corretto; \
    fi && \
    echo "export JAVA_HOME=${JAVA_HOME}" >> /etc/profile.d/java.sh && \
    echo "export PATH=${JAVA_HOME}/bin:${PATH}" >> /etc/profile.d/java.sh && \
    chmod +x /etc/profile.d/java.sh && \
    rm -rf /var/lib/apt/lists/*

# הגדרת JAVA_HOME ב-ENV כדי להיות זמין תמיד
ARG JAVA_HOME
ENV JAVA_HOME=${JAVA_HOME}
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# התקנת Maven
ARG MAVEN_VERSION=3.9.6
ENV MAVEN_HOME=/opt/maven
RUN wget -qO- https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xz -C /opt/ && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME} && \
    ln -s ${MAVEN_HOME}/bin/mvn /usr/local/bin/mvn

# התקנת Gradle
ARG GRADLE_VERSION=8.5
ENV GRADLE_HOME=/opt/gradle
RUN wget -qO gradle.zip https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip -q gradle.zip -d /opt && \
    rm gradle.zip && \
    ln -s /opt/gradle-${GRADLE_VERSION} ${GRADLE_HOME} && \
    ln -s ${GRADLE_HOME}/bin/gradle /usr/local/bin/gradle

# יצירת תיקיית העבודה
WORKDIR /workspace

# עדכון PATH
ENV PATH=${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${GRADLE_HOME}/bin:${PATH}

# אימות התקנות
RUN bash -c "source /etc/profile.d/java.sh && \
    echo \"Java version:\" && java -version && \
    echo \"Maven version:\" && mvn -version && \
    echo \"Gradle version:\" && gradle -version"

# Entrypoint כברירת מחדל
ENTRYPOINT ["/bin/bash", "-c", "exec \"$@\"", "--"]
CMD ["java", "-version"]

# בדיקת בריאות
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
    CMD java -version || exit 1
